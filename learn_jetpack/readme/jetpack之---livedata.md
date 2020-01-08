LiveData
====

### LiveData

一）oberserve()是入口 构建观察关系

两个参数(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer)

(1)owner是LifecycleOwner的实现者，就是为了得到Lifecycle的继承类LifecycleRegistry,该类
是监控activity和fragment生命周期变化的核心;

(2)oserver是Observer的具体实现者,将observer保存在LiveData的成员变量
SafeIterableMap<Observer<? super T>, ObserverWrapper> mObservers =
            new SafeIterableMap<>();中

二）setValue变化的发起点

值变化->分发变化的值到具体的观察者->数据具体的变化逻辑

当值变化值：执行dispatchingValue->遍历mObservers 定义观察者，并进行通知

observer.mActive 这个状态标识与activity、fragment的生命周期状态。有关系的，当LifecycleRegistry标识的生命周期非DESTROYED时，就会去修改mActive状态。

具体的逻辑由ObserverWithState.dispatchEvent方法通过调用mLifecycleObserver.onStateChanged方法完成mLifecycleObserver就是ObserverWrapper，正是oberserve()中owner.getLifecycle().addObserver(wrapper)传入的wrapper， wrapper正是LifecycleBoundObserver extends ObserverWrapper
```java
    private void considerNotify(ObserverWrapper observer) {
        if (!observer.mActive) {
        //只有当前activity、fragment的状态非DESTROYED，并且是在STARTED之后
        //才将值变化，分发给具体的观察者

            return;
        }
        if (!observer.shouldBeActive()) {
            observer.activeStateChanged(false);
            return;
        }
        if (observer.mLastVersion >= mVersion) {
            return;
        }
        observer.mLastVersion = mVersion;
        //发送到具体的观察者，处理后续逻辑
        observer.mObserver.onChanged((T) mData);
    }
```

### Lifecycle

LiveData对数据变化的监听在LiveData这个类中就已经实现了

大致流程就是：

先构建观察者，然后保存观察到LiveData的map中，当值有变化时遍历map然后通知具体的观察者
LiveData的另一个特性监听Activity、Fragment的生命周期，

它的实现是依赖于Lifecycle
主要类和接口有：FragmentActivity（实现了LifecycleOwner接口）、LifecycleRegistry（Lifecycle的继承类当FragmentActivity生命周期变化时，都会通过mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.xx);发送具体状态，然后由LifecycleRegistry.moveToState将状态进行保存，以便LiveData在数据变化时查询状态

同时也会对特殊生命周期进行值改变操作