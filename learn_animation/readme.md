## 动画分类

Property animation 和 View animation  

> Tween animation

> Frame animation

> [Animation resources](https://developer.android.com/guide/topics/resources/animation-resource)

### Property animation

ValueAnimator

通过xml的方式配置

可以指向的编译对象 ：ValueAnimator, ObjectAnimator, or AnimatorSet.
参考：res/animator/filename
xml文件必须要有一个<set>或者 <objectAnimator>或者<valueAnimator>作为根标签
如果有多个动画需要组合，可以在用<set>作为根标签，用来包容其他标签
1)<set>
<set> 相当于实体类 AnimatorSet (android.animation.AnimatorSet)
<set> 的 android:ordering 属性表示：指定该集合中动画的播放顺序(together 一起 ; sequentially 顺序)
2) <objectAnimator>
    对应的实体类是 ObjectAnimator(android.animation.ObjectAnimator)
```text
   android:propertyName
           String. Required. The object's property to animate, referenced by its name.
           For example you can specify "alpha" or "backgroundColor" for a View object. 
           The objectAnimator element does not expose a target attribute, however,
            so you cannot set the object to animate in the XML declaration. 
            You have to inflate your animation XML resource by calling loadAnimator() 
            and call setTarget() to set the target object that contains this property.
           android:valueTo
           float, int, or color. Required. The value where the animated property ends.
            Colors are represented as six digit hexadecimal numbers (for example, #333333).
   android:valueFrom
           float, int, or color. The value where the animated property starts.
            If not specified, the animation starts at the value obtained by the property's get
             method. Colors are represented as six digit hexadecimal numbers (for example, #333333).
           android:duration
           int. The time in milliseconds of the animation. 300 milliseconds is the default.
   android:startOffset
           int. The amount of milliseconds the animation delays after start() is called.
           android:repeatCount
           int. How many times to repeat an animation. Set to "-1" to infinitely repeat or 
           to a positive integer. For example, a value of "1" means that the animation is 
           repeated once after the initial run of the animation, so the animation plays a 
           total of two times. The default value is "0", which means no repetition.
   android:repeatMode
           int. How an animation behaves when it reaches the end of the animation. 
           android:repeatCount must be set to a positive integer or "-1" for this attribute
            to have an effect. Set to "reverse" to have the animation reverse direction with
             each iteration or "repeat" to have the animation loop from the beginning each time.
   android:valueType
           Keyword. Do not specify this attribute if the value is a color. The animation 
           framework automatically handles color values
```
3)<animator>
    对应的实体类 ValueAnimator(android.animation.ValueAnimator)
```text
    android:valueTo
            float, int, or color. Required. The value where the animation ends.
             Colors are represented as six digit hexadecimal numbers (for example, #333333).
            android:valueFrom
            float, int, or color. Required. The value where the animation starts.
             Colors are represented as six digit hexadecimal numbers (for example, #333333).
    android:duration
            int. The time in milliseconds of the animation. 300ms is the default.
            android:startOffset
            int. The amount of milliseconds the animation delays after start() is called.
    android:repeatCount
            int. How many times to repeat an animation. Set to "-1" to infinitely 
            repeat or to a positive integer. For example, a value of "1" means that 
            the animation is repeated once after the initial run of the animation, 
            so the animation plays a total of two times. The default value is "0", 
            which means no repetition.
    android:repeatMode
            int. How an animation behaves when it reaches the end of the animation.
             android:repeatCount must be set to a positive integer or "-1" for this 
             attribute to have an effect. Set to "reverse" to have the animation reverse 
             direction with each iteration or "repeat" to have the animation loop from the beginning each time.
    android:valueType
             Keyword. Do not specify this attribute if the value is a color. 
             The animation framework automatically handles color values.
```
4) 例子
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
5) 在设置好动画的xml文件后，就是动画的加载
```java
    AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(myContext,R.animator.property_animator);
        set.setTarget(myObject);
        set.start();
```