
![响应生命周期的概述](https://github.com/twentyfourKing/learnandroid/blob/master/learn_open_source/learn_picture_load/readme/img/img_4.png)

在继承于 AppCompatActivity 中使用 Glide.with(this)的方式构建 RequestManager 流程如下：

```java
    public static RequestManager with(@NonNull FragmentActivity activity) {
        return getRetriever(activity).get(activity);
      }
```
getRetriever(activity) 是 通过如下代码构建了 
```java
    private static RequestManagerRetriever getRetriever(@Nullable Context context) {
        ...有一个判空检验
        return Glide.get(context).getRequestManagerRetriever();
      }
```
get(FragmentActivity)的代码如下
```java
    public RequestManager get(@NonNull FragmentActivity activity) {
        if (Util.isOnBackgroundThread()) {
          return get(activity.getApplicationContext());
        } else {
          assertNotDestroyed(activity);
          FragmentManager fm = activity.getSupportFragmentManager();
          return supportFragmentGet(activity, fm, /*parentHint=*/ null, isActivityVisible(activity));
        }
      }
```
```java
    private RequestManager supportFragmentGet(
          @NonNull Context context,
          @NonNull FragmentManager fm,
          @Nullable Fragment parentHint,
          boolean isParentVisible) {
        //先拿到 SupportRequestManagerFragment 实例
        SupportRequestManagerFragment current =
            getSupportRequestManagerFragment(fm, parentHint, isParentVisible);
        // 然后获取 SupportRequestManagerFragment 实例中绑定的 RequestManager
        // 当然初始创建的  SupportRequestManagerFragment 的 RequestManager是空值
        RequestManager requestManager = current.getRequestManager();
        if (requestManager == null) {
          // TODO(b/27524013): Factor out this Glide.get() call.
          Glide glide = Glide.get(context);
          // factory 有两种情况 ，如果使用了注解，那么会自动创建一个Factory，不然就使用默认的 DEFAULT_FACTORY
          requestManager =
              factory.build(
                  glide, current.getGlideLifecycle(), current.getRequestManagerTreeNode(), context);
          // 将  SupportRequestManagerFragment 的 Lifecycle 绑定到 RequestManager
          current.setRequestManager(requestManager);
        }
        return requestManager;
      }
```

```java
    public RequestManagerRetriever(@Nullable RequestManagerFactory factory) {
        this.factory = factory != null ? factory : DEFAULT_FACTORY;
        handler = new Handler(Looper.getMainLooper(), this /* Callback */);
      }
```

默认构建了 RequestManagerFactory ,用来构建 RequestManager
```java
    private static final RequestManagerFactory DEFAULT_FACTORY =
          new RequestManagerFactory() {
            @NonNull
            @Override
            public RequestManager build(
                @NonNull Glide glide,
                @NonNull Lifecycle lifecycle,
                @NonNull RequestManagerTreeNode requestManagerTreeNode,
                @NonNull Context context) {
              return new RequestManager(glide, lifecycle, requestManagerTreeNode, context);
            }
          };
```
获取 SupportRequestManagerFragment 实例 
> 缓存有直接用，没有就创建新的
```java
    private SupportRequestManagerFragment getSupportRequestManagerFragment(
          @NonNull final FragmentManager fm, @Nullable Fragment parentHint, boolean isParentVisible) {
        SupportRequestManagerFragment current =
            (SupportRequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
          current = pendingSupportRequestManagerFragments.get(fm);
          if (current == null) {
            current = new SupportRequestManagerFragment();
            current.setParentFragmentHint(parentHint);
            if (isParentVisible) {
              current.getGlideLifecycle().onStart();
            }
            pendingSupportRequestManagerFragments.put(fm, current);
            fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
            handler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER, fm).sendToTarget();
          }
        }
        return current;
      }
```

对于 RequestManager中Lifecycle的使用

注册：

```java
    if (Util.isOnBackgroundThread()) {
          mainHandler.post(addSelfToLifecycle);
        } else {
          lifecycle.addListener(this);
        }
        lifecycle.addListener(connectivityMonitor);
```

回调回来的逻辑实现：比如onDestroy

清理和回收
```java
    public synchronized void onDestroy() {
        targetTracker.onDestroy();
        for (Target<?> target : targetTracker.getAll()) {
          clear(target);
        }
        targetTracker.clear();
        requestTracker.clearRequests();
        lifecycle.removeListener(this);
        lifecycle.removeListener(connectivityMonitor);
        mainHandler.removeCallbacks(addSelfToLifecycle);
        glide.unregisterRequestManager(this);
      }
```

