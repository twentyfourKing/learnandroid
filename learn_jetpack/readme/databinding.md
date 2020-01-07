总结
====
## 双向绑定
> 数据刷新视图
> 视图刷新数据

## (一)数据刷新视图
对于数据监听主要靠 BaseObservable ，它的子抽象类BaseObservableField，总的来说
对基本的数据类型都有监听者

对数据的监听有两种方式：

1）使用已经存在的监听类

> Int -> ObservableInt
> boolean -> ObservableBoolean
> ObservableField<T> 处理泛型 ,如 String类型的使用  ObservableField<String>
> 还有满足序列化的处理 ObservableParcelable

2）自定义监听类 ，通过继承BaseObservable

> 在设置值变化时。主动调用notifyPropertyChanged方法
> 另外 BaseObservable 它进行了线程安全的处理，对方法做了同步处理

#### 分析 ObservableInt数据监听的逻辑

    //设置值变化
    public void set(int value) {
         if (value != mValue) {//数据有变化才设置新的值
             mValue = value;
             notifyChange();
         }
     }
     //变化的逻辑主要靠 notifyChange();
     //调到 父类BaseObservable
     public void notifyChange() {
             synchronized (this) {
                 if (mCallbacks == null) {
                     return;
                 }
             }
             mCallbacks.notifyCallbacks(this, 0, null);
     }
     // mCallbacks 就是PropertyChangeRegistry mCallbacks
     //通过addOnPropertyChangedCallback进行具体回调对象的保存

## （二）视图保存数据

 通过 @ = {} 进行设置

> api已经实现的有TextViewBindingAdapter 等

它们的实现原理：

通过添加 @BindingAdapter 注解，并实现逻辑判断
```java
    //eg：
    @BindingAdapter("android:text")
    public static void setText(TextView view, CharSequence text) {
        final CharSequence oldText = view.getText();
        if (text == oldText || (text == null && oldText.length() == 0)) {
            return;
        }
        if (text instanceof Spanned) {
            if (text.equals(oldText)) {
                return; // No change in the spans, so don't set anything.
            }
        } else if (!haveContentsChanged(text, oldText)) {
            return; // No content changes, so don't set anything.
        }
        view.setText(text);
    }
```

#### 对findViewById的替换操作

直接用ViewDataBinding 可以获取id对应的 控件 ，不过id是去掉_并进行驼峰命名的新变量
#### DataBindingUtil

> 设置绑定的对象，不单可以是activity，还可以是view


[更详细的细节可以参考](https://juejin.im/post/5a55ecb6f265da3e4d7298e9#heading-41)



