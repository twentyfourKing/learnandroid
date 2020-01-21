LruResourceCache

构造方法
> 设置缓存可用的最大内存值

```java
    // LruResourceCache.java
    public LruResourceCache(long size) {
    super(size);
  }
  // LruCache.java
  public LruCache(long size) {
    this.initialMaxSize = size;
    this.maxSize = size;
  }
```


实例构建过程

在GlideBuilder 构建Engine的过程中
```java
    ...
     if (memoryCache == null) {
      memoryCache = new LruResourceCache(memorySizeCalculator.getMemoryCacheSize());
    }
    ...
```

memorySizeCalculator.getMemoryCacheSize() 计算出缓存可以使用的最大值

对内存等值的计算源于 MemorySizeCalculator
> MemorySizeCalculator 的创建也是在GlideBuilder 构建Engine的过程中

```java
    ...
    if (memorySizeCalculator == null) {
      memorySizeCalculator = new MemorySizeCalculator.Builder(context).build();
    }
    ...
```
通过 MemorySizeCalculator 计算出的缓存大小 
> 大小基本上是: 放置两张撑满全屏图片占用的内存大小(按照argb888的规则存放。即 每个像素占4个字节)




