
DataCacheGenerator
===

```java
    DataCacheGenerator(DecodeHelper<?> helper, FetcherReadyCallback cb) {
        this(helper.getCacheKeys(), helper, cb);
    }
```


构建 DataCacheGenerator 的实例时会去查询 key getCacheKeys()

与ResourceCacheGenerator 的查询结果一样:

key就是 model 的内容 （加载时候是String 那么这里key就是String，标识地址）


```java
    public boolean startNext() {
        while (modelLoaders == null || !hasNextModelLoader()) {
          sourceIdIndex++;
          if (sourceIdIndex >= cacheKeys.size()) {
            return false;
          }
    
          Key sourceId = cacheKeys.get(sourceIdIndex);
          // PMD.AvoidInstantiatingObjectsInLoops The loop iterates a limited number of times
          // and the actions it performs are much more expensive than a single allocation.
          @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
          Key originalKey = new DataCacheKey(sourceId, helper.getSignature());
          cacheFile = helper.getDiskCache().get(originalKey);
          if (cacheFile != null) {
            this.sourceKey = sourceId;
            modelLoaders = helper.getModelLoaders(cacheFile);
            modelLoaderIndex = 0;
          }
        }
    
        loadData = null;
        boolean started = false;
        while (!started && hasNextModelLoader()) {
          ModelLoader<File, ?> modelLoader = modelLoaders.get(modelLoaderIndex++);
          loadData =
              modelLoader.buildLoadData(
                  cacheFile, helper.getWidth(), helper.getHeight(), helper.getOptions());
          if (loadData != null && helper.hasLoadPath(loadData.fetcher.getDataClass())) {
            started = true;
            loadData.fetcher.loadData(helper.getPriority(), this);
          }
        }
        return started;
  }
```

startNext的逻辑大致就是：根据key信息从硬盘缓存中取出File，然后根据这个File找到对应的Loader，然后把这个文件缓存通过对应的解码器，转为成我们需要的资源。

