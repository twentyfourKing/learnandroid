Lifecycle
==========

## 作用

监听并响应Activity或者Fragment生命周期的变化

## api涉及的接口和类
> 针对的api版本是 appcompat-1.1.0 、fragment-1.1.0、activity-1.0.0、lifecycle-runtime-2.1.0

### Lifecycle

1) 是抽象类，定义有添加和移除观察者的抽象方法

2) 在类中还定义了标识生命周期和事件的两个枚举 State 、Event

### LifecycleRegistry

1) 是lifecycle的子类也是实现类

2) 添加和删除观察者，保存状态、更新状态、分发状态

3) 持有LifecycleOwner接口实现类的引用

4) 主要分析下addObserver(LifecycleObserver observer)
```java
    public void addObserver(@NonNull LifecycleObserver observer) {
        State initialState = mState == DESTROYED ? DESTROYED : INITIALIZED;
        //说明1
        ObserverWithState statefulObserver = new ObserverWithState(observer, initialState);
        ObserverWithState previous = mObserverMap.putIfAbsent(observer, statefulObserver);

        if (previous != null) {
            return;
        }
        LifecycleOwner lifecycleOwner = mLifecycleOwner.get();
        if (lifecycleOwner == null) {
            // it is null we should be destroyed. Fallback quickly
            return;
        }
        //说明2
        boolean isReentrance = mAddingObserverCounter != 0 || mHandlingEvent;
        State targetState = calculateTargetState(observer);
        mAddingObserverCounter++;
        while ((statefulObserver.mState.compareTo(targetState) < 0
                && mObserverMap.contains(observer))) {
            pushParentState(statefulObserver.mState);
            statefulObserver.dispatchEvent(lifecycleOwner, upEvent(statefulObserver.mState));
            popParentState();
            // mState / subling may have been changed recalculate
            targetState = calculateTargetState(observer);
        }

        if (!isReentrance) {
            // we do sync only on the top level.
            sync();
        }
        mAddingObserverCounter--;
    }
```
> 说明1

用ObserverWithState对传入的LifecycleObserver观察者进行了封装，
并通过键值对的方式保存在FastSafeIterableMap的map中，另外在保存的时候会有一个重复与否的校验逻辑

> 说明2

校验当前状态，并通过ObserverWithState的dispatchEvent分发状态到观察者


###  LifecycleOwner

1) 接口定义了getLifecycle()抽象方法，用来获取LifecycleOwner实现类中Lifecycle的实例


在 FragmentActivity中定义有
```java
    final LifecycleRegistry mFragmentLifecycleRegistry = new LifecycleRegistry(this);

    //在Activity生命周期变化时，会通过
    //mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.xx);
    //的方法进行状态的变更已经状态的分发
    public void handleLifecycleEvent(@NonNull Lifecycle.Event event) {
        State next = getStateAfter(event);
        moveToState(next);
    }
```
```java
    //LifecycleRegistry.java
    private void moveToState(State next) {
        if (mState == next) {
            return;
        }
        mState = next;
        if (mHandlingEvent || mAddingObserverCounter != 0) {
            mNewEventOccurred = true;
            // we will figure out what to do on upper level.
            return;
        }
        mHandlingEvent = true;
        sync();
        mHandlingEvent = false;
    }
```





### 使用
（一）针对Activity的观察

1.添加观察者
```java
    getLifecycle().addObserver（xx）
    //getLifecycle()调用的是父类ComponentActivity的getLifecycle()方法获得，在ComponentActivity中实例化的
    //LifecycleRegistry对象
    //xx是自定义的 LifecycleEventObserver匿名内部类，亦可以定义成成员内部类
    //实现onStateChanged方法，响应activity的生命周期变化
```

2.分发生命周期状态的变化

这个逻辑需要从ComponentActivity的onCreate方法说起,在onCreate方法中有ReportFragment.injectIfNeededIn(this);ReportFragment 就是专门来分发状态变化的，它本身是继承Fragment的。
通过FragmentManager将ReportFragment添加上，这样就能让ReportFragment跟随Activity的生命周期进行变化,这样ReportFragment就可以将Activityd的生命周期，通过dispatch(Lifecycle.Event.ON_START);的方式将生命周期的状态分发给LifecycleRegistry，再由LifecycleRegistry将状态分发到对应的观察者

```java
    private void dispatch(Lifecycle.Event event) {
        Activity activity = getActivity();
        if (activity instanceof LifecycleRegistryOwner) {
            ((LifecycleRegistryOwner) activity).getLifecycle().handleLifecycleEvent(event);
            return;
        }

        if (activity instanceof LifecycleOwner) {
            Lifecycle lifecycle = ((LifecycleOwner) activity).getLifecycle();
            if (lifecycle instanceof LifecycleRegistry) {
                ((LifecycleRegistry) lifecycle).handleLifecycleEvent(event);
            }
        }
    }
```

(二) 针对Fragment的观察

> api使用的是fragment-1.1.0就是androidx包中的Fragment

1.同样先添加观察者

自定义观察者,getLifecycle()获取的是Fragment中的实例化对象LifecycleRegistry。Fragment实现了LifecycleOwner接口
```java
    getLifecycle().addObserver(xx)
```


2.分发状态变化

Fragment生命周期变化与activity有一定的关系，它具体生命周期状态的变化是由
FragmentManagerImpl的moveToState操作的。最后流转到Fragment的 performCreate 、performStart等再由mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
进行分发，可以发现 Fragment处理的生命周期主要还是反应activity的生命，Fragment自身的一些生命周期其实并未进行状态分发


### 结论

>不论是activity中还是fragment中进行生命周期的观察，响应的其实都是
activity的生命周期

>有add 就有remove


