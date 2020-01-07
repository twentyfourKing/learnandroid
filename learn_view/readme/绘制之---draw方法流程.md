自定义view涉及自定义绘制逻辑时，需要明白绘制的几个阶段

当视图刷新流程进入performDraw时，选择用软件进行绘制后进入View.draw()方法

view.draw方法定义了绘制的具体流程

> 绘制背景  drawBackground
> 绘制自身的内容 onDraw
> 绘制子view dispatchDraw
> 绘制滑动条和前景图 

tips：每个阶段的绘制都会覆盖前一个阶段绘制的内容

自定义view时，根据需要来覆写不同的绘制逻辑
draw() onDraw() dispatchDraw()都可以覆写，drawBackground()方法无法被覆写

有一个特殊情况当继承于ViewGroup时，它的绘制流程是直接进入dispatchDraw，如果要打断这个流程使用
setWillNotDraw(false)
