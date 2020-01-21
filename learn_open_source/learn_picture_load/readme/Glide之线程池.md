GlideExecutor.java

> implements ExecutorService

加载动画的线程池，可以提供的线程数由设备可以提供的内核数决定

核心数大于等于4，那么线程数时，否则就是1

```java
    newAnimationExecutor()

    newAnimationExecutor(
          int threadCount, 
          UncaughtThrowableStrategy uncaughtThrowableStrategy) 
```
硬件加载线程池，默认是1个线程，但是可以自己灵活设置  

tips：不允许网络操作在这个线程池
      
```java
    newDiskCacheExecutor()

    newDiskCacheExecutor(
          int threadCount, 
          String name, 
          UncaughtThrowableStrategy uncaughtThrowableStrategy) 
```

加载资源的线程池，线程数量由设备的可用内核数来进行设置

可以自定义线程数和线程名

允许网络操作在这个线程池
      
```java
    newSourceExecutor()

    newSourceExecutor(
          int threadCount, 
          String name, 
          UncaughtThrowableStrategy uncaughtThrowableStrategy)
          
    newSourceExecutor(
          UncaughtThrowableStrategy uncaughtThrowableStrategy)
```
没有限制的线程池

tips:允许执行网络任务

[参考](http://developer.android.com/reference/java/util/concurrent/ThreadPoolExecutor.html)

```java
    newUnlimitedSourceExecutor()
```

上面几种类型的线程池都是使用 ThreadPoolExecutor 的构造方法,
具体的参数设置又不尽相同

```java
    ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
             threadFactory, defaultHandler);
```

线程池在Glide中初始化构建是在 GlideBuilder.build方法中

      
