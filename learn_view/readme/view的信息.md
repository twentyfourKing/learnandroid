
view的位置信息

位置信息



获取途径

1、相对父布局的位置

相对父布局(0,0)坐标点的值，getX() 和getY() 

getX() 

```text
    计算逻辑 mLeft + getTranslationX()
    mLeft 可以通过getLeft()获取
```
getY() 

```text
    计算逻辑 mTop + getTranslationY()
    mTop 可以通过getTop()获取
```
2、

