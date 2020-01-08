ViewModel
===

### 状态保存和恢复

当界面状态配置变化或者屏幕横竖屏变化时对界面数据的处理

```text
    保存数据 >> onRetainNonConfigurationInstance()

    恢复数据 >> getLastNonConfigurationInstance()
```

在appcompat包中，具体的实现逻辑是在ComponentActivity中

```java
    //ComponentActivity.java
    //保存操作
    public final Object onRetainNonConfigurationInstance() {
        Object custom = onRetainCustomNonConfigurationInstance();
        ViewModelStore viewModelStore = mViewModelStore;
        if (viewModelStore == null) {
            // No one called getViewModelStore(), so see if there was an existing
            // ViewModelStore from our last NonConfigurationInstance
            NonConfigurationInstances nc =
                    (NonConfigurationInstances) getLastNonConfigurationInstance();
            if (nc != null) {
                viewModelStore = nc.viewModelStore;
            }
        }
        if (viewModelStore == null && custom == null) {
            return null;
        }
        NonConfigurationInstances nci = new NonConfigurationInstances();
        nci.custom = custom;
        nci.viewModelStore = viewModelStore;
        return nci;
    }
```
```text
    数据的保存和恢复是与Activity的生命周期有关系的

    Activity-> 启动 performLaunchActivity->会通过getLastNonConfigurationInstance()获取保存的数据，并进行数据恢复

    Activity-> 销毁 performDestroyActivity->会通过onRetainNonConfigurationInstance()方法保存数据
```
### ViewModel的使用

创建出来的ViewModel是保存在ViewModelStore的HashMap中,当销毁的时候在执行onRetainNonConfigurationInstance()如上代码所示将保存ViewModel数据的

ViewModelStore存放在了NonConfigurationInstances中;

当销毁重新启动的时候，再次使用
```java

    ViewModelProvider.Factory factory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
    ViewModelProvider viewModelProvider = new ViewModelProvider(this, factory);

```
> 说明

ViewModelProvider的构造函数入参是 ViewModelStoreOwner 和ViewModelProvider.Factory具体调用如下：
```java
     this(owner.getViewModelStore(), factory);
```

owner其实就是Activity本身，因为在父类ComponentActivity中实现了ViewModelStoreOwner

owner.getViewModelStore()获取的就是ViewModelStore。具体代码：

```java
    public ViewModelStore getViewModelStore() {
        if (getApplication() == null) {
            throw new IllegalStateException("Your activity is not yet attached to the "
                    + "Application instance. You can't request ViewModel before onCreate call.");
        }
        //说明
        if (mViewModelStore == null) {
            NonConfigurationInstances nc =
                    (NonConfigurationInstances) getLastNonConfigurationInstance();
            if (nc != null) {
                // Restore the ViewModelStore from NonConfigurationInstances
                mViewModelStore = nc.viewModelStore;
            }
            if (mViewModelStore == null) {
                mViewModelStore = new ViewModelStore();
            }
        }
        return mViewModelStore;
    }
```
> 说明

如果做了状态数据保存，从之前保存的数据中找到ViewModelStore；如果没有缓存数据，那么就直接新建一个ViewModelStore

在拿到ViewModelStore后并创建好ViewModelProvider之后，然后根据具体的ViewModel，从
ViewModelStore中根据key来获取ViewModel

ViewModelProvider获取ViewModel的方法有两个get方法

（1）直接用ViewModel的Class进行查询或者保存，key是默认的
```java
    public <T extends ViewModel> T get(@NonNull Class<T> modelClass) {
        String canonicalName = modelClass.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
        }
        return get(DEFAULT_KEY + ":" + canonicalName, modelClass);
    }
```

(2) 根据指定的key进行获取或者保存。

指定key的方式可以保存同一个ViewModel的不同实例;如果不指定key使用默认的key，那么只能保存相同实例
```java
    public <T extends ViewModel> T get(@NonNull String key, @NonNull Class<T> modelClass) {
         ViewModel viewModel = mViewModelStore.get(key);
         if (modelClass.isInstance(viewModel)) {
             //noinspection unchecked
             return (T) viewModel;
         } else {
             //noinspection StatementWithEmptyBody
             if (viewModel != null) {
                 // TODO: log a warning.
             }
         }
         if (mFactory instanceof KeyedFactory) {
             viewModel = ((KeyedFactory) (mFactory)).create(key, modelClass);
         } else {
             viewModel = (mFactory).create(modelClass);
         }
         mViewModelStore.put(key, viewModel);
         //noinspection unchecked
         return (T) viewModel;
     }
```

