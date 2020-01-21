

as(Class)

资源需要被解码成什么什么类型的数据（使用已经注册的资源解码工具类）

> 方法最后构建一个新的RequestBuilder实例

```java
    // RequestManager.java
    public <ResourceType> RequestBuilder<ResourceType> as(
      @NonNull Class<ResourceType> resourceClass) {
        return new RequestBuilder<>(glide, this, resourceClass, context);
    }
```

资源可以被解码成的类型有4类：

Gif、Drawable、File、Bitmap

下面是具体的定义逻辑：
> RequestManager 的方法

asGif()
```java
    public RequestBuilder<GifDrawable> asGif() {
        return as(GifDrawable.class).apply(DECODE_TYPE_GIF);
    }
```

asDrawable()
```java
    public RequestBuilder<Drawable> asDrawable() {
        return as(Drawable.class);
    }
```

asFile()
```java
    public RequestBuilder<File> asFile() {
        return as(File.class).apply(skipMemoryCacheOf(true));
    }
```
asBitmap()
```java
    public RequestBuilder<Bitmap> asBitmap() {
        return as(Bitmap.class).apply(DECODE_TYPE_BITMAP);
    }
```

在常规的使用中，load方法存在于RequestBuilder类中也存在于RequestManager类中。
其中RequestManager.load最后还是会调到RequestBuilder的load方法

```java
    // RequestManager.java
    public RequestBuilder<Drawable> load(@Nullable Bitmap bitmap) {
        return asDrawable().load(bitmap);
    }
```
```java
    // RequestManager.java
    public RequestBuilder<Drawable> load(@Nullable byte[] model) {
        return asDrawable().load(model);
    }
```
```java
    // RequestManager.java
    public RequestBuilder<Drawable> load(@Nullable Drawable drawable) {
        return asDrawable().load(drawable);
    }
```
```java
    // RequestManager.java
    public RequestBuilder<Drawable> load(@Nullable File file) {
        return asDrawable().load(file);
    }
```
```java
    // RequestManager.java
    public RequestBuilder<Drawable> load(@RawRes @DrawableRes @Nullable Integer resourceId) {
        return asDrawable().load(resourceId);
    }
```
```java
    // RequestManager.java
    public RequestBuilder<Drawable> load(@Nullable Object model) {
        return asDrawable().load(model);
    }
```
```java
    // RequestManager.java
    public RequestBuilder<Drawable> load(@Nullable String string) {
        return asDrawable().load(string);
    }
```
```java
    // RequestManager.java
    public RequestBuilder<Drawable> load(@Nullable Uri uri) {
        return asDrawable().load(uri);
    }
```
```java
    // RequestManager.java
    public RequestBuilder<Drawable> load(@Nullable URL url) {
        return asDrawable().load(url);
    }
```
> RequestBuilder.load

```java
    //将资源解码成 File 
    public RequestBuilder<TranscodeType> load(@Nullable File file) {
        return loadGeneric(file);
    }
```
loadGeneric 设置 model
```java
    private RequestBuilder<TranscodeType> loadGeneric(@Nullable Object model) {
        this.model = model;
        isModelSet = true;
        return this;
    }
```

