invalidate与requestLayout的区别
====

## 在View中启动scheduleTraversals()的常用方法

1 ）requestLayout()

2 ）invalidate()

3 ) postInvalidate()

4 ) postInvalidateDelayed(long delayMilliseconds)

tips:  postInvalidate()实际也是执行的postInvalidateDelayed(long delayMilliseconds)，
它们用于在非主线程启动scheduleTraversals()，它背后的逻辑就是，在ViewRootImpl中通过Handler，
将线程切换到了UI线程，通过消息的方式，最后还是调用的view的invalidate()方法

下面主要分析下requestLayout() 和 invalidate() 流程的区别

## requestLayout()

view.requestLayout()

```java
    public void requestLayout() {
        if (mMeasureCache != null) mMeasureCache.clear();//清除测量的缓存数据

        if (mAttachInfo != null && mAttachInfo.mViewRequestingLayout == null) {
            // Only trigger request-during-layout logic if this is the view requesting it,
            // not the views in its parent hierarchy
            ViewRootImpl viewRoot = getViewRootImpl();
            if (viewRoot != null && viewRoot.isInLayout()) {
                if (!viewRoot.requestLayoutDuringLayout(this)) {
                    //正在执行performLayout的逻辑，并且正在进行view的布局逻辑时
                    //就不会响应这次布局的请求
                    return;
                }
            }
            mAttachInfo.mViewRequestingLayout = this;
        }

        mPrivateFlags |= PFLAG_FORCE_LAYOUT;//说明1
        mPrivateFlags |= PFLAG_INVALIDATED;
        
        if (mParent != null && !mParent.isLayoutRequested()) {
            mParent.requestLayout();//说明2
        }
        if (mAttachInfo != null && mAttachInfo.mViewRequestingLayout == this) {
            mAttachInfo.mViewRequestingLayout = null;
        }
    }
```
> 说明1

设置flag PFLAG_FORCE_LAYOUT 标识了需要执行测量,这个flag的具体使用在

```java
     public final void measure(int widthMeasureSpec, int heightMeasureSpec){
        ...
         final boolean specChanged = widthMeasureSpec != mOldWidthMeasureSpec
                        || heightMeasureSpec != mOldHeightMeasureSpec;
         final boolean isSpecExactly = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY
                && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY;
         final boolean matchesSpecSize = getMeasuredWidth() == MeasureSpec.getSize(widthMeasureSpec)
                && getMeasuredHeight() == MeasureSpec.getSize(heightMeasureSpec);
         final boolean needsLayout = specChanged
                && (sAlwaysRemeasureExactly || !isSpecExactly || !matchesSpecSize);
            //上面这段逻辑是判断 view的大小是否有改变,如果有改变，那么也会进行测量        
        
         final boolean forceLayout = (mPrivateFlags & PFLAG_FORCE_LAYOUT) == PFLAG_FORCE_LAYOUT;
          if (forceLayout || needsLayout) {
                //满足条件就进行测量
                ...
               onMeasure(widthMeasureSpec, heightMeasureSpec);
               ...
               //同时设置新的flag
               mPrivateFlags |= PFLAG_LAYOUT_REQUIRED;//用于布局操作
               
          }
         ...
         
     }
     
     
    public void layout(int l, int t, int r, int b) {
        if (changed || (mPrivateFlags & PFLAG_LAYOUT_REQUIRED) == PFLAG_LAYOUT_REQUIRED) {
             ...
             //满足条件进行布局操作
             onLayout(changed, l, t, r, b);
             ...
             
             mPrivateFlags &= ~PFLAG_LAYOUT_REQUIRED;//清除 布局flag
             }
             
            ...
            
            mPrivateFlags &= ~PFLAG_FORCE_LAYOUT;
            ...
    }
     
```

> 说明2

1、mParent 就是当前view的父布局。此逻辑会一直向上寻找view的父布局并调用requestLayout()，
最后DecorView的mParent即ViewRootImpl，执行ViewRootImpl.requestLayout()

2、!mParent.isLayoutRequested()，其实就是ViewRootImpl中的mLayoutRequested需要为false，
而该值被修改为false是在performLayout()逻辑中

3、满足条件后，执行ViewRootImpl.requestLayout()
```java
    public void requestLayout() {
        if (!mHandlingLayoutInLayoutRequest) {
            checkThread();//检查是否在当前线程。即主线程中
            mLayoutRequested = true;
            scheduleTraversals();//安排遍历操作
        }
    }
```
最后启动scheduleTraversals()，根据设置的flag，依次执行测量、布局、绘制。

> 结论 requestLayout()方法会触发traversal相关的流程，并根据设置的flag，执行测量、布局、绘制操作

## invalidate()

view.invalidate()

```java
    public void invalidate(boolean invalidateCache) {
        invalidateInternal(0, 0, mRight - mLeft, mBottom - mTop, invalidateCache, true);
    }

    void invalidateInternal(int l, int t, int r, int b, boolean invalidateCache,
            boolean fullInvalidate) {
        ...
        // Reset content capture caches
        mCachedContentCaptureSession = null;
        if ((mPrivateFlags & (PFLAG_DRAWN | PFLAG_HAS_BOUNDS)) == (PFLAG_DRAWN | PFLAG_HAS_BOUNDS)
                || (invalidateCache && (mPrivateFlags & PFLAG_DRAWING_CACHE_VALID) == PFLAG_DRAWING_CACHE_VALID)
                || (mPrivateFlags & PFLAG_INVALIDATED) != PFLAG_INVALIDATED
                || (fullInvalidate && isOpaque() != mLastIsOpaque)) {
            if (fullInvalidate) {
                mLastIsOpaque = isOpaque();
                mPrivateFlags &= ~PFLAG_DRAWN;
            }
            mPrivateFlags |= PFLAG_DIRTY;
            if (invalidateCache) {
                mPrivateFlags |= PFLAG_INVALIDATED;
                mPrivateFlags &= ~PFLAG_DRAWING_CACHE_VALID;
            }
            // Propagate the damage rectangle to the parent view.
            final AttachInfo ai = mAttachInfo;
            final ViewParent p = mParent;
            if (p != null && ai != null && l < r && t < b) {
                final Rect damage = ai.mTmpInvalRect;
                damage.set(l, t, r, b);
                p.invalidateChild(this, damage);/说明1
            }
            // Damage the entire projection receiver, if necessary.
            if (mBackground != null && mBackground.isProjected()) {
                final View receiver = getProjectionReceiver();
                if (receiver != null) {
                    receiver.damageInParent();
                }
            }
        }
    }
```
> 说明1

进入父布局的invalidateChild()并不断请求父布局的该逻辑，在过程中计算父容器和子View需要重绘的区域，求得 (dirty)。

最后进入ViewRootImpl中的invalidateChild()
```java
     private void invalidateRectOnScreen(Rect dirty) {
        final Rect localDirty = mDirty;
        // Add the new dirty rect to the current one
        localDirty.union(dirty.left, dirty.top, dirty.right, dirty.bottom);//说明
        // Intersect with the bounds of the window to skip
        // updates that lie outside of the visible region
        final float appScale = mAttachInfo.mApplicationScale;
        final boolean intersected = localDirty.intersect(0, 0,
                (int) (mWidth * appScale + 0.5f), (int) (mHeight * appScale + 0.5f));
        if (!intersected) {
            localDirty.setEmpty();
        }
        if (!mWillDrawSoon && (intersected || mIsAnimating)) {
            scheduleTraversals();//说明2
        }
    }
```
> 说明1

union合并计算需要处理的视图区域

> 说明2

1) mWillDrawSoon 表示已经历测量和布局，即将进行绘制操作

2) scheduleTraversals();启动遍历

未设置测量标识，就不会执行测量和布局，直接进入绘制操作。但是当view的大小被改变了，依旧会执行测量和布局。

> 总结 invalidate()会启动traversal，但是只会执行绘制操作