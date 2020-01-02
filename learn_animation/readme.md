动画分类
===

Property animation 和 View animation  

> [Animation resources](https://developer.android.com/guide/topics/resources/animation-resource)

## View animation
![视图动画-类结构](https://github.com/twentyfourKing/learnandroid/blob/master/learn_animation/readme/img/img_2.png)

1) Tween animation

2) Frame animation

> AnimationSet 可以理解位动画集合器，它可以把多个动画集合在一起运行

> AnimationUtils  用于从xml文件加载动画

> AnimationListener 动画监听器

### 一、视图动画的使用

1）通过xml加载



2) 通过代码配置



## Property animation
![属性动画-类结构](https://github.com/twentyfourKing/learnandroid/blob/master/learn_animation/readme/img/img_3.png)

属性动画，顾名思义就是会调整view的属性值

> ValueAnimator

> ObjectAnimator

> AnimatorSet 严格意义上来说不是属性动画，只能算动画包装器

> AnimatorInflater 用于从xml文件加载动画


### 一、属性动画的使用方式
1) 通过xml的方式配置

需要在res/路径下创建animator文件存放动画xml文件（参考：res/animator/filename）
xml文件必须要有一个<set>或者 <objectAnimator>或者<valueAnimator>作为根标签
如果有多个动画需要组合，用<set>作为根标签，来包容其他标签

(1) <set>

    <set> 相当于实体类 AnimatorSet (android.animation.AnimatorSet)
    <set> 的 android:ordering 属性表示：指定该集合中动画的播放顺序(together 一起 ; sequentially 顺序)

(2) <objectAnimator>

    对应的实体类是 ObjectAnimator(android.animation.ObjectAnimator)

```text
   android:propertyName
           
   android:valueFrom
           
   android:startOffset 动画延时执行的时间
           
   android:repeatMode
           
   android:valueType 
```
(4) <animator>

    对应的实体类 ValueAnimator(android.animation.ValueAnimator)

```text
    android:valueTo
           
    android:duration
            
    android:repeatCount

    android:repeatMode

    android:valueType
```

(4) 例子

```xml
<!--    res/animator/property_animator.xml-->
    <set android:ordering="sequentially">
        <set>
            <objectAnimator
                android:propertyName="x"
                android:duration="500"
                android:valueTo="400"
                android:valueType="intType"/>
            <objectAnimator
                android:propertyName="y"
                android:duration="500"
                android:valueTo="300"
                android:valueType="intType"/>
        </set>
        <objectAnimator
            android:propertyName="alpha"
            android:duration="500"
            android:valueTo="1f"/>
    </set>
```

(5) 动画加载

```java
    AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(myContext,R.animator.property_animator);
        set.setTarget(myObject);
        set.start();
```
2) 通过代码的方式
```java
   ObjectAnimator  objectAnimator = ObjectAnimator.ofFloat(view, "scaleX",1f, 1.5f);
            objectAnimator.setDuration(mDuration);//动画执行时间
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);//重复次数
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);//重复的模式
            objectAnimator.start();
            //上面这段代码就是通过代码实现了一个放大view的属性动画
```

### 二、属性动画的监听器

Animator 中有 AnimatorPauseListener AnimatorListener

AnimatorSet 继承于Animator 那么也有AnimatorPauseListener AnimatorListener

ValueAnimator 继承于Animator 也有AnimatorPauseListener AnimatorListener ，同时还有自己的AnimatorUpdateListener  

ObjectAnimator 继承于 ValueAnimator

AnimatorListener接口
```java
    public static interface AnimatorListener {
    
        default void onAnimationStart(Animator animation, boolean isReverse) {
            onAnimationStart(animation);
        }

        default void onAnimationEnd(Animator animation, boolean isReverse) {
            onAnimationEnd(animation);
        }

        void onAnimationStart(Animator animation);

        void onAnimationEnd(Animator animation);

        void onAnimationCancel(Animator animation);

        void onAnimationRepeat(Animator animation);
    }
```
AnimatorPauseListener接口
```java
    public static interface AnimatorPauseListener {

        void onAnimationPause(Animator animation);

        void onAnimationResume(Animator animation);
    }
```
AnimatorUpdateListener接口
```java
    public static interface AnimatorUpdateListener {
       void onAnimationUpdate(ValueAnimator animation);
    }
```

### [更详细的内容](https://github.com/twentyfourKing/learnandroid/tree/master/learn_animation/readme)
