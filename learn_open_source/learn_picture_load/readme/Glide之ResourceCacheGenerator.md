
从构建说起

在执行DecodeJob.runWrapped()方式时根据状态和前置条件确定了，构建什么类型的
Generator

```java
    new ResourceCacheGenerator(decodeHelper, this);
```

> decodeHelper 是 

DecodeHelper<R> decodeHelper = new DecodeHelper<>();

这里R泛型 最终由 transcodeClass 的类型决定 （如：Drawable等）

DecodeHelper的作用很明显：辅助DecodeJob完成部分功能操作

DecodeHelper 的init(...)入参就是源于 DecodeJob.init(...)的参数

DecodeHelper 实现的功能其实就是DecodeJob想实现的功能，只是将逻辑放到了DecodeHelper中

**tips:**  将DecodeHelper 传入ResourceCacheGenerator 中说明，ResourceCacheGenerator中的逻辑实现需要DecodeJob来辅助完成

> this 是 

FetcherReadyCallback 回调实现 在DecodeJob 中

```java
    interface FetcherReadyCallback {

    void reschedule();

    void onDataFetcherReady(
        Key sourceKey,
        @Nullable Object data,
        DataFetcher<?> fetcher,
        DataSource dataSource,
        Key attemptedKey);

    void onDataFetcherFailed(
        Key attemptedKey, Exception e, DataFetcher<?> fetcher, DataSource dataSource);
  }
```

**ResourceCacheGenerator 的核心逻辑在startNext()方法**

```java
    public boolean startNext() {
        List<Key> sourceIds = helper.getCacheKeys();
        if (sourceIds.isEmpty()) {
          return false;
        }
        List<Class<?>> resourceClasses = helper.getRegisteredResourceClasses();
        if (resourceClasses.isEmpty()) {
          if (File.class.equals(helper.getTranscodeClass())) {
            return false;
          }
          throw new IllegalStateException(
              "Failed to find any load path from "
                  + helper.getModelClass()
                  + " to "
                  + helper.getTranscodeClass());
        }
        while (modelLoaders == null || !hasNextModelLoader()) {
          resourceClassIndex++;
          if (resourceClassIndex >= resourceClasses.size()) {
            sourceIdIndex++;
            if (sourceIdIndex >= sourceIds.size()) {
              return false;
            }
            resourceClassIndex = 0;
          }
    
          Key sourceId = sourceIds.get(sourceIdIndex);
          Class<?> resourceClass = resourceClasses.get(resourceClassIndex);
          Transformation<?> transformation = helper.getTransformation(resourceClass);
          currentKey =
              new ResourceCacheKey( // NOPMD AvoidInstantiatingObjectsInLoops
                  helper.getArrayPool(),
                  sourceId,
                  helper.getSignature(),
                  helper.getWidth(),
                  helper.getHeight(),
                  transformation,
                  resourceClass,
                  helper.getOptions());
          cacheFile = helper.getDiskCache().get(currentKey);
          if (cacheFile != null) {
            sourceKey = sourceId;
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
1) getCacheKeys()

过程简述：
model 就是 load(xx)中的参数类型,如加载load("http://xx.xx.jpg")，这个model就是String

先从Registry中注册的load类型查询出能够处理，String类型的load，
经过查询得出如下结论：

因为model是String，那么StringLoader类型就满足，从注册的类型：
```java
        .append(String.class, InputStream.class, new StringLoader.StreamFactory())
        .append(String.class, ParcelFileDescriptor.class, new StringLoader.FileDescriptorFactory())
        .append(String.class, AssetFileDescriptor.class, new StringLoader.AssetFileDescriptorFactory())
```
会有3种类型的loader。

从StringLoader的类结构也可以看出，它有3种类型factory。这3个factory也就意味处理String 这种类型的model，会有3种数据源，对应关系：

```text
    StreamFactory -> InputStream
    FileDescriptorFactory -> ParcelFileDescriptor
    AssetFileDescriptorFactory -> AssetFileDescriptor
```
在构建 StringLoader 实例时 ，又会根据3种不同数据数据源，查询出对应的 ModelLoader，

保存在 StringLoader 的 uriLoader 变量，依次执行 StringLoader.buildLoadData 真正的执行逻辑还是 根据 uriLoader的类型来执行对应的 buildLoadData方法

理论上会查询出3个 StringLoader，大致信息如下：

```text
    // StringLoader-1 的 uriLoader 信息如下：
    com.bumptech.glide.load.model.DataUrlLoader
    com.bumptech.glide.load.model.stream.HttpUriLoader
    com.bumptech.glide.load.model.AssetUriLoader
    com.bumptech.glide.load.model.stream.MediaStoreImageThumbLoader
    com.bumptech.glide.load.model.stream.MediaStoreVideoThumbLoader
    com.bumptech.glide.load.model.UriLoader
    com.bumptech.glide.load.model.UrlUriLoader

    // StringLoader-2 的 uriLoader 信息如下：
    com.bumptech.glide.load.model.AssetUriLoader
    com.bumptech.glide.load.model.UriLoader
    
    // StringLoader-3 的 uriLoader 信息如下：
    com.bumptech.glide.load.model.UriLoader
```

经过遍历逻辑最后得出loadData信息如下：

```text
    getLoadData():
    LoadData 只有一个：
    LoadData的信息如下：
    sourceKey = https://www.wanandroid.com/resources/image/pc/default_project_img.jpg
    fetcher = MultiModelLoader.MultiFetcher
    alternateKeys = 大小为0 
```

然后遍历获得的LoadData，并判断和保存sourceKey在 cacheKeys 

最后 getCacheKeys() 得到的 cacheKeys信息就只有一个：

String 标识的地址信息："http://xx.xx.jpg"


2) helper.getRegisteredResourceClasses()
```java
    List<Class<?>> getRegisteredResourceClasses() {
        return glideContext
            .getRegistry()
            .getRegisteredResourceClasses(model.getClass(), resourceClass, transcodeClass);
    }
```
> model 是 String

> resourceClass 是 Object

> transcodeClass 是 Drawable

 getRegisteredResourceClasses 大致逻辑：
 
 在Registry中根据 需要处理的 model，数据源和解码后的资源
查询出已经注册的资源类型。这里的资源类型就是，数据被解码后，可以使用
资源如（model为String，查询出的注册资源有：GifDrawable、Bitmap、BitmapDrawable）

resourceClasses 查询出的理论结果如下：
```text
     com.bumptech.glide.load.resource.gif.GifDrawable
     android.graphics.Bitmap
     android.graphics.drawable.BitmapDrawable
```

根据 resourceClasses的类型 可以得到Transformation的对应类型

Transformation<?> transformation = helper.getTransformation(resourceClass);
```java
    GifDrawable -> GifDrawableTransformation
    Bitmap -> FitCenter
    BitmapDrawable -> DrawableTransformation
```

后续逻辑 与 modelLoaders == null || !hasNextModelLoader() 的逻辑判断有关系

modelLoaders初始值是空的，而且modelLoaders 的赋值和大小与硬盘缓存有关系，初次使用是没有缓存的，所以这个是死循环的，不过这里的逻辑用 前面查询出的 resourceClasses 数量做了限制，超过了循环也就退出了。


3) 从硬盘获取缓存 [硬盘缓存]()

```java
      cacheFile = helper.getDiskCache().get(currentKey);
      if (cacheFile != null) {
        sourceKey = sourceId;
        modelLoaders = helper.getModelLoaders(cacheFile);
        modelLoaderIndex = 0;
      }
```
这个get方法要么从 DiskLruCacheWrapper 中获取，要么从 DiskCacheAdapter 中获取


ResourceCacheGenerator.startNext()的逻辑总结而言就是：

> 根据需要解码的数据源，查找存在的解码类，然后得到缓存中的key也就是可以处理的model的key，然后查出可以被解析成的资源格式，根据这个格式的数量作为依据查找硬盘缓存中时候存在缓存，如果不存在，那么startNext最后的返回结果就是false。说明在硬盘缓存中处理不了该model的









