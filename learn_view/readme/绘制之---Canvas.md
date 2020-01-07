Canvas 简述
=======

BaseCanvas 是对skia底层图形绘制引擎的java层抽象

真正的工作类是Canvas，它继承于BaseCanvas实现了抽象的方法,扩展了绘制方式

从绘制方法举例来说：

Canvas.drawLine -> BaseCanvas.drawLine -> nDrawLine -> 具体的绘制逻辑在底层的skia中

> nDrawLine 方法的jni定义在 android_graphics_Canvas.cpp

```objectivec
    //frameworks/base/core/jni/android_graphics_Canvas.cpp
    static const JNINativeMethod gDrawMethods[] = {
        {"nDrawColor","(JII)V", (void*) CanvasJNI::drawColor},
        {"nDrawColor","(JJJI)V", (void*) CanvasJNI::drawColorLong},
        {"nDrawPaint","(JJ)V", (void*) CanvasJNI::drawPaint},
        {"nDrawPoint", "(JFFJ)V", (void*) CanvasJNI::drawPoint},
        {"nDrawPoints", "(J[FIIJ)V", (void*) CanvasJNI::drawPoints},
        {"nDrawLine", "(JFFFFJ)V", (void*) CanvasJNI::drawLine},
        ...
        //还有其他很多方法
     }
```

```objectivec
    static void drawLine(JNIEnv* env, jobject, jlong canvasHandle, jfloat startX, jfloat startY,
                         jfloat stopX, jfloat stopY, jlong paintHandle) {
        Paint* paint = reinterpret_cast<Paint*>(paintHandle);
        get_canvas(canvasHandle)->drawLine(startX, startY, stopX, stopY, *paint);
    }
```
方法的定义在

```objectivec
    //frameworks/base/libs/hwui/hwui/Canvas.h
    virtual void drawPaint(const SkPaint& paint) = 0;
```
具体的实现在
```objectivec
    //frameworks/base/libs/hwui/SkiaCanvas.cpp
    void SkiaCanvas::drawLine(float startX, float startY, float stopX, float stopY,
                              const SkPaint& paint) {
        mCanvas->drawLine(startX, startY, stopX, stopY, *filterPaint(paint));
    }
```
从上面举例的方法中，我们可以看出，Canvas所定义绘制方法，具体的绘制逻辑都是在底层实现。
用到的Paint属性也转换为了底层的 skia中的 SkPaint

依次类推
concat()；
setMatrix()；
save() ；
restore()；
等方法都是在底层实现

### 分析下save() 和restore()

save()
```objectivec
    static jint save(jlong canvasHandle, jint flagsHandle) {
        SaveFlags::Flags flags = static_cast<SaveFlags::Flags>(flagsHandle);
        return static_cast<jint>(get_canvas(canvasHandle)->save(flags));
    }
```
```objectivec
    int SkiaCanvas::save(SaveFlags::Flags flags) {
        int count = mCanvas->save();
        recordPartialSave(flags);
        return count;
    }
```
```objectivec
    void SkiaCanvas::recordPartialSave(SaveFlags::Flags flags) {
        // A partial save is a save operation which doesn't capture the full canvas state.
        // (either SaveFlags::Matrix or SaveFlags::Clip is missing).
    
        // Mask-out non canvas state bits.
        flags &= SaveFlags::MatrixClip;
    
        if (flags == SaveFlags::MatrixClip) {
            // not a partial save.
            return;
        }
    
        if (!mSaveStack) {
            mSaveStack.reset(new SkDeque(sizeof(struct SaveRec), 8));
        }
    
        SaveRec* rec = static_cast<SaveRec*>(mSaveStack->push_back());
        rec->saveCount = mCanvas->getSaveCount();
        rec->saveFlags = flags;
        rec->clipIndex = mClipStack.size();
    }
```
mSaveStack的数据结构
```objectivec
     std::unique_ptr<SkDeque> mSaveStack;
```
大致的逻辑流程就是，保存当前的属性特征在“栈”中

restore()

```objectivec
    static bool restore(jlong canvasHandle) {
        Canvas* canvas = get_canvas(canvasHandle);
        if (canvas->getSaveCount() <= 1) {
            return false; // cannot restore anymore
        }
        canvas->restore();
        return true; // success
    }
```
```objectivec
    void SkiaCanvas::restore() {
        const auto* rec = this->currentSaveRec();
        if (!rec) {
            // Fast path - no record for this frame.
            mCanvas->restore();
            return;
        }
    
        bool preserveMatrix = !(rec->saveFlags & SaveFlags::Matrix);
        bool preserveClip = !(rec->saveFlags & SaveFlags::Clip);
    
        SkMatrix savedMatrix;
        if (preserveMatrix) {
            savedMatrix = mCanvas->getTotalMatrix();
        }
    
        const size_t clipIndex = rec->clipIndex;
    
        mCanvas->restore();
        mSaveStack->pop_back();
    
        if (preserveMatrix) {
            mCanvas->setMatrix(savedMatrix);
        }
    
        if (preserveClip) {
            this->applyPersistentClips(clipIndex);
        }
    }
```
mSaveStack->pop_back();出“栈”。
这个栈明显就是要给后进先出的栈

### 使用

```text
    save();      //保存状态
    ...          //具体操作
    restore();   //回滚到之前的状态
```

[参考](https://www.gcssloop.com/customview/Canvas_Convert)



