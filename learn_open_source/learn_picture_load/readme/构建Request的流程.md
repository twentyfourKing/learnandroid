构建Request的流程
===

从 **RequestBuilder.into()** 的下面逻辑说起
```java
    Request request = buildRequest(target, targetListener, options, callbackExecutor);
```


target 就是图片使用的对象 如 ImageView

targetListener RequestListener回调

```java
    public interface RequestListener<R> {
        boolean onLoadFailed(@Nullable GlideException e, Object model, Target<R> target, boolean isFirstResource);
        
        boolean onResourceReady(
          R resource, Object model, Target<R> target, DataSource dataSource, boolean isFirstResource);
    }
```
options 配置设置

callbackExecutor 切换线程的Executor(切换到主线程)

```java
    Request request = buildRequest(target, targetListener, options, callbackExecutor);
```
将配置项细化，如果没有特别定义就使用默认的配置
```java
    private Request buildRequest(
      Target<TranscodeType> target,
      @Nullable RequestListener<TranscodeType> targetListener,
      BaseRequestOptions<?> requestOptions,
      Executor callbackExecutor) {
        return buildRequestRecursive(
            /*requestLock=*/ new Object(),
            target,
            targetListener,
            /*parentCoordinator=*/ null,
            transitionOptions,
            requestOptions.getPriority(),
            requestOptions.getOverrideWidth(),
            requestOptions.getOverrideHeight(),
            requestOptions,
            callbackExecutor);
  }
```
new Object() 请求对象锁 requestLock 

构建请求

**构建request 有3种情况**

```java
    private Request buildThumbnailRequestRecursive(){
        if (thumbnailBuilder != null){
            ...
        }else if(thumbSizeMultiplier != null){
            ...
        }else{
            ...
        }
    }
    
```

第一种情况 

通过 thumbnail设置了缩略图请求的

```java
    // RequestBuilder.java
    public RequestBuilder<TranscodeType> thumbnail(
      @Nullable RequestBuilder<TranscodeType> thumbnailRequest) {
        this.thumbnailBuilder = thumbnailRequest;
    
        return this;
  }
```

第二种情况

通过 thumbnail 设置了 缩略比列的 

当然对缩略比列的值限制是 0 到 1 之间

```java
    public RequestBuilder<TranscodeType> thumbnail(float sizeMultiplier) {
        if (sizeMultiplier < 0f || sizeMultiplier > 1f) {
          throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
        }
        this.thumbSizeMultiplier = sizeMultiplier;
    
        return this;
  }
```

第三种情况 

没有设置缩略图加载


> 情况1和情况2  都是会生成两个请求 一个是全尺寸，一个是缩略尺寸

> 情况3 只会生成一个全尺寸的请求

构建逻辑 由 SingleRequest的 静态方法完成 

> 其实就是通过静态方法调用SingleRequest的构造方法

> 关于 SingleRequest<R> implements Request, SizeReadyCallback, ResourceCallback

全请求和缩略请求的区别在于 requestOptions 这个参数

```java
    private Request obtainRequest(
      Object requestLock,
      Target<TranscodeType> target,
      RequestListener<TranscodeType> targetListener,
      BaseRequestOptions<?> requestOptions,
      RequestCoordinator requestCoordinator,
      TransitionOptions<?, ? super TranscodeType> transitionOptions,
      Priority priority,
      int overrideWidth,
      int overrideHeight,
      Executor callbackExecutor) {
      
        return SingleRequest.obtain(
            context,
            glideContext,
            requestLock,
            model,
            transcodeClass,
            requestOptions,
            overrideWidth,
            overrideHeight,
            priority,
            target,
            targetListener,
            requestListeners,
            requestCoordinator,
            glideContext.getEngine(),
            transitionOptions.getTransitionFactory(),
            callbackExecutor);
  }
```



