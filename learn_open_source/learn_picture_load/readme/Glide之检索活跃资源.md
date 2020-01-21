
对活跃资源的操作在 ActiveResources

首先看下  ActiveResources 实例的构建和相关属性的设置
```java
    //Engine.java的构造方法
    
    if (activeResources == null) {
      activeResources = new ActiveResources(isActiveResourceRetentionAllowed);
    }
    this.activeResources = activeResources;
    activeResources.setListener(this);
```

是否允许活跃资源缓存通过

```java
    // GlideBuilder.java
    public GlideBuilder setIsActiveResourceRetentionAllowed(
      boolean isActiveResourceRetentionAllowed) {
        this.isActiveResourceRetentionAllowed = isActiveResourceRetentionAllowed;
        return this;
  }
```
设置回调监听 setListener(ResourceListener)

回调在 Engine.java中 具体实现了 onResourceReleased 方法
> 对于
```java
    // Engine.java
    public void onResourceReleased(Key cacheKey, EngineResource<?> resource) {
        activeResources.deactivate(cacheKey);
        if (resource.isMemoryCacheable()) {
          cache.put(cacheKey, resource);
        } else {
          resourceRecycler.recycle(resource);
        }
  }
```

**检索**

在Engine.load资源时  如果可以操作缓存，那么先检索活跃资源，看下操作逻辑

```java
    private EngineResource<?> loadFromActiveResources(Key key) {
        EngineResource<?> active = activeResources.get(key);
        if (active != null) {
          active.acquire();
        }
    
        return active;
  }
```

activeResources 就是 **ActiveResources**

进入get方法

首先key 就是当初load时根据资源的相关参数构建的 EngineKey ，该key应用于
很多数据结构中作为保存资源数据的key

activeEngineResources 就是一个HashMap，存放的entry就是 构建的一个
ResourceWeakReference(弱引用)。activeEngineResources.get()就是获取实际的对象
EngineResource

```java
    final Map<Key, ResourceWeakReference> activeEngineResources = new HashMap<>();
```

```java
    synchronized EngineResource<?> get(Key key) {
        ResourceWeakReference activeRef = activeEngineResources.get(key);
        if (activeRef == null) {
          return null;
        }
    
        EngineResource<?> active = activeRef.get();
        if (active == null) {
          cleanupActiveReference(activeRef);
        }
        return active;
  }
```
如果拿到的 EngineResource 为空，那么就从活跃map中将该key对应的内容移除掉
> 当活跃资源不存在（可能被回收了）。那么就回调Engine中用内存来保存
> 实现逻辑在Engine.onResourceReleased方法

```java
    void cleanupActiveReference(@NonNull ResourceWeakReference ref) {
        synchronized (this) {
          activeEngineResources.remove(ref.key);
    
          if (!ref.isCacheable || ref.resource == null) {
            return;
          }
        }
    
        EngineResource<?> newResource =
            new EngineResource<>(
                ref.resource, /*isMemoryCacheable=*/ true, /*isRecyclable=*/ false, ref.key, listener);
        listener.onResourceReleased(ref.key, newResource);
  }
```

**保存**
```java
    // ActiveResources.java
    synchronized void activate(Key key, EngineResource<?> resource) {
        ResourceWeakReference toPut =
            new ResourceWeakReference(
                key, resource, resourceReferenceQueue, isActiveResourceRetentionAllowed);
        // 被取代的旧数据，如果不为空，那么需要回收和释放
        ResourceWeakReference removed = activeEngineResources.put(key, toPut);
        if (removed != null) {
          removed.reset();
        }
  }
```

移除保存

回收和释放移除的对象
```java
    synchronized void deactivate(Key key) {
        ResourceWeakReference removed = activeEngineResources.remove(key);
        
        if (removed != null) {
          removed.reset();
        }
  }
```