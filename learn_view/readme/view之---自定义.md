view自定义的3种实现方式
===

1、自定义于系统已有控件或布局

> 已有控件：TextView 、ImageView等

> 已有布局：LinearLayout、FrameLayout等

这种形式的自定义，测量、布局、绘制都有现成的逻辑。更多的操作是在原有基础上添加一些新的效果或属性，
也就是说所继承的控件或布局它们本身的测量布局绘制流程主体是不变的，而是在流程上做加法。

比如常见的例子：

想实现一个正方形的ImageView，那么就可以直接继承系统的ImageView，然后在重写的
onMeasure方法中取出父onMeasure测量出的宽高值，做下修正逻辑，再将值保存回view的宽高
变量中
```java
    //自定义于系统已有控件
    public CircleImageView extends ImageView{
        @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec,heightMeasureSpec);//按照原有逻辑进行测试
                ...
                int width = getMeasuredWidth();
                int height = getMeasuredHeight();
                if (width < height) {
                    setMeasuredDimension(width, width);
                } else {
                    setMeasuredDimension(height, height);
                }
            }
    }
```
2、自定义于View或者ViewGroup

这种形式的自定义，相对来说自由程度高，但是复杂程度也高

> 自定义于View

view的大小和样式都需要代码覆写实现，主要涉及的覆写方法有:

```text
     onMeasure 定义测量的规则
    
     onLayout 定义布局的规则
    
     onDraw 定义具体的绘制逻辑
```
> 自定义于ViewGroup

相当于自定义布局容器，那么不单要定义自身的测量、布局、绘制，需要安排、
其子view的测量和布局,同样需要实现：

```text
     onMeasure 定义测量的规则
    
     onLayout 定义布局的规则
    
     onDraw 定义具体的绘制逻辑
```
不过核心逻辑在onMeasure和onLayout

onMeasure里面需要完成的逻辑有2：

1) 遍历子view，并用该布局的父布局提交的测量规则（MeasureSpec 包括mode和size），来
定制子view的测量规则(MeasureSpec.makeMeasureSpec)，然后再调用childview的measure(xx,xx)进行测量。

2） 在测量完所有的子view之后，最后测量自身的尺寸，并保存测量后的尺寸。

onLayout逻辑：需要遍历子view并，用相关的位置关系来调用childview的layout方法进行布局

3、自定义于多个view的集合

将多个现成的View或者ViewGroup拼装在一起，实现一个功能的集合，在视图上来看这些view就是集合块。

多用于复杂界面，功能块内聚的效果，在这种自定义的view中，我们不在过分关注view的布局和绘制，而更多在乎的是
功能和逻辑，同时包括view触发事件等。逻辑层面可以用接口回调的方法与视图管理类进行通信。

#### tag:后续会在代码中具体实践，待后续









