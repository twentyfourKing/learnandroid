MemorySizeCalculator

计算相关值

构造方法
```java
    MemorySizeCalculator(MemorySizeCalculator.Builder builder) {
        this.context = builder.context;
    
        arrayPoolSize =
            isLowMemoryDevice(builder.activityManager)
                ? builder.arrayPoolSizeBytes / LOW_MEMORY_BYTE_ARRAY_POOL_DIVISOR
                : builder.arrayPoolSizeBytes;
        int maxSize =
            getMaxSize(
                builder.activityManager, builder.maxSizeMultiplier, builder.lowMemoryMaxSizeMultiplier);
    
        int widthPixels = builder.screenDimensions.getWidthPixels();
        int heightPixels = builder.screenDimensions.getHeightPixels();
        int screenSize = widthPixels * heightPixels * BYTES_PER_ARGB_8888_PIXEL;
    
        int targetBitmapPoolSize = Math.round(screenSize * builder.bitmapPoolScreens);
    
        int targetMemoryCacheSize = Math.round(screenSize * builder.memoryCacheScreens);
        int availableSize = maxSize - arrayPoolSize;
    
        if (targetMemoryCacheSize + targetBitmapPoolSize <= availableSize) {
          memoryCacheSize = targetMemoryCacheSize;
          bitmapPoolSize = targetBitmapPoolSize;
        } else {
          float part = availableSize / (builder.bitmapPoolScreens + builder.memoryCacheScreens);
          memoryCacheSize = Math.round(part * builder.memoryCacheScreens);
          bitmapPoolSize = Math.round(part * builder.bitmapPoolScreens);
        }
    
        if (Log.isLoggable(TAG, Log.DEBUG)) {
          Log.d(
              TAG,
              "Calculation complete"
                  + ", Calculated memory cache size: "
                  + toMb(memoryCacheSize)
                  + ", pool size: "
                  + toMb(bitmapPoolSize)
                  + ", byte array size: "
                  + toMb(arrayPoolSize)
                  + ", memory class limited? "
                  + (targetMemoryCacheSize + targetBitmapPoolSize > maxSize)
                  + ", max size: "
                  + toMb(maxSize)
                  + ", memoryClass: "
                  + builder.activityManager.getMemoryClass()
                  + ", isLowMemoryDevice: "
                  + isLowMemoryDevice(builder.activityManager));
        }
  }
```

**isLowMemoryDevice() 判断设备是否是低内存**

在19版本以上用方法判断，19以下直接就判定为低内存(本来在低版本上内存空间就小)
```java
    static boolean isLowMemoryDevice(ActivityManager activityManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
          return activityManager.isLowRamDevice();
        } else {
          return true;
        }
  }
```

MemorySizeCalculator.Builder.arrayPoolSizeBytes 默认大小是4M

那么 arrayPoolSize 的值不言而喻 低内存时大小是 2M ， 内存合适就是4M
```java
    arrayPoolSize =
        isLowMemoryDevice(builder.activityManager)
            ? builder.arrayPoolSizeBytes / LOW_MEMORY_BYTE_ARRAY_POOL_DIVISOR
            : builder.arrayPoolSizeBytes;
```

**最大内存**

```java
    int maxSize =
        getMaxSize(
            builder.activityManager, builder.maxSizeMultiplier, builder.lowMemoryMaxSizeMultiplier);
```
这里的最大内存是基于 系统配置的可用内存基础上根据使用比例计算的
> 一般系统设置的可用内存是16M，性能稍微好的是24M或者更高

根据是否是低内存设备，决定使用怎样的使用比例来使用内存 
> 低内存设备： 计算因子是 0.33f ; 高内存设备：计算因子是 0.4f

```java
    private static int getMaxSize(
      ActivityManager activityManager, float maxSizeMultiplier, float lowMemoryMaxSizeMultiplier) {
        final int memoryClassBytes = activityManager.getMemoryClass() * 1024 * 1024;
        final boolean isLowMemoryDevice = isLowMemoryDevice(activityManager);
        return Math.round(
            memoryClassBytes * (isLowMemoryDevice ? lowMemoryMaxSizeMultiplier : maxSizeMultiplier));
  }
```
**获取屏幕尺寸**

```java
    int widthPixels = builder.screenDimensions.getWidthPixels();
    int heightPixels = builder.screenDimensions.getHeightPixels();
```
默认使用argb8888（每个像素 占4字节）

那么根据屏幕的尺寸可以计算出，一个撑满屏幕的图片，用argb888模式存放，会占到的内存大小
```java
    int screenSize = widthPixels * heightPixels * BYTES_PER_ARGB_8888_PIXEL;
```

**根据系统版本进行计算bitmap缓存池的内存大小**
> 版本小于26 ，默认使用 图片缓存池大小是 4 ，那么计算出的内存大小就是缓存池的内存大小

> 版本大于26 ，默认是 1  ，这是因为在27以上Bitmap的设置有 HARDWARE

```java
    int targetBitmapPoolSize = Math.round(screenSize * builder.bitmapPoolScreens);
    
    bitmapPoolScreens = BITMAP_POOL_TARGET_SCREENS;
    
    // BITMAP_POOL_TARGET_SCREENS 值的计算逻辑
    
    static final int BITMAP_POOL_TARGET_SCREENS =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.O ? 4 : 1;
```

**缓存大小计算**
> 默认是按照 screenSize大小的2倍来计算

```java
    int targetMemoryCacheSize = Math.round(screenSize * builder.memoryCacheScreens);
```
**可用内存大小计算**
> 系统提供的最大内存 - arrayPoolSize

```java
    int availableSize = maxSize - arrayPoolSize;
```

**计算缓存大小和图片池内存大小的合理性**
> 设置的值如果小于 可以内存，那么使用。否则重新计算大小

```java
    if (targetMemoryCacheSize + targetBitmapPoolSize <= availableSize) {
          memoryCacheSize = targetMemoryCacheSize;
          bitmapPoolSize = targetBitmapPoolSize;
    } else {
          float part = availableSize / (builder.bitmapPoolScreens + builder.memoryCacheScreens);
          memoryCacheSize = Math.round(part * builder.memoryCacheScreens);
          bitmapPoolSize = Math.round(part * builder.bitmapPoolScreens);
    }
```


