#注解标注

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public @interface Subscribe {
        ThreadMode threadMode() default ThreadMode.POSTING;
    
        /**
         * If true, delivers the most recent sticky event (posted with
         * {@link EventBus#postSticky(Object)}) to this subscriber (if event available).
         */
        boolean sticky() default false;
    
        /** Subscriber priority to influence the order of event delivery.
         * Within the same delivery thread ({@link ThreadMode}), higher priority subscribers will receive events before
         * others with a lower priority. The default priority is 0. Note: the priority does *NOT* affect the order of
         * delivery among subscribers with different {@link ThreadMode}s! */
        int priority() default 0;
    }
>@Subscribe是运行时注解，在运行时通过反射进行获取，只能标注方法

##使用步骤1
用@Subscribe标注订阅的方法
##步骤2
在需要使用的地方先注册
>EventBus.getDefault().register(this);
##步骤3
在发布事件的地方
>EventBus.getDefault().post(xxEvent)

#流程分析
###首先是register
    public void register(Object subscriber) {
        //获取注册类的类型类
        Class<?> subscriberClass = subscriber.getClass();
        //findSubscriberMethods根据类型类，获取该类中订阅的方法列表
        List<SubscriberMethod> subscriberMethods = subscriberMethodFinder.findSubscriberMethods(subscriberClass);
        synchronized (this) {
            for (SubscriberMethod subscriberMethod : subscriberMethods) {
                //在同步锁下进行订阅
                subscribe(subscriber, subscriberMethod);
                //1) 用subscriptionsByEventType ->Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType
                //根据具体的"事件类型"来保存注册的类和类中的订阅方法
                //2) 用typesBySubscriber ->Map<Object, List<Class<?>>> typesBySubscriber
                //根据注册的类来保存，订阅的"事件"
            }
        }
    }
    >>跟进findSubscriberMethods方法
    List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass) {
            //先从缓存中拿
            List<SubscriberMethod> subscriberMethods = METHOD_CACHE.get(subscriberClass);
            if (subscriberMethods != null) {
                return subscriberMethods;
            }
    
            if (ignoreGeneratedIndex) {
                //用FindState来封装了订阅类，
                //然后用类的 getDeclaredMethods或者getMethods获取方法，然后根据@Subscribe注解进行解析
                //保存方法名
                subscriberMethods = findUsingReflection(subscriberClass);
            } else {
                //根据FindState.subscriberInfo来获取注解的方法
                //FindState.subscriberInfo为空，还是用反射的方式来获取方法
                subscriberMethods = findUsingInfo(subscriberClass);
            }
            if (subscriberMethods.isEmpty()) {
                throw new EventBusException("Subscriber " + subscriberClass
                        + " and its super classes have no public methods with the @Subscribe annotation");
            } else {
                //用注册类，对订阅方法进行缓存
                METHOD_CACHE.put(subscriberClass, subscriberMethods);
                return subscriberMethods;
            }
        }
###其次发布事件

    public void post(Object event) {
        //currentPostingThreadState是ThreadLocal的匿名内部类
        PostingThreadState postingState = currentPostingThreadState.get();
        List<Object> eventQueue = postingState.eventQueue;
        eventQueue.add(event);
        //postingState.isPosting默认是false
        if (!postingState.isPosting) {
            postingState.isMainThread = isMainThread();
            postingState.isPosting = true;
            if (postingState.canceled) {
                throw new EventBusException("Internal error. Abort state was not reset");
            }
            try {
                while (!eventQueue.isEmpty()) {
                    //边发布，边清除数据
                    postSingleEvent(eventQueue.remove(0), postingState);
                }
            } finally {
                postingState.isPosting = false;
                postingState.isMainThread = false;
            }
        }
    }
    
    private void postSingleEvent(Object event, PostingThreadState postingState) throws Error {
        Class<?> eventClass = event.getClass();//获取event的类型类
        boolean subscriptionFound = false;
        if (eventInheritance) {//eventInheritance表示是否处理继承的类
            //添加父类中的接口和父类
            //因为自定义的Event是类，那么它可能有继承关系或者实现了接口，这些场景都要考虑到
            List<Class<?>> eventTypes = lookupAllEventTypes(eventClass);
            int countTypes = eventTypes.size();
            for (int h = 0; h < countTypes; h++) {
                Class<?> clazz = eventTypes.get(h);
                subscriptionFound |= postSingleEventForEventType(event, postingState, clazz);
            }
        } else {
            subscriptionFound = postSingleEventForEventType(event, postingState, eventClass);
        }
        if (!subscriptionFound) {
            if (logNoSubscriberMessages) {
                logger.log(Level.FINE, "No subscribers registered for event " + eventClass);
            }
            if (sendNoSubscriberEvent && eventClass != NoSubscriberEvent.class &&
                    eventClass != SubscriberExceptionEvent.class) {
                post(new NoSubscriberEvent(this, event));
            }
        }
    }
    
    private boolean postSingleEventForEventType(Object event, PostingThreadState postingState, Class<?> eventClass) {
        CopyOnWriteArrayList<Subscription> subscriptions;
        synchronized (this) {
            subscriptions = subscriptionsByEventType.get(eventClass);//从注册保存的数据，根据事件类型取出订阅者（订阅的类和订阅的方法）
        }
        if (subscriptions != null && !subscriptions.isEmpty()) {
            for (Subscription subscription : subscriptions) {
                postingState.event = event;
                postingState.subscription = subscription;
                boolean aborted = false;
                try {
                    postToSubscription(subscription, event, postingState.isMainThread);
                    aborted = postingState.canceled;
                } finally {
                    postingState.event = null;
                    postingState.subscription = null;
                    postingState.canceled = false;
                }
                if (aborted) {
                    break;
                }
            }
            return true;
        }
        return false;
    }
    
    private void postToSubscription(Subscription subscription, Object event, boolean isMainThread) {
        //根据线程的类型通过反射调用方法，完成事件的post
        //如果存在线程的差别，还会存在线程的切换
        switch (subscription.subscriberMethod.threadMode) {
            case POSTING:
                invokeSubscriber(subscription, event);
                break;
            case MAIN://订阅的方法需要在主线程中执行
                if (isMainThread) {//如果发送的线程也是主线程，那么直接执行
                    invokeSubscriber(subscription, event);
                } else {
                    //如果发送的线程不是主线程，那么需要切换线程，需要在post的线程，通过handler机制进行处理
                    //把需要发送的event入队，并在handler中发送消息，然后处理消息
                    // 在处理消息时进行出队操作，进行具体方法的调用
                    mainThreadPoster.enqueue(subscription, event);
                }
                break;
            case MAIN_ORDERED:
                if (mainThreadPoster != null) {
                    mainThreadPoster.enqueue(subscription, event);
                } else {
                    // temporary: technically not correct as poster not decoupled from subscriber
                    invokeSubscriber(subscription, event);
                }
                break;
            case BACKGROUND:
                if (isMainThread) {
                    backgroundPoster.enqueue(subscription, event);
                } else {
                    invokeSubscriber(subscription, event);
                }
                break;
            case ASYNC:
                asyncPoster.enqueue(subscription, event);
                break;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscription.subscriberMethod.threadMode);
        }
    }
    
        