
Resource 是一个基础的接口

```java
    public interface Resource<Z> {
        Class<Z> getResourceClass();
        Z get();
        int getSize();
        void recycle();
    }
```

具体的Resource类型

BitmapDrawableResource

BytesResource

**EngineResource**

FileResource

GifDrawableResource

LazyBitmapDrawableResource

LockedResource

NonOwnedBitmapResource

NonOwnedDrawableResource


### EngineResource

