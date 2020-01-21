


**DecodeHelper 从硬盘获取缓存**

```java
    DiskCache getDiskCache() {
        return diskCacheProvider.getDiskCache();
    }
```
1) DecodeJob.DiskCacheProvider diskCacheProvider 就是 LazyDiskCacheProvider 
构建于 Engine的构造方法


```java
    private static class LazyDiskCacheProvider implements DecodeJob.DiskCacheProvider {

        private final DiskCache.Factory factory;
        private volatile DiskCache diskCache;
    
        LazyDiskCacheProvider(DiskCache.Factory factory) {
          this.factory = factory;
        }
    
        @VisibleForTesting
        synchronized void clearDiskCacheIfCreated() {
          if (diskCache == null) {
            return;
          }
          diskCache.clear();
        }
    
        @Override
        public DiskCache getDiskCache() {
          if (diskCache == null) {
            synchronized (this) {
              if (diskCache == null) {
                diskCache = factory.build();
              }
              if (diskCache == null) {
                diskCache = new DiskCacheAdapter();
              }
            }
          }
          return diskCache;
        }
  }
```

2) 回到  diskCacheProvider.getDiskCache();

实际执行的是 LazyDiskCacheProvider.getDiskCache() 方法

这里的diskCacheFactory 就是 InternalCacheDiskCacheFactory 
 > 构建于GlideBuilder.java

```java
    public final class InternalCacheDiskCacheFactory extends DiskLruCacheFactory {

      public InternalCacheDiskCacheFactory(Context context) {
        this(
            context,
            DiskCache.Factory.DEFAULT_DISK_CACHE_DIR,
            DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
      }
    
      public InternalCacheDiskCacheFactory(Context context, long diskCacheSize) {
        this(context, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR, diskCacheSize);
      }
    
      public InternalCacheDiskCacheFactory(
          final Context context, final String diskCacheName, long diskCacheSize) {
        super(
            new CacheDirectoryGetter() {
              @Override
              public File getCacheDirectory() {
                File cacheDirectory = context.getCacheDir();
                if (cacheDirectory == null) {
                  return null;
                }
                if (diskCacheName != null) {
                  return new File(cacheDirectory, diskCacheName);
                }
                return cacheDirectory;
              }
            },
            diskCacheSize);
      }
}
```
**diskCache = factory.build();**

查询 InternalCacheDiskCacheFactory 的build，其实就是查询 DiskLruCacheFactory
```java
    ublic DiskCache build() {
        File cacheDir = cacheDirectoryGetter.getCacheDirectory();
    
        if (cacheDir == null) {
          return null;
        }
    
        if (!cacheDir.mkdirs() && (!cacheDir.exists() || !cacheDir.isDirectory())) {
          return null;
        }
    
        return DiskLruCacheWrapper.create(cacheDir, diskCacheSize);
  }
```
大致逻辑流程就是：先获取应用程序默的认缓存文件。如果为空那么返回空；不为空在应用程序默认的缓存文件夹下构建新的子文件（取名：image_manager_disk_cache）
。如果能得到系统的缓存文件或者能够创建新的子文件，那么用 DiskLruCacheWrapper 封装

总结就是：正常返回的就是 DiskLruCacheWrapper 封装的文件缓存


**diskCache = new DiskCacheAdapter();**

DiskLruCacheWrapper为空那么用 DiskCacheAdapter

在得到缓存实例后，后面根据key获取具体的缓存内容，在具体的get方法中

硬件缓存具体逻辑跟进 DiskLruCacheWrapper




