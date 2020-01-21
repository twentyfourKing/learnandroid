从 Engine.load()开始
===

1) 将图的属性配置 构建为  EngineKey 也作为缓存数据时的 key

2) 加载逻辑

> 1 检索缓存数据

> 2 获取新鲜数据

 Engine.load()方法的完整代码如下：
 
  > transcodeClass 根据加载Drawable到ImageView的前提，这里 resourceClass 就是Drawable
  
  > resourceClass 这里的值需要根据options的具体配置来确定，
  > 默认是Object

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

**检索缓存数据**

a、不允许操作缓存 isMemoryCacheable 为false，不检索缓存

b、检索活跃区的资源 loadFromActiveResources() ，如存在 那么直接使用
```java
    private EngineResource<?> loadFromActiveResources(Key key) {
        EngineResource<?> active = activeResources.get(key);
        if (active != null) {
          active.acquire();
        }
    
        return active;
    }
```
c、检索缓存 loadFromCache()
> [检索缓存-具体逻辑流程分析]()

```java
    private EngineResource<?> loadFromCache(Key key) {
        EngineResource<?> cached = getEngineResourceFromCache(key);
        if (cached != null) {
          cached.acquire();
          activeResources.activate(key, cached);
        }
        return cached;
  }
```
当从缓存(活跃区和内存缓存)中检索出数据那么直接使用
```java
    cb.onResourceReady(memoryResource, DataSource.MEMORY_CACHE);
```
这里的cb 其实是 SingleRequest 它实现了 ResourceCallback 接口
> 处理逻辑进入 SingleRequest.onResourceReady


**获取新鲜数据**

> waitForExistingOrStartNewJob()

```java
    // Engine.java
    
    private <R> LoadStatus waitForExistingOrStartNewJob(
      ...省略参数) {

        EngineJob<?> current = jobs.get(key, onlyRetrieveFromCache);
        if (current != null) {
          current.addCallback(cb, callbackExecutor);
          if (VERBOSE_IS_LOGGABLE) {
            logWithTimeAndKey("Added to existing load", startTime, key);
          }
          return new LoadStatus(cb, current);
        }
    
        EngineJob<R> engineJob =
            engineJobFactory.build(
                key,
                isMemoryCacheable,
                useUnlimitedSourceExecutorPool,
                useAnimationPool,
                onlyRetrieveFromCache);
    
        DecodeJob<R> decodeJob =
            decodeJobFactory.build(
                glideContext,
                model,
                key,
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
                onlyRetrieveFromCache,
                options,
                engineJob);
    
        jobs.put(key, engineJob);
    
        engineJob.addCallback(cb, callbackExecutor);
        engineJob.start(decodeJob);
    
        if (VERBOSE_IS_LOGGABLE) {
          logWithTimeAndKey("Started new load", startTime, key);
        }
        return new LoadStatus(cb, engineJob);
  }
```

1) 查看是否存在相同的 EngineJob



```java
    EngineJob<?> current = jobs.get(key, onlyRetrieveFromCache);
    if (current != null) {
      current.addCallback(cb, callbackExecutor);
      if (VERBOSE_IS_LOGGABLE) {
        logWithTimeAndKey("Added to existing load", startTime, key);
      }
      return new LoadStatus(cb, current);
    }
```
jobs 就是 Jobs ，它是EngineJob的管理类
```java
    // Jobs.java
    EngineJob<?> get(Key key, boolean onlyRetrieveFromCache) {
        return getJobMap(onlyRetrieveFromCache).get(key);
  }
```
```java
    private Map<Key, EngineJob<?>> getJobMap(boolean onlyRetrieveFromCache) {
        return onlyRetrieveFromCache ? onlyCacheJobs : jobs;
  }
```
onlyRetrieveFromCache 决定了检索哪个数据结构对象 (默认是 true， 可以通过option设置)
onlyCacheJobs jobs都是HashMap
```java
    private final Map<Key, EngineJob<?>> jobs = new HashMap<>();
    private final Map<Key, EngineJob<?>> onlyCacheJobs = new HashMap<>();
```

2) 存在缓存的 EngineJob

current.addCallback(cb, callbackExecutor);

```java
    // EngineJob.java
    synchronized void addCallback(final ResourceCallback cb, Executor callbackExecutor) {
        stateVerifier.throwIfRecycled();
        cbs.add(cb, callbackExecutor);
        if (hasResource) {
          // Acquire early so that the resource isn't recycled while the Runnable below is still sitting
          // in the executors queue.
          incrementPendingCallbacks(1);
          callbackExecutor.execute(new CallResourceReady(cb));
        } else if (hasLoadFailed) {
          incrementPendingCallbacks(1);
          callbackExecutor.execute(new CallLoadFailed(cb));
        } else {
          Preconditions.checkArgument(!isCancelled, "Cannot add callbacks to a cancelled EngineJob");
        }
  }
```

3) 不存在缓存

构建新的 EngineJob 和  DecodeJob

构建完成后缓存EngineJob
```java
    jobs.put(key, engineJob);

    engineJob.addCallback(cb, callbackExecutor);
```

4) 启动
```java
    engineJob.start(decodeJob);
```

```java
    // EngineJob.java
    public synchronized void start(DecodeJob<R> decodeJob) {
        this.decodeJob = decodeJob;
        GlideExecutor executor =
            decodeJob.willDecodeFromCache() ? diskCacheExecutor : getActiveSourceExecutor();
        executor.execute(decodeJob);
  }
```
获取线程池

```java
    GlideExecutor executor =
        decodeJob.willDecodeFromCache() ? diskCacheExecutor : getActiveSourceExecutor();
```
```java
    boolean willDecodeFromCache() {
        Stage firstStage = getNextStage(Stage.INITIALIZE);
        return firstStage == Stage.RESOURCE_CACHE || firstStage == Stage.DATA_CACHE;
    }
```

[根据DecoeJob中的分析]() getNextStage(Stage.INITIALIZE); 就会返回 true，那么

 GlideExecutor executor  就是 diskCacheExecutor
 根据构建流程可以知道如下：
```java
   diskCacheExecutor =  GlideExecutor.newDiskCacheExecutor();
```

DEFAULT_DISK_CACHE_EXECUTOR_THREADS 是 1 

就是构建了只有一个线程数的线程池
```java
    public static GlideExecutor newDiskCacheExecutor() {
        return newDiskCacheExecutor(
            DEFAULT_DISK_CACHE_EXECUTOR_THREADS,
            DEFAULT_DISK_CACHE_EXECUTOR_NAME,
            UncaughtThrowableStrategy.DEFAULT);
  }
```

确定了Executor 后 ，回到 executor.execute(decodeJob);

就是执行 DecodeJob.run [进入DecodeJob分析]()



