DecodeJobFactory

其实是Engine的内部类

代码作用很纯粹

```java
    //Engine.java
    static class DecodeJobFactory {
        @Synthetic final DecodeJob.DiskCacheProvider diskCacheProvider;
    
        @Synthetic
        final Pools.Pool<DecodeJob<?>> pool =
            FactoryPools.threadSafe(
                JOB_POOL_SIZE,
                new FactoryPools.Factory<DecodeJob<?>>() {
                  @Override
                  public DecodeJob<?> create() {
                    return new DecodeJob<>(diskCacheProvider, pool);
                  }
                });
    
        private int creationOrder;
    
        DecodeJobFactory(DecodeJob.DiskCacheProvider diskCacheProvider) {
          this.diskCacheProvider = diskCacheProvider;
        }
    
        @SuppressWarnings("unchecked")
        <R> DecodeJob<R> build(
            GlideContext glideContext,
            Object model,
            EngineKey loadKey,
            Key signature,
            int width,
            int height,
            Class<?> resourceClass,
            Class<R> transcodeClass,
            Priority priority,
            DiskCacheStrategy diskCacheStrategy,
            Map<Class<?>, Transformation<?>> transformations,
            boolean isTransformationRequired,
            boolean isScaleOnlyOrNoTransform,
            boolean onlyRetrieveFromCache,
            Options options,
            DecodeJob.Callback<R> callback) {
          DecodeJob<R> result = Preconditions.checkNotNull((DecodeJob<R>) pool.acquire());
          return result.init(
              glideContext,
              model,
              loadKey,
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
              callback,
              creationOrder++);
        }
  }
```

1) DecodeJobFactory 构建实例 
```java
    DecodeJobFactory(DecodeJob.DiskCacheProvider diskCacheProvider) {
        this.diskCacheProvider = diskCacheProvider;
    }
```

构建于 Engine的构造方法
```java
    if (decodeJobFactory == null) {
        decodeJobFactory = new DecodeJobFactory(diskCacheProvider);
    }
```
diskCacheProvider 
```java
    this.diskCacheProvider = new LazyDiskCacheProvider(diskCacheFactory);
```

diskCacheFactory
```java
    // GlideBuilder.java
    if (diskCacheFactory == null) {
        diskCacheFactory = new InternalCacheDiskCacheFactory(context);
    }
```
2) 用DecodeFactory.build() 构建DecodeJob实例

```java
    <R> DecodeJob<R> build(
        ...省略) {
        
          DecodeJob<R> result = Preconditions.checkNotNull((DecodeJob<R>) pool.acquire());
          
          
          return result.init();
             
    }
```

pool 的构建在 变量赋值阶段。这里涉及到 Pools 这个类
这类的作用是构建和操作对象池。

pool值的设置 是通过 FactoryPools.threadSafe(size , factory)
> size 是设置对象池的数量

> factory 是通过create方法的实现来构建 DecodeJob实例

FactoryPools.threadSafe的根本逻辑设置对象池数量，并创建 Factory的匿名实例，最后创建出 FactoryPool(FactoryPool 是 Pool的实现)。

当pools执行acquire()方式时，其实就是进入 FactoryPool.acquire()
如下逻辑：

```java
     public T acquire() {
          T result = pool.acquire();
          if (result == null) {
            result = factory.create();
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
              Log.v(TAG, "Created new " + result.getClass());
            }
          }
          if (result instanceof Poolable) {
            ((Poolable) result).getVerifier().setRecycled(false /*isRecycled*/);
          }
          return result;
    }
```
然而这个 acquire() 中 pool其实是 SynchronizedPool，那么执行 pool.acquire();

就进入
```java
    // SynchronizedPool.java
    public T acquire() {
        synchronized (mLock) {
            return super.acquire();
        }
    }
```
super.acquire()

取出对象池中最后一个实例，并返回
```java
    public T acquire() {
        if (mPoolSize > 0) {
            final int lastPooledIndex = mPoolSize - 1;
            T instance = (T) mPool[lastPooledIndex];
            mPool[lastPooledIndex] = null;
            mPoolSize--;
            return instance;
        }
        return null;
    }
```
回到  FactoryPool.acquire() 在取出的对象实例不为空时，直接返回使用。
否则 重新构建
```java
    result = factory.create();
```
factory的匿名实现类如下：
```java
    new FactoryPools.Factory<DecodeJob<?>>() {
                  @Override
                  public DecodeJob<?> create() {
                    return new DecodeJob<>(diskCacheProvider, pool);
                  }
                })
```
绕了一大圈，终于将DecodeJob的实例构建出来了。

从DecodeJob的构造方法


