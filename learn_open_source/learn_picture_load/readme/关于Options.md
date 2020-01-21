
对请求配置的设置


基础抽象类
```java
    public abstract class BaseRequestOptions<T extends BaseRequestOptions<T>>
    
    implements Cloneable
```
RequestBuilder 对 BaseRequestOptions 的实现
```java
    public class RequestBuilder<TranscodeType> extends
    BaseRequestOptions<RequestBuilder<TranscodeType>>
    implements Cloneable, ModelTypes<RequestBuilder<TranscodeType>> 
```

通过 apply() 进行配置的设置

apply() 也是  RequestBuilder 对 BaseRequestOptions 的方法的覆写

**对部分变量的说明**


resourceClass在基类中的初始化设置如下
```java
    private Class<?> resourceClass = Object.class;
```
可以通过其他方法进行重新赋值

在构建新的配置实例时，可以通过如下方法设置
```java
    //BaseRequestOptions.java
    public T decode(@NonNull Class<?> resourceClass) {
    if (isAutoCloneEnabled) {
      return clone().decode(resourceClass);
    }
    this.resourceClass = Preconditions.checkNotNull(resourceClass);
    fields |= RESOURCE_CLASS;
    return selfOrThrowIfLocked();
  }
```

```java
    // BaseRequestOptions.java
    // 设置bitmap保存格式，一个像素占用多少个字节的内存
    // 默认是 DecodeFormat.PREFER_ARGB_8888
    public T format(@NonNull DecodeFormat format) {
        Preconditions.checkNotNull(format);
        return set(Downsampler.DECODE_FORMAT, format).set(GifOptions.DECODE_FORMAT, format);
      }
```