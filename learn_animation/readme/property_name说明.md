属性动画，是对view所包含的属性值进行有规律的调整，所呈现出来的动态效果
view的属性值是通过view中定义的setxx方法进行设置的，属性动画就是就是根据setxx的xx属性使用反射的方式调用
调view中对应的setxx方法，从而动态改变view的指定属性。
比如View中的setAlpha()方法，对应的propertyName就是"alpha"依次类推，View类中体现出的属性调整方法还有：

setTranslationX -> translationX
setTranslationY -> translationY
setRotationX -> rotationX
setRotationY -> rotationY
setRotation -> rotation
setX -> x
setY -> y

因为View是其他自定义View的基类，这些属性可以在自定义的View中使用，自定义View中如果增加其他属性，而且该属性可以通过setxx
进行设置，那么该属性可以被用作属性动画的propertyName，可以通过属性动画的方式来调整自定义view对应的属性
比如：TextView

setTextSize -> textSize
setTextColor -> textColor
依次类推

在PropertyValuesHolder 和ObjectAnimator中都会使用到propertyName
