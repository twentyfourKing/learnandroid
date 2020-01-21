
#功能描述
支持直接解析标准URL进行跳转，并自动注入参数到目标页面中
支持多模块工程使用
支持添加多个拦截器，自定义拦截顺序（@Interceptor设置优先级）
支持依赖注入，可单独作为依赖注入框架使用(@Autowired)
支持InstantRun
支持MultiDex(Google方案)
映射关系按组分类、多级管理，按需初始化
支持用户指定全局降级与局部降级策略(实现DegradeService接口)
支持路径重解析（实现PathReplaceServices接口）
支持预处理机制（实现PretreatmentService接口）
页面、拦截器、服务等组件均自动注册到框架()
支持多种方式配置转场动画
支持获取Fragment
完全支持Kotlin以及混编(配置见文末 其他#5)
支持第三方 App 加固(使用 arouter-register 实现自动注册)
支持生成路由文档
提供 IDE 插件便捷的关联路径和目标类

官方介绍：https://github.com/alibaba/ARouter/blob/master/README_CN.md


#注解标注

@Route (标注路由标识)
@Autowired (用于依赖注入,自动解析传递的参数)
@Interceptor(用于标注拦截器，设置拦截器优先级，名字)



#初始化流程

ARouter.init->_ARouter.init()->Logistics.init()
    
    ARouter.init
    public static void init(Application application) {
            if (!hasInit) {
                logger = _ARouter.logger;
                _ARouter.logger.info(Consts.TAG, "ARouter init start.");
                hasInit = _ARouter.init(application);
    
                if (hasInit) {
                    _ARouter.afterInit();
                    //在初始化完成后，直接先获取到（通过依赖查找的方式,byType）
                    //interceptorService = (InterceptorService) ARouter.getInstance().build("/arouter/service/interceptor").navigation();
                    //InterceptorService用于处理拦截器逻辑
                    //InterceptorService接口继承于IProvider接口，那么它的类型就是RouteType.PROVIDER
                    //ARouter.getInstance().build("/arouter/service/interceptor").navigation()返回的就是postcard.getProvider()
                          
                }
    
                _ARouter.logger.info(Consts.TAG, "ARouter init over.");
            }
        }
##Logistics.init的作用总结：

通过扫描软件安装路径，找到dex文件，从dex文件中找到包名包好"com.alibaba.android.arouter.routes"包文件
存放在Set<String>中，并通过SharedPreferences做缓存
然后根据类全名，匹配对应的类，匹配规则跟apt生成的规则有关系
1）Root类：
((IRouteRoot) (Class.forName(className).getConstructor().newInstance())).loadInto(Warehouse.groupsIndex);
（就是存放在Warehouse 的 static Map<String, Class<? extends IRouteGroup>> groupsIndex = new HashMap<>();中）
2）Interceptors类
((IInterceptorGroup) (Class.forName(className).getConstructor().newInstance())).loadInto(Warehouse.interceptorsIndex);
3）Providers类
((IProviderGroup) (Class.forName(className).getConstructor().newInstance())).loadInto(Warehouse.providersIndex);

将满足规则的类名，用反射生成对应的类文件，并保存数据在仓库Warehouse中

#路由流程
1)构建Postcard
ARouter.getInstance().build(path)... 在_ARouter的build中，会根据path先进行路径的处理（可以重写跳转的路径或者url），
通过（PathReplaceServiceImpl implements PathReplaceService）实现PathReplaceService接口，并用@Route(path = "/xxx/xxx")
对PathReplaceServiceImpl进行标准，然后PathReplaceService pService = ARouter.getInstance().navigation(PathReplaceService.class)，
就可以重写路径了，最后根据路径和解析的group生成这次路由的Postcard
2）添加传递的数据
通过withxx（可以传递基本数据类型、序列化数据、切换动画等）

3)路由
navigation(),有很多种参数组合的方式进行路由，不过最后的参数组合如下
navigation(Activity mContext, int requestCode, NavigationCallback callback)，
然后进入ARouter.navigation->_ARouter.navigation(final Context context, final Postcard postcard,
    final int requestCode, final NavigationCallback callback)
    
    _ARouter.navigation作用总结：
    1）判断是否有预处理逻辑 PretreatmentService
    2）(LogisticsCenter.completion)对PostCard进行数据的封装（从仓库Warehouse）,默认为PROVIDER和Fragment开启绿色通道
    3）(_ARouter._navigation)根据PostCard的数据进行路由逻辑
    protected Object navigation(final Context context, final Postcard postcard, final int requestCode, final NavigationCallback callback) {
            //PretreatmentService预处理操作，可以自已实现PretreatmentService接口，并用@Route(path = "/xxx/xxx")进行标注
            //如果自己处理了就可以让onPretreatmen返回false，不进行流程的后续路由了
            PretreatmentService pretreatmentService = ARouter.getInstance().navigation(PretreatmentService.class);
            if (null != pretreatmentService && !pretreatmentService.onPretreatment(context, postcard)) {
                // Pretreatment failed, navigation canceled.
                return null;
            }
            try {
                //核心逻辑1
                LogisticsCenter.completion(postcard);
            } catch (NoRouteFoundException ex) {
               //有对异常的处理逻辑
                return null;
            }
            if (null != callback) {
                callback.onFound(postcard);
            }
            //判断是否开启了绿色通道
            if (!postcard.isGreenChannel()) {   // It must be run in async thread, maybe interceptor cost too mush time made ANR.
               
                //InterceptorServiceImpl的逻辑处理都是在线程池中异步完成，不阻塞主线程
                //未开启绿色通道，那么就会使用InterceptorService的实现类InterceptorServiceImpl进行拦截处理
                //具体逻辑在InterceptorServiceImpl实现的doInterceptions方法中
                //会判断Warehouse.interceptors是否存放有自定义的拦截器,如果存在拦截器，那么就会
                //调用InterceptorServiceImpl._excute，处理具体拦截器的iInterceptor.process(postcard, new InterceptorCallback()
                //逻辑
                // InterceptorCallback 回调用于处理拦截逻辑的后续处理，就是在拦截逻辑完成后，又回到主的路由流程中
                interceptorService.doInterceptions(postcard, new InterceptorCallback() {
                    /**
                     * Continue process
                     *
                     * @param postcard route meta
                     */
                    @Override
                    public void onContinue(Postcard postcard) {
                        _navigation(context, postcard, requestCode, callback);
                    }
    
                    /**
                     * Interrupt process, pipeline will be destory when this method called.
                     *
                     * @param exception Reson of interrupt.
                     */
                    @Override
                    public void onInterrupt(Throwable exception) {
                        if (null != callback) {
                            callback.onInterrupt(postcard);
                        }
    
                        logger.info(Consts.TAG, "Navigation failed, termination by interceptor : " + exception.getMessage());
                    }
                });
            } else {
                //核心逻辑2
                return _navigation(context, postcard, requestCode, callback);
            }
            return null;
        }
        
        1）LogisticsCenter.completion ->代码逻辑
        //根据postcard的path从仓库Warehouse中取出解析存放的RouteMeta
        //包含了类型、path、类信息、优先级等信息
        RouteMeta routeMeta = Warehouse.routes.get(postcard.getPath());
        if(routeMeta == null){
              //进行重新加载
              completion(postcard);   // Reload
        } else {
            //将RouteMeta的信息放在这次路由的Postcard中
            postcard.setDestination(routeMeta.getDestination());//类信息
            postcard.setType(routeMeta.getType());//类型
            postcard.setPriority(routeMeta.getPriority());//优先级
            postcard.setExtra(routeMeta.getExtra());//携带的数据信息
            Uri rawUri = postcard.getUri();
            if (null != rawUri) {   // Try to set params into bundle.
                Map<String, String> resultMap = TextUtils.splitQueryParameters(rawUri);
                Map<String, Integer> paramsType = routeMeta.getParamsType();
                if (MapUtils.isNotEmpty(paramsType)) {
                    // Set value by its type, just for params which annotation by @Param
                    for (Map.Entry<String, Integer> params : paramsType.entrySet()) {
                        setValue(postcard,
                                params.getValue(),
                                params.getKey(),
                                resultMap.get(params.getKey()));
                    }
                    // Save params name which need auto inject.
                    postcard.getExtras().putStringArray(ARouter.AUTO_INJECT, paramsType.keySet().toArray(new String[]{}));
                }
                // Save raw uri
                postcard.withString(ARouter.RAW_URI, rawUri.toString());
            }
            //根据数据类型进行处理
            switch (routeMeta.getType()) {
                case PROVIDER:  // if the route is provider, should find its instance
                    // Its provider, so it must implement IProvider
                    Class<? extends IProvider> providerMeta = (Class<? extends IProvider>) routeMeta.getDestination();
                    IProvider instance = Warehouse.providers.get(providerMeta);
                    if (null == instance) { // There's no instance of this provider
                        IProvider provider;
                        try {
                            provider = providerMeta.getConstructor().newInstance();
                            provider.init(mContext);
                            Warehouse.providers.put(providerMeta, provider);
                            instance = provider;
                        } catch (Exception e) {
                            throw new HandlerException("Init provider failed! " + e.getMessage());
                        }
                    }
                    postcard.setProvider(instance);//保存实现类到PostCard中
                    //Provider不接受拦截器的处理
                    postcard.greenChannel();    // Provider should skip all of interceptors
                    break;
                case FRAGMENT:
                    postcard.greenChannel();    // Fragment needn't interceptors
                default:
                    break;
            }
        }
        
        2）_navigation->代码逻辑
       
        private Object _navigation(final Context context, final Postcard postcard, final int requestCode, 
            final NavigationCallback callback) {
                final Context currentContext = null == context ? mContext : context;
        
                switch (postcard.getType()) {
                    case ACTIVITY:
                        // Build intent
                        final Intent intent = new Intent(currentContext, postcard.getDestination());
                        intent.putExtras(postcard.getExtras());
        
                        // Set flags.
                        int flags = postcard.getFlags();
                        if (-1 != flags) {
                            intent.setFlags(flags);
                        } else if (!(currentContext instanceof Activity)) {    // Non activity, need less one flag.
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
        
                        // Set Actions
                        String action = postcard.getAction();
                        if (!TextUtils.isEmpty(action)) {
                            intent.setAction(action);
                        }
        
                        // Navigation in main looper.
                        runInMainThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(requestCode, currentContext, intent, postcard, callback);
                            }
                        });
        
                        break;
                    case PROVIDER:
                        return postcard.getProvider();
                    case BOARDCAST:
                    case CONTENT_PROVIDER:
                    case FRAGMENT:
                        Class fragmentMeta = postcard.getDestination();
                        try {
                            Object instance = fragmentMeta.getConstructor().newInstance();
                            if (instance instanceof Fragment) {
                                ((Fragment) instance).setArguments(postcard.getExtras());
                            } else if (instance instanceof android.support.v4.app.Fragment) {
                                ((android.support.v4.app.Fragment) instance).setArguments(postcard.getExtras());
                            }
        
                            return instance;
                        } catch (Exception ex) {
                            logger.error(Consts.TAG, "Fetch fragment instance error, " + TextUtils.formatStackTrace(ex.getStackTrace()));
                        }
                    case METHOD:
                    case SERVICE:
                    default:
                        return null;
                }
        
                return null;
            }
        

#关于自动注册工具的使用
https://github.com/luckybilly/AutoRegister

#关于@Autowired

@Autowired这个标注的作用有两个:
前提是需要ARouter.getInstance().inject(this);的操作
>1、自动构建,用@Route标注的对象(用依赖注入的方式构建对象，在apt的自动代码中，又用的是依赖查找，可查看apt的Autowired代码)
>2、自动解析Intent中传递过来的数据，使用时参数的key应该作为@Autowired标注的变量名