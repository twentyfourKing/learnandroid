
### view的大小

view的大小在常规意义上来理解就是view的宽高

I ）、view宽高的获取，一般当界面绘制完成之后就可以通过
getWidth()和getHeight()获得
```text
    //这里就会涉及到获取时机的问题
    //一般在Activity界面完全显示出来需要 ActivityThread.handleResumeActivity
    //执行完成之后，也就是执行了Activity.makeVisible()方法。
    在Activity.makeVisible()方法之前，activity的onCreate和onResume生命周期已经回调了
    所以我们在onCreate和onResume中想去获取view呈现在界面的大小尺寸，是拿不到的
```
那么到底什么时候获取view尺寸合适呢？

通过ViewTreeObserver注册监听是个不错的方法

关于ViewTreeObserver
```text
    //简述ViewTreeObserver
    它是一个final类，主要是管理view树的监听信息
    从类结构中可以发现如下的监听类型：
    
    addOnComputeInternalInsetsListener
    addOnDrawListener
    addOnEnterAnimationCompleteListener
    addOnGlobalFocusChangeListener
    addOnGlobalLayoutListener  视图布局有改变时的回调
    addOnPreDrawListener    绘制之前的回调
    addOnScrollChangedListener
    addOnSystemGestureExclusionRectsChangedListener
    addOnTouchModeChangeListener
    addOnWindowAttachListener
    addOnWindowFocusChangeListener
    addOnWindowShownListener
```
```java
    addOnGlobalLayoutListener -> OnGlobalLayoutListener
    回调被执行是在 ViewRootImpl.performTraversals -> mAttachInfo.mTreeObserver.dispatchOnGlobalLayout()
    在performLayout()逻辑之后
    
    addOnPreDrawListener -> OnPreDrawListener
    回调执行在 ViewRootImpl.performTraversals -> mAttachInfo.mTreeObserver.dispatchOnPreDraw()
    在执行perfromDraw()逻辑之前
    
```
OnGlobalLayoutListener和OnPreDrawListener这两个监听回调被执行时，view的大小已经被确定了
那么我们可以通过这两个回调来获取view的大小

使用方法：

addOnPreDrawListener
```java
    //使用DecorView来添加监听
    //也可以使用特定的某一个view来设置监听
    //因为getViewTreeObserver 是View类的方法，所有继承或者间接继承View都可以使用
    ViewTreeObserver treeObserver = getWindow().getDecorView().getViewTreeObserver();
    treeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            return false;//这里的false和true很有讲究，它涉及到后续具体的draw能够继续执行
        }
    });
    onPreDraw 返回false ， 后续draw可能可以继续执行
    onPreDraw 返回true draw就无法被执行
    原因如下：
    boolean cancelDraw = mAttachInfo.mTreeObserver.dispatchOnPreDraw() || !isViewVisible;
    if (!cancelDraw) {
        ...
        performDraw();
    } else {
        if (isViewVisible) {
            // Try again
            scheduleTraversals();
        } else if (mPendingTransitions != null && mPendingTransitions.size() > 0) {
            ...
        }
    }
    
```
addOnGlobalLayoutListener

```java
    ViewTreeObserver treeObserver = getWindow().getDecorView().getViewTreeObserver();
    treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            
        }
    });
```
> addOnGlobalLayoutListener addOnPreDrawListener
> 的使用当获取到需要的内容或者逻辑后，最好用removexx的方法移除监听，这样可以节省开销

II）、可以通过位置坐标来进行计算

view自身的矩形区域进行计算，获取利用其他view的相对位置进行计算


### view的位置

view的位置概念，无非就是这个view在整个屏幕或者window或者父布局中的位置

我们可以得出这样的结论：view的位置是一个相对值，它需要参照物

所以根据参照物的不同获取的方式就不一样

1、相对父布局的位置
> 相对父布局(0,0)坐标点的值，getX() 和getY() 
```text
    getX() 
    计算逻辑 mLeft + getTranslationX()
    mLeft 可以通过getLeft()获取
```
```text
    getY() 
    计算逻辑 mTop + getTranslationY()
    mTop 可以通过getTop()获取
```
tips:mLeft是子view左边沿距离父（0.0）的横坐标距离,mRight是子view右边沿距离父（0,0）的横坐标距离

2、相对window
>相对window的（0,0）
```java
    int[] locationInWindow = new int[2];
    view.getLocationInWindow(locationInWindow);
```
3、相对屏幕
> 相对屏幕的（0,0）
```java
    int[] locationOnScreen = new int[2];
    view.getLocationOnScreen(locationOnScreen);
```
4、相对Surface
```java
    int[] locationInSurface = new int[2];
    view.getLocationInSurface(locationInSurface);
```
另外还有通过获取可见矩形区域的方式
> 矩形区域不但可以算出位置，同时还可以算出view可见区域的大小

1）相对自身的（0,0）点
```java
    Rect rectLocal = new Rect();
    view.getLocalVisibleRect(rectLocal);
```
2）相对屏幕的（0,0）点
```java
    Rect rectGlobal = new Rect();
    view.getGlobalVisibleRect(rectGlobal);
```

### view的事件

1）先认知MotionEvent

MotionEvent 继承于InputEvent 并实现序列化接口。
就是对手势操作屏幕数据的封装.
> Object used to report movement (mouse, pen, finger, trackball) events. 
Motion events may hold either absolute or relative movements and other data, 
depending on the type of device.

在处理MotionEvent事件时，一般是根据事件的类型进行处理，而类型又是通过
getAction()来获取,如果是多指操作就需要根据getActionMasked()来获取类型；
getActionIndex()获取手指的索引标识

事件类型的定义在 MotionEvent中：
```text
    ACTION_DOWN             = 0
    ACTION_UP               = 1
    ACTION_MOVE             = 2
    ACTION_CANCEL           = 3
    ACTION_OUTSIDE          = 4
    ACTION_POINTER_DOWN     = 5  多指时，第一个接触屏幕
    ACTION_POINTER_UP       = 6  多指时，最后一个指离开屏幕
    ACTION_HOVER_MOVE       = 7
    ACTION_SCROLL           = 8
    ACTION_HOVER_ENTER      = 9
    ACTION_HOVER_EXIT       = 10
```
在MotionEvent不单有事件类型还有，位置的概念，就是当前触摸点的位置信息

getX() 和getY() 获取的值是当前触摸点距离view自身（0,0）的值

getRawX() 和getRawY() 获取的值是当前触摸点距离屏幕（0，0）的值

MotionEvent还有其他很多getxx可以获取的值。它们都是用底层方法获取的。

2）说说触摸事件

```java
    //触摸监听接口 View.OnTouchListener
    public interface OnTouchListener {
        /**
         * Called when a touch event is dispatched to a view. This allows listeners to
         * get a chance to respond before the target view.
         *
         * @param v The view the touch event has been dispatched to.
         * @param event The MotionEvent object containing full information about
         *        the event.
         * @return True if the listener has consumed the event, false otherwise.
         */
        boolean onTouch(View v, MotionEvent event);
    }
```
分发这个监听在View.dispatchTouchEvent,不过这个分发的具体时机在后续研究，这里就涉及到注册系统输入事件和
分发事件的流程分析

这里先简单理下思路：

从DecorView开始分发dispatchTouchEvent
```java
    //DecorView
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final Window.Callback cb = mWindow.getCallback();//cb其实就是对应的Activity
        return cb != null && !mWindow.isDestroyed() && mFeatureId < 0
                ? cb.dispatchTouchEvent(ev) : super.dispatchTouchEvent(ev);
    }
    //当activity可以分发时，就执行转入Activity.dispatchTouchEvent
```
```java
    //Activity
     public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onUserInteraction();
        }
        if (getWindow().superDispatchTouchEvent(ev)) {//说明
            return true;
        }
        return onTouchEvent(ev);
    }
    //说明
    getWindow()其实获取的就是PhoneWindow
    public boolean superDispatchTouchEvent(MotionEvent event) {
        return mDecor.superDispatchTouchEvent(event);
    }
    //又回到DecorView
    public boolean superDispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }
    // super 找 FrameLayout 、ViewGroup 、View
    //发现是进入ViewGroup.dispatchTouchEvent
    
```
```java
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ...
        //校验是否被拦截 关键方法 onInterceptTouchEvent
        if (actionMasked == MotionEvent.ACTION_DOWN
                || mFirstTouchTarget != null) {
            final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
            if (!disallowIntercept) {
                intercepted = onInterceptTouchEvent(ev);
                ev.setAction(action); // restore action in case it was changed
            } else {
                intercepted = false;
            }
        } else {
            // There are no touch targets and this action is not an initial down
            // so this view group continues to intercept touches.
            intercepted = true;
        }
        //如果违背拦截 和 取消 ，那么继续分发给子view或ViewGroup
        ...
    }
```
当来到最底层的view时，看view的onTouchEvent的返回
onTouchEvent返回true那么表示当前的view已经处理该次touch事件，如果返回false，那么就会调到父布局的
onTouchEvent，是否消化该touch事件，以此类推，最后来到()其实就是看activity分发dispatchTouchEvent方法中
superDispatchTouchEvent返回情况，true表明touch事件已经被view层处理，返回false表明事件并未被处理

```java
    public boolean onTouchEvent(MotionEvent event) {
        if (mWindow.shouldCloseOnTouch(this, event)) {
            finish();
            return true;
        }
        return false;
    }
```
tips:还有一个需要注意的逻辑，就是在onTouchEvent的ACTION_DOWN类型时，如果已经返回了false，那就
是表明该view对该touch事件没有兴趣，后续的其他类型的事件也不会分发给该view进行分发

在进入view进行分发时还有一个逻辑

OnTouchListener具有优先处理权
```java
    //就是当注册了OnTouchListener直接回调，如果没有注册监听那么就会进入view本身的onTouchEvent方法处理touch事件
    ListenerInfo li = mListenerInfo;
    if (li != null && li.mOnTouchListener != null
            && (mViewFlags & ENABLED_MASK) == ENABLED
            && li.mOnTouchListener.onTouch(this, event)) {
        result = true;
    }

    if (!result && onTouchEvent(event)) {
        result = true;
    }
```

还有一个常用的监听接口 OnClickListener
```java
    //View.OnClickListener
    public interface OnClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        void onClick(View v);
    }
```
OnClickListener的回调监听是在开始执行view的onTouchEvent后,有一个处理逻辑
```java
    if (mPerformClick == null) {
        mPerformClick = new PerformClick();
    }
    if (!post(mPerformClick)) {
        performClickInternal();
    }
```
#### 关于拦截和阻止拦截
> 拦截

如果当前的ViewGroup想拦截事件的分发，那么覆写
onInterceptTouchEvent方法返回true就表示拦截了,如果想拦截不同的action类型
```java
    //ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //ev.getAction()
        做不同的拦截
        //如果ACTION_DOWN被拦截了，那么后续的action也被拦截
    }
```
> 阻止拦截
```java
    //ViewGroup
    requestDisallowInterceptTouchEvent
```
在子view中调用父布局的requestDisallowInterceptTouchEvent(xx)
xx 为true就是不允许拦截

具体操作是：eg
```java
    //在子view中覆写dispatchTouchEvent方法，根据不容的action进行处理
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if (父容器需要当前点击事件) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            default:{
                break;
            }
        }
        mLastXIntercept = x;
        mLastYIntercept = y;
        return super.dispatchTouchEvent(ev);
    }
```
但是如果想做一锤子买卖，那么就直接调用getParent().requestDisallowInterceptTouchEvent(true);

3） [事件的拓展总结]()

### view的绘制












    

