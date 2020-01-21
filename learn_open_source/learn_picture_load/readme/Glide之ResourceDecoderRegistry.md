
数据解码注册管理类
> 可以把任意类型的数据源解码成任意类型的资源


**getDecoders**
根据数据类型和数据解码后的形态 查询对用的解码工具类
```java
    public synchronized <T, R> List<ResourceDecoder<T, R>> getDecoders(
      @NonNull Class<T> dataClass, @NonNull Class<R> resourceClass) {
        List<ResourceDecoder<T, R>> result = new ArrayList<>();
        for (String bucket : bucketPriorityList) {
          List<Entry<?, ?>> entries = decoders.get(bucket);
          if (entries == null) {
            continue;
          }
          for (Entry<?, ?> entry : entries) {
            if (entry.handles(dataClass, resourceClass)) {
              result.add((ResourceDecoder<T, R>) entry.decoder);
            }
          }
        }
        // TODO: cache result list.
    
        return result;
  }
```
**getResourceClasses**

根据数据类型和解码后的资源，查询注册缓存中对应的解码资源类型
```java
    public synchronized <T, R> List<Class<R>> getResourceClasses(
      @NonNull Class<T> dataClass, @NonNull Class<R> resourceClass) {
        List<Class<R>> result = new ArrayList<>();
        for (String bucket : bucketPriorityList) {
          List<Entry<?, ?>> entries = decoders.get(bucket);
          if (entries == null) {
            continue;
          }
          for (Entry<?, ?> entry : entries) {
            if (entry.handles(dataClass, resourceClass)
                && !result.contains((Class<R>) entry.resourceClass)) {
              result.add((Class<R>) entry.resourceClass);
            }
          }
        }
        return result;
  }
```
**append**

注册保存
```java
    public synchronized <T, R> void append(
      @NonNull String bucket,
      @NonNull ResourceDecoder<T, R> decoder,
      @NonNull Class<T> dataClass,
      @NonNull Class<R> resourceClass) {
        getOrAddEntryList(bucket).add(new Entry<>(dataClass, resourceClass, decoder));
  }
```
实际操作如在Glide中注册的:
```java
    .append(Registry.BUCKET_BITMAP, Bitmap.class, Bitmap.class, new UnitBitmapDecoder())

    .append(
        Registry.BUCKET_BITMAP_DRAWABLE,
        InputStream.class,
        BitmapDrawable.class,
        new BitmapDrawableDecoder<>(resources, streamBitmapDecoder))
        
    .append(Registry.BUCKET_GIF, ByteBuffer.class, GifDrawable.class, byteBufferGifDecoder)
```
上面的操作就是注册了3种类型，他们的对应关系如下：
|bucket|dataClass|resourceClass|ResourceDecoder|
|------|---------|-------------|---------------|
|Bitmap|Bitmap|Bitmap|UnitBitmapDecoder|
|BitmapDrawable|InputStream|BitmapDrawable|BitmapDrawableDecoder|
|Gif|ByteBuffer|GifDrawable|byteBufferGifDecoder|

bucket 是 ResourceDecoderRegistry 中数据分组的标签
> 用逻辑阐述这种匹配关系，比如：把数据源为 InputStream 数据，使用BitmapDrawableDecoder
这个解码类，转换为BitmapDrawable



**内部类Entry**

> 该类用于保存 对应的  数据类型、解码后的资源类型、解码工具类

```java
    private static class Entry<T, R> {
        private final Class<T> dataClass;
        @Synthetic final Class<R> resourceClass;
        @Synthetic final ResourceDecoder<T, R> decoder;
    
        public Entry(
            @NonNull Class<T> dataClass,
            @NonNull Class<R> resourceClass,
            ResourceDecoder<T, R> decoder) {
          this.dataClass = dataClass;
          this.resourceClass = resourceClass;
          this.decoder = decoder;
        }
    
        public boolean handles(@NonNull Class<?> dataClass, @NonNull Class<?> resourceClass) {
          return this.dataClass.isAssignableFrom(dataClass)
              && resourceClass.isAssignableFrom(this.resourceClass);
        }
  }
```