SourceGenerator
===

```java
    SourceGenerator(DecodeHelper<?> helper, FetcherReadyCallback cb) {
        this.helper = helper;
        this.cb = cb;
    }
```


```java
    // SourceGenerator.java
    public boolean startNext() {
        if (dataToCache != null) {
          Object data = dataToCache;
          dataToCache = null;
          cacheData(data);
        }
    
        if (sourceCacheGenerator != null && sourceCacheGenerator.startNext()) {
          return true;
        }
        sourceCacheGenerator = null;
    
        loadData = null;
        boolean started = false;
        while (!started && hasNextModelLoader()) {
          loadData = helper.getLoadData().get(loadDataListIndex++);
          if (loadData != null
              && (helper.getDiskCacheStrategy().isDataCacheable(loadData.fetcher.getDataSource())
                  || helper.hasLoadPath(loadData.fetcher.getDataClass()))) {
            started = true;
            loadData.fetcher.loadData(helper.getPriority(), this);
          }
        }
        return started;
  }
```

1、判断缓存

2、用helper.getLoadData() 去获取LoadData

3) 遍历查询出的 LoadData

查询出的理论数据如下：
ModelLoader.LoadData
```text
    sourceKey = http://xx.xx.jpg
    fetcher = MultiModelLoader.MultiFetcher
    alternateKeys = 0
```

HttpUrlFetcher.loadData 从网络加载数据

加载完后回调 SourceGenerator.onDataReady

ByteBufferFileLoader.loadData 从已经加载好的文件中取出资源

当数据加载好之后回到到 DecodeJob.onDataFetcherReady




