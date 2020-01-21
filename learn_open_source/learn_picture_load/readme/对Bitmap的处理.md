
## 创建Bitmap

**8.0以上(api 28)**
```java
    //Bitmap.java
    createBitmap(xx) -> 
    // 构建图片过程中默认使用 ,一个像素占4个字节
    Config newConfig = Config.ARGB_8888;

    //进入jni层
    private static native Bitmap nativeCreate(int[] colors, int offset,
                                                  int stride, int width, int height,
                                                  int nativeConfig, boolean mutable,
                                                  long nativeColorSpace);
```

```objectivec
    //frameworks/base/core/jni/android/graphics/Bitmap.cpp
    static const JNINativeMethod gBitmapMethods[] = {
        {   "nativeCreate",             "([IIIIIIZJ)Landroid/graphics/Bitmap;",
            (void*)Bitmap_creator },
            ...
```
```objectivec
    static jobject Bitmap_creator(JNIEnv* env, jobject, jintArray jColors,
                                  jint offset, jint stride, jint width, jint height,
                                  jint configHandle, jboolean isMutable,
                                  jlong colorSpacePtr) {
        ...
        //在native层构建存放Bitmap的内存空间
        sk_sp<Bitmap> nativeBitmap = Bitmap::allocateHeapBitmap(&bitmap);
        ...
    
        return createBitmap(env, nativeBitmap.release(), getPremulBitmapCreateFlags(isMutable));
    }
```
```objectivec
    jobject createBitmap(JNIEnv* env, Bitmap* bitmap,
            int bitmapCreateFlags, jbyteArray ninePatchChunk, jobject ninePatchInsets,
            int density) {
        ...
        //使用jni回调到java层Bitmap类的构造方法 如下：说明1
        jobject obj = env->NewObject(gBitmap_class, gBitmap_constructorMethodID,
                reinterpret_cast<jlong>(bitmapWrapper), bitmap->width(), bitmap->height(), density,
                isPremultiplied, ninePatchChunk, ninePatchInsets, fromMalloc);
        ...
        return obj;
    }
```
> 说明1：
```java
    // Bitmap.java
    // nativeBitmap 是native层为这个Bitmap分配的内存大小
    Bitmap(long nativeBitmap, int width, int height, int density,
            boolean requestPremultiplied, byte[] ninePatchChunk,
            NinePatch.InsetStruct ninePatchInsets) {
        this(nativeBitmap, width, height, density, requestPremultiplied, ninePatchChunk,
                ninePatchInsets, true);
    }
```
经过上面的步骤，得出结论：Bitmap被保存在native层的内存空间中，并不会占用堆内存的大小

**8.0以下版本**

源码使用的7.1版本
```java
      //Bitmap.java
      private static native Bitmap nativeCreate(int[] colors, int offset,
                                                  int stride, int width, int height,
                                                  int nativeConfig, boolean mutable);
```
```objectivec
    // frameworks/base/core/jni/android/graphics/Bitmap.cpp
    static jobject Bitmap_creator(JNIEnv* env, jobject, jintArray jColors,
                                  jint offset, jint stride, jint width, jint height,
                                  jint configHandle, jboolean isMutable) {
        ...
        //  说明1
        Bitmap* nativeBitmap = GraphicsJNI::allocateJavaPixelRef(env, &bitmap, NULL);
        ...
        return GraphicsJNI::createBitmap(env, nativeBitmap,
                getPremulBitmapCreateFlags(isMutable));
    }
```
```objectivec
    jobject GraphicsJNI::createBitmap(JNIEnv* env, android::Bitmap* bitmap,
            int bitmapCreateFlags, jbyteArray ninePatchChunk, jobject ninePatchInsets,
            int density) {
        ...
        // 说明2
        jobject obj = env->NewObject(gBitmap_class, gBitmap_constructorMethodID,
                reinterpret_cast<jlong>(bitmap), bitmap->javaByteArray(),
                bitmap->width(), bitmap->height(), density, isMutable, isPremultiplied,
                ninePatchChunk, ninePatchInsets);
        hasException(env); // For the side effect of logging.
        return obj;
    }
```
> 说明1

```objectivec
    android::Bitmap* GraphicsJNI::allocateJavaPixelRef(JNIEnv* env, SkBitmap* bitmap,
                                                 SkColorTable* ctable) {
        ...
        // 创建java层的数组来存放 Bitmap
        jbyteArray arrayObj = (jbyteArray) env->CallObjectMethod(gVMRuntime,
                                                                 gVMRuntime_newNonMovableArray,
                                                                 gByte_class, size);
        jbyte* addr = (jbyte*) env->CallLongMethod(gVMRuntime, gVMRuntime_addressOf, arrayObj);
        if (env->ExceptionCheck() != 0) {
            return NULL;
        }
        android::Bitmap* wrapper = new android::Bitmap(env, arrayObj, (void*) addr,
                info, rowBytes, ctable);
        wrapper->getSkBitmap(bitmap);
        ...
        return wrapper;
    }
```
> 说明2

jni的方式回调到java层Bitmap的构造方法
```java
    // Bitmap.java
    Bitmap(long nativeBitmap, byte[] buffer, int width, int height, int density,
                boolean isMutable, boolean requestPremultiplied,
                byte[] ninePatchChunk, NinePatch.InsetStruct ninePatchInsets) {
        if (nativeBitmap == 0) {
            throw new RuntimeException("internal error: native bitmap is 0");
        }
        ...
    }
```
通过上述创建过程，Bitmap被存放在堆内存空间中。

## 回收Bitmap

**5.1-6.0**

在Bitmap的构造方法中,创建 BitmapFinalizer 实例
```java
     Bitmap(long nativeBitmap, byte[] buffer, int width, int height, int density,
                boolean isMutable, boolean requestPremultiplied,
            byte[] ninePatchChunk, NinePatch.InsetStruct ninePatchInsets) {
        ...
        //构建 BitmapFinalizer 参数为内存地址
        mFinalizer = new BitmapFinalizer(nativeBitmap);
        int nativeAllocationByteCount = (buffer == null ? getByteCount() : 0);
        mFinalizer.setNativeAllocationByteCount(nativeAllocationByteCount);
    }
```
BitmapFinalizer 覆写了 finalize()方法
> finalize() 是Object类的 方法

> finalize() 垃圾回收器,发现没有引用指向时会回调该方法

> Called by the garbage collector on an object when garbage collection
       determines that there are no more references to the object.
       A subclass overrides the {@code finalize} method to dispose of
       system resources or to perform other cleanup.
```java
      private static class BitmapFinalizer {
            private long mNativeBitmap;
    
            // Native memory allocated for the duration of the Bitmap,
            // if pixel data allocated into native memory, instead of java byte[]
            private int mNativeAllocationByteCount;
    
            BitmapFinalizer(long nativeBitmap) {
                mNativeBitmap = nativeBitmap;
            }
    
            public void setNativeAllocationByteCount(int nativeByteCount) {
                if (mNativeAllocationByteCount != 0) {
                    VMRuntime.getRuntime().registerNativeFree(mNativeAllocationByteCount);
                }
                mNativeAllocationByteCount = nativeByteCount;
                if (mNativeAllocationByteCount != 0) {
                    VMRuntime.getRuntime().registerNativeAllocation(mNativeAllocationByteCount);
                }
            }
    
            @Override
            public void finalize() {
                try {
                    super.finalize();
                } catch (Throwable t) {
                    // Ignore
                } finally {
                    setNativeAllocationByteCount(0);
                    nativeDestructor(mNativeBitmap);
                    mNativeBitmap = 0;
                }
            }
     }
```
当Bitmap被回收时， BitmapFinalizer 实例变量也会被回收，那么finalize()
就会被调到，然后释放native层针对Bitmap的一些引用。

**7.0-9.0**

Bitmap的构造方法中 创建 NativeAllocationRegistry 实例
```java
    Bitmap(long nativeBitmap, byte[] buffer, int width, int height, int density,
                boolean isMutable, boolean requestPremultiplied,
                byte[] ninePatchChunk, NinePatch.InsetStruct ninePatchInsets) {
        ...
        mNativePtr = nativeBitmap;
        long nativeSize = NATIVE_ALLOCATION_SIZE;
        if (buffer == null) {
            nativeSize += getByteCount();
        }
        NativeAllocationRegistry registry = new NativeAllocationRegistry(
            Bitmap.class.getClassLoader(), nativeGetNativeFinalizer(), nativeSize);
        registry.registerNativeAllocation(this, nativeBitmap);
    }
```
注册垃圾回收器的监听 ,当Bitmap被回收时，那么与该Bitmap相关的native层的内存也会被回收
```java
    // libcore/luni/src/main/java/libcore/util/NativeAllocationRegistry.java
    private static void registerNativeAllocation(long size) {
        VMRuntime runtime = VMRuntime.getRuntime();
        if ((size & IS_MALLOCED) != 0) {
            final long notifyImmediateThreshold = 300000;
            if (size >= notifyImmediateThreshold) {
                runtime.notifyNativeAllocationsInternal();
            } else {
                runtime.notifyNativeAllocation();
            }
        } else {
            runtime.registerNativeAllocation(size);
        }
    }
```

## 图片压缩



