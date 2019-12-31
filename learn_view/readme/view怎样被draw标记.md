view.draw流程是绘制view

那么我们用requestLayout和invalidate方法是，怎样标记其他view不进行绘制呢？

在view.draw(Canvas canvas, ViewGroup parent, long drawingTime) 

该方法是父布局遍历子view执行绘制时调用的方法
```java
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
            return child.draw(canvas, this, drawingTime);
        }
```
在draw(canvas, this, drawingTime)中有这样的逻辑来跳过没有被标记view，不执行绘制
```java
     if (!drawingWithDrawingCache) {
                if (drawingWithRenderNode) {
                    mPrivateFlags &= ~PFLAG_DIRTY_MASK;
                    ((RecordingCanvas) canvas).drawRenderNode(renderNode);
                } else {
                    // Fast path for layouts with no backgrounds
                    if ((mPrivateFlags & PFLAG_SKIP_DRAW) == PFLAG_SKIP_DRAW) {
                        mPrivateFlags &= ~PFLAG_DIRTY_MASK;
                        dispatchDraw(canvas);
                    } else {
                        draw(canvas);
                    }
                }
            }
```

那么不会被绘制的view的这个flag是怎样被设置的呢？是不是view绘制完成后，就会主动给标记这个状态，
我们通过反射的方法，看看当view都绘制完成的时候，mPrivateFlags这个变量的是什么？是不是已经包含了这个
不需要绘制的flag
