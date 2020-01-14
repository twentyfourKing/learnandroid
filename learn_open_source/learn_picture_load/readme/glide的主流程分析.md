
执行RequestBuilder.into(target)
假设这里的target是ImageView

```java
    // RequestBuilder.java
    private <Y extends Target<TranscodeType>> Y into(
      @NonNull Y target,
      @Nullable RequestListener<TranscodeType> targetListener,
      BaseRequestOptions<?> options,
      Executor callbackExecutor) {
        ...
        
        into(
            glideContext.buildImageViewTarget(view, transcodeClass),
            /*targetListener=*/ null,
            requestOptions,
            Executors.mainThreadExecutor());
     }
    
```
 1) transcodeClass其实就是构建执行RequestBuilder asxx方法的类型标识
 
 我们load(String) 所以默认使用asDrawable 。transcodeClass 就是Drawable
 
 ```java
    glideContext.buildImageViewTarget(view, transcodeClass)
 ```
 
 ```java
    //GlideContext.java
    
    // imageViewTargetFactory 为 ImageViewTargetFactory 
    public <X> ViewTarget<ImageView, X> buildImageViewTarget(
      @NonNull ImageView imageView, @NonNull Class<X> transcodeClass) {
        return imageViewTargetFactory.buildTarget(imageView, transcodeClass);
  }
 ```
 
 ```java
    // ImageViewTargetFactory.java
    public <Z> ViewTarget<ImageView, Z> buildTarget(
      @NonNull ImageView view, @NonNull Class<Z> clazz) {
        if (Bitmap.class.equals(clazz)) {
          return (ViewTarget<ImageView, Z>) new BitmapImageViewTarget(view);
        } else if (Drawable.class.isAssignableFrom(clazz)) {
          return (ViewTarget<ImageView, Z>) new DrawableImageViewTarget(view);
        } else {
          throw new IllegalArgumentException(
              "Unhandled class: " + clazz + ", try .as*(Class).transcode(ResourceTranscoder)");
        }
    }
    // clazz 是Drawable 那么返回的就是 new DrawableImageViewTarget(view);
 ```
 
 2) requestOptions 
 
 使用当前RequestBuilder中的BaseRequestOptions 根据图片的缩放格式，配置新的options
 
 3) Executors.mainThreadExecutor()
 
 构建可以与主线程通信的 Executors （实现线程切换）
 
**现在进入into()下个逻辑**
 
 ```java
    <Y extends Target<TranscodeType>> Y into(
      @NonNull Y target,
      @Nullable RequestListener<TranscodeType> targetListener,
      Executor callbackExecutor) {
    return into(target, targetListener, /*options=*/ this, callbackExecutor);
  }

  private <Y extends Target<TranscodeType>> Y into(
      @NonNull Y target,
      @Nullable RequestListener<TranscodeType> targetListener,
      BaseRequestOptions<?> options,
      Executor callbackExecutor) {
      
    ...

    Request request = buildRequest(target, targetListener, options, callbackExecutor);

    Request previous = target.getRequest();
    if (request.isEquivalentTo(previous)
        && !isSkipMemoryCacheWithCompletePreviousRequest(options, previous)) {
      if (!Preconditions.checkNotNull(previous).isRunning()) {
        previous.begin();
      }
      return target;
    }

    requestManager.clear(target);
    target.setRequest(request);
    requestManager.track(target, request);

    return target;
  }
 ```
 
 1) 构建新的 Request 
 
 [构建Request的流程看另一个md]()
 
 ```java
     Request request = buildRequest(target, targetListener, options, callbackExecutor);
 ```

 
 2) 判断Target上存在的Request与新构建的Request是否相同
 
 如果相同， 那么 判断已经存在的Request的执行状态 ，来决定是否执行新的
 
 3) 清理Target上存在的Request，然后将新的Request进行赋值
```java
     target.setRequest(request);
```

4) 执行新的Request
```java
    requestManager.track(target, request);
```

**回到RequestManager 执行 track**

```java
    synchronized void track(@NonNull Target<?> target, @NonNull Request request) {
    targetTracker.track(target);
    requestTracker.runRequest(request);
  }
```
> RequestTracker Request管理类

启动 Request

```java
    requestTracker.runRequest(request);
```
```java
    public void runRequest(@NonNull Request request) {
        requests.add(request);
        if (!isPaused) {
          request.begin();
        } else {
          request.clear();
          if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Paused, delaying request");
          }
          pendingRequests.add(request);
        }
  }
```

判断Request的状态，没有暂停，那么直接启动 begin()

如果是暂停的，那么加入暂停队列中，在后续找机会启动

```java
    pendingRequests.add(request);
```


从构建Request的流程我们知道 这里的Request是 SingleRequest

> 也许有方式的区别 （全图 和 缩略图）请求


分析主流程，没有暂停的情况

**进去SingleRequest.begin()**

1) 有同步锁，线程安全 synchronized (requestLock)  requestLock  对象锁

> requestLock = new Object();

2) model

这里的 model 就是 SingleRequest 构造方法中的参数 

> 我们主流程分析的是（ String 和 ImageView ）方式 ，那么这里的model其实就是 Drawable 

model 为空直接中断流程

3) 判断 请求的状态 status 

```java
    // 请求完成的状态 直接加载资源
    if (status == Status.COMPLETE) {
        onResourceReady(resource, DataSource.MEMORY_CACHE);
        return;
      }
```
4) 标注状态

```java
      status = Status.WAITING_FOR_SIZE;
      if (Util.isValidDimensions(overrideWidth, overrideHeight)) {
        onSizeReady(overrideWidth, overrideHeight);
      } else {
        target.getSize(this);
      }
```

4-1) 判断尺寸大小的合理性，如果合理直接
```java
    onSizeReady(overrideWidth, overrideHeight);
```

4-2) 不合理 ，那么重新计算，计算成功后 用回调，再次执行onSizeReady

> 这里的 target 源于  glideContext.buildImageViewTarget(view, transcodeClass) 的调用 

```java
    // RequestBuilder.java
    ViewTarget<ImageView, TranscodeType> into(@NonNull ImageView view){
        ...
        return into(
        glideContext.buildImageViewTarget(view, transcodeClass),
        /*targetListener=*/ null,
        requestOptions,
        Executors.mainThreadExecutor());
    }
```
> 因为我们分析的 String 和ImageView ，这里transcodeClass 是 Drawable 

> buildImageViewTarget返回的就是 DrawableImageViewTarget

**继续**

target 为 DrawableImageViewTarget

SizeReadyCallback 由 SingleRequest 实现 回调当然逻辑又回到 SingleRequest

```java
    //DrawableImageViewTarget.java
    target.getSize(this);
```

```java
    // ViewTarget.java
    public void getSize(@NonNull SizeReadyCallback cb) {
    sizeDeterminer.getSize(cb);
  }
```
```java
    void getSize(@NonNull SizeReadyCallback cb) {
          int currentWidth = getTargetWidth();
          int currentHeight = getTargetHeight();
          if (isViewStateAndSizeValid(currentWidth, currentHeight)) {
            cb.onSizeReady(currentWidth, currentHeight);
            return;
          }
    
          // We want to notify callbacks in the order they were added and we only expect one or two
          // callbacks to be added a time, so a List is a reasonable choice.
          if (!cbs.contains(cb)) {
            cbs.add(cb);
          }
          if (layoutListener == null) {
            ViewTreeObserver observer = view.getViewTreeObserver();
            layoutListener = new SizeDeterminerLayoutListener(this);
            observer.addOnPreDrawListener(layoutListener);
          }
    }
```
SingleRequest.begin()的完整逻辑如下：

```java
    // SingleRequest.java
    public void begin() {
        synchronized (requestLock) {
          assertNotCallingCallbacks();
          stateVerifier.throwIfRecycled();
          startTime = LogTime.getLogTime();
          if (model == null) {
            if (Util.isValidDimensions(overrideWidth, overrideHeight)) {
              width = overrideWidth;
              height = overrideHeight;
            }
            int logLevel = getFallbackDrawable() == null ? Log.WARN : Log.DEBUG;
            onLoadFailed(new GlideException("Received null model"), logLevel);
            return;
          }
          if (status == Status.RUNNING) {
            throw new IllegalArgumentException("Cannot restart a running request");
          }
          if (status == Status.COMPLETE) {
            onResourceReady(resource, DataSource.MEMORY_CACHE);
            return;
          }
          status = Status.WAITING_FOR_SIZE;
          if (Util.isValidDimensions(overrideWidth, overrideHeight)) {
            onSizeReady(overrideWidth, overrideHeight);
          } else {
            target.getSize(this);
          }
          if ((status == Status.RUNNING || status == Status.WAITING_FOR_SIZE)
              && canNotifyStatusChanged()) {
            target.onLoadStarted(getPlaceholderDrawable());
          }
          if (IS_VERBOSE_LOGGABLE) {
            logV("finished run method in " + LogTime.getElapsedMillis(startTime));
          }
        }
  }
```

**进入 onSizeReady()**

同步锁： synchronized (requestLock)

标记状态： status = Status.RUNNING;

获得缩略图的属性值：  requestOptions.getSizeMultiplier();

核心逻辑 ： engine.load

```java
    //SingleRequest.java
    public void onSizeReady(int width, int height) {
        stateVerifier.throwIfRecycled();
        synchronized (requestLock) {
          if (IS_VERBOSE_LOGGABLE) {
            logV("Got onSizeReady in " + LogTime.getElapsedMillis(startTime));
          }
          if (status != Status.WAITING_FOR_SIZE) {
            return;
          }
          status = Status.RUNNING;
    
          float sizeMultiplier = requestOptions.getSizeMultiplier();
          this.width = maybeApplySizeMultiplier(width, sizeMultiplier);
          this.height = maybeApplySizeMultiplier(height, sizeMultiplier);
    
          if (IS_VERBOSE_LOGGABLE) {
            logV("finished setup for calling load in " + LogTime.getElapsedMillis(startTime));
          }
          loadStatus =
              engine.load(
                  glideContext,
                  model,
                  requestOptions.getSignature(),
                  this.width,
                  this.height,
                  requestOptions.getResourceClass(),
                  transcodeClass,
                  priority,
                  requestOptions.getDiskCacheStrategy(),
                  requestOptions.getTransformations(),
                  requestOptions.isTransformationRequired(),
                  requestOptions.isScaleOnlyOrNoTransform(),
                  requestOptions.getOptions(),
                  requestOptions.isMemoryCacheable(),
                  requestOptions.getUseUnlimitedSourceGeneratorsPool(),
                  requestOptions.getUseAnimationPool(),
                  requestOptions.getOnlyRetrieveFromCache(),
                  this,
                  callbackExecutor);
    
          // This is a hack that's only useful for testing right now where loads complete synchronously
          // even though under any executor running on any thread but the main thread, the load would
          // have completed asynchronously.
          if (status != Status.RUNNING) {
            loadStatus = null;
          }
          if (IS_VERBOSE_LOGGABLE) {
            logV("finished onSizeReady in " + LogTime.getElapsedMillis(startTime));
          }
        }
  }
```
**进入 Engine.load()**

1) 将图的属性构建为  EngineKey 也作为缓存数据时的 key

2) 加载逻辑

```java
    public <R> LoadStatus load(xxx 省略) {
    long startTime = VERBOSE_IS_LOGGABLE ? LogTime.getLogTime() : 0;

    EngineKey key =
        keyFactory.buildKey(
            model,
            signature,
            width,
            height,
            transformations,
            resourceClass,
            transcodeClass,
            options);

    EngineResource<?> memoryResource;
    synchronized (this) {
      memoryResource = loadFromMemory(key, isMemoryCacheable, startTime);

      if (memoryResource == null) {
        return waitForExistingOrStartNewJob(
            glideContext,
            model,
            signature,
            width,
            height,
            resourceClass,
            transcodeClass,
            priority,
            diskCacheStrategy,
            transformations,
            isTransformationRequired,
            isScaleOnlyOrNoTransform,
            options,
            isMemoryCacheable,
            useUnlimitedSourceExecutorPool,
            useAnimationPool,
            onlyRetrieveFromCache,
            cb,
            callbackExecutor,
            key,
            startTime);
      }
    }

    // Avoid calling back while holding the engine lock, doing so makes it easier for callers to
    // deadlock.
    cb.onResourceReady(memoryResource, DataSource.MEMORY_CACHE);
    return null;
  }
```



 
 
 
 