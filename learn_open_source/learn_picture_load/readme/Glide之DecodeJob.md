

**getNextStage**

```java
    private Stage getNextStage(Stage current) {
        switch (current) {
          case INITIALIZE:
            return diskCacheStrategy.decodeCachedResource()
                ? Stage.RESOURCE_CACHE
                : getNextStage(Stage.RESOURCE_CACHE);
          case RESOURCE_CACHE:
            return diskCacheStrategy.decodeCachedData()
                ? Stage.DATA_CACHE
                : getNextStage(Stage.DATA_CACHE);
          case DATA_CACHE:
            // Skip loading from source if the user opted to only retrieve the resource from cache.
            return onlyRetrieveFromCache ? Stage.FINISHED : Stage.SOURCE;
          case SOURCE:
          case FINISHED:
            return Stage.FINISHED;
          default:
            throw new IllegalArgumentException("Unrecognized stage: " + current);
        }
  }
```
根据当前状态获取下一个状态

diskCacheStrategy 是构建DecodeJob时的入参，这个参数最终来源于 
BaseRequestOptions。默认值是如下，不过可以通过Options设置
```java
    private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;
```
```java
    public static final DiskCacheStrategy AUTOMATIC =
      new DiskCacheStrategy() {
        @Override
        public boolean isDataCacheable(DataSource dataSource) {
          return dataSource == DataSource.REMOTE;
        }

        @Override
        public boolean isResourceCacheable(
            boolean isFromAlternateCacheKey, DataSource dataSource, EncodeStrategy encodeStrategy) {
          return ((isFromAlternateCacheKey && dataSource == DataSource.DATA_DISK_CACHE)
                  || dataSource == DataSource.LOCAL)
              && encodeStrategy == EncodeStrategy.TRANSFORMED;
        }

        @Override
        public boolean decodeCachedResource() {
          return true;
        }

        @Override
        public boolean decodeCachedData() {
          return true;
        }
      };
```

在默认情况下 diskCacheStrategy.decodeCachedResource() 返回的就是 true

**run()**

在资源加载流程中，确定了Executor后，开始执行 executor.execute(decodeJob);

就是进入了 DecodeJob.run()

```java
    public void run() {
        
        GlideTrace.beginSectionFormat("DecodeJob#run(model=%s)", model);
        
        DataFetcher<?> localFetcher = currentFetcher;
        try {
          if (isCancelled) {
            notifyFailed();
            return;
          }
          runWrapped();
        } catch (CallbackException e) {
          
          throw e;
        } catch (Throwable t) {
          
          if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(
                TAG,
                "DecodeJob threw unexpectedly" + ", isCancelled: " + isCancelled + ", stage: " + stage,
                t);
          }
          // When we're encoding we've already notified our callback and it isn't safe to do so again.
          if (stage != Stage.ENCODE) {
            throwables.add(t);
            notifyFailed();
          }
          if (!isCancelled) {
            throw t;
          }
          throw t;
        } finally {
          if (localFetcher != null) {
            localFetcher.cleanup();
          }
          GlideTrace.endSection();
        }
  }
```

1) 是否被取消 
```java
    if (isCancelled) {
        notifyFailed();
        return;
    }
```
如果被取消,执行 失败回调

```java
    private void notifyFailed() {
        setNotifiedOrThrow();
        GlideException e = new GlideException("Failed to load resource", new ArrayList<>(throwables));
        callback.onLoadFailed(e);
        onLoadFailed();
  }
```
2) 没有取消

执行 runWrapped();

```java
    private void runWrapped() {
        switch (runReason) {
          case INITIALIZE:
            stage = getNextStage(Stage.INITIALIZE);
            currentGenerator = getNextGenerator();
            runGenerators();
            break;
          case SWITCH_TO_SOURCE_SERVICE:
            runGenerators();
            break;
          case DECODE_DATA:
            decodeFromRetrievedData();
            break;
          default:
            throw new IllegalStateException("Unrecognized run reason: " + runReason);
        }
  }
```
> getNextStage 的逻辑原理前面已经说明了 

runReason 的值就是逻辑流转的关键了
> DecodeJob被构建时 runReason的值被初始化为 this.runReason = RunReason.INITIALIZE;

> 当然runReason 这个值除了初始化，是会被动态修改的。如下

```java
    public void reschedule() {
        runReason = RunReason.SWITCH_TO_SOURCE_SERVICE;
        callback.reschedule(this);
  }
```
```java
    public void onDataFetcherReady(){
        ...
        if (Thread.currentThread() != currentThread) {
        runReason = RunReason.DECODE_DATA;
        callback.reschedule(this);
        } else {
          GlideTrace.beginSection("DecodeJob.decodeFromRetrievedData");
          try {
            decodeFromRetrievedData();
          } finally {
            GlideTrace.endSection();
          }
        }
    }
    
    
    public void onDataFetcherFailed(xx) {
        fetcher.cleanup();
        ...
        if (Thread.currentThread() != currentThread) {
          runReason = RunReason.SWITCH_TO_SOURCE_SERVICE;
          callback.reschedule(this);
        } else {
          runGenerators();
        }
     }
```
当初次执行runWrapped() runReason 的值应该是 RunReason.INITIALIZE

```java
    stage = getNextStage(Stage.INITIALIZE);
    currentGenerator = getNextGenerator();
    runGenerators();
```
stage 值修改为  Stage.RESOURCE_CACHE
```java
    private DataFetcherGenerator getNextGenerator() {
        switch (stage) {
          case RESOURCE_CACHE:
            return new ResourceCacheGenerator(decodeHelper, this);
          case DATA_CACHE:
            return new DataCacheGenerator(decodeHelper, this);
          case SOURCE:
            return new SourceGenerator(decodeHelper, this);
          case FINISHED:
            return null;
          default:
            throw new IllegalStateException("Unrecognized stage: " + stage);
        }
  }
```
currentGenerator 的值就是 new ResourceCacheGenerator(decodeHelper, this);

继续进入 runGenerators();

```java
    private void runGenerators() {
        currentThread = Thread.currentThread();
        startFetchTime = LogTime.getLogTime();
        boolean isStarted = false;
        while (!isCancelled
            && currentGenerator != null
            && !(isStarted = currentGenerator.startNext())) {
          stage = getNextStage(stage);
          currentGenerator = getNextGenerator();
    
          if (stage == Stage.SOURCE) {
            reschedule();
            return;
          }
        }
        // We've run out of stages and generators, give up.
        if ((stage == Stage.FINISHED || isCancelled) && !isStarted) {
          notifyFailed();
        }
  }
```
isCancelled的值默认是false 除非主动调用了 cancel()

进入 currentGenerator.startNext()
> 即 ResourceCacheGenerator.startNext

ResourceCacheGenerator.startNext的逻辑在 [ResourceCacheGenerator]()

我们知道如果是首次加载资源，那么是不会存在缓存的（不论是内存还是硬盘）

所以
```java
     stage = getNextStage(stage);
     currentGenerator = getNextGenerator();
```

Stage.RESOURCE_CACHE  的下个状态就是 Stage.DATA_CACHE

执行 getNextStage后 stage为 Stage.DATA_CACHE

然后执行getNextGenerator() 得到 DataCacheGenerator(decodeHelper, this);

**继续DataCacheGenerator.startNext()**

同理进入[DataCacheGenerator]()





加载回调：

DecodeJob.onDataFetcherReady


最后回调到 SingleRequest.onResourceReady
```java
    private void onResourceReady(Resource<R> resource, R result, DataSource dataSource) {
    ..
    try {
      ...
      if (!anyListenerHandledUpdatingTarget) {
        Transition<? super R> animation = animationFactory.build(dataSource, isFirstResource);
        target.onResourceReady(result, animation);
      }
    } finally {
      isCallingCallbacks = false;
    }

    notifyLoadSuccess();
  }
```
调用具体的 target 的 onResourceReady方法

将图片设置给对应的 View
比如：
```java
    public class DrawableImageViewTarget extends ImageViewTarget<Drawable> {

      public DrawableImageViewTarget(ImageView view) {
        super(view);
      }
    
      /** @deprecated Use {@link #waitForLayout()} instead. */
      // Public API.
      @SuppressWarnings({"unused", "deprecation"})
      @Deprecated
      public DrawableImageViewTarget(ImageView view, boolean waitForLayout) {
        super(view, waitForLayout);
      }
    
      @Override
      protected void setResource(@Nullable Drawable resource) {
        view.setImageDrawable(resource);
      }
}
```






