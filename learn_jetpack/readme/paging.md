# paging

从功能上来看

paging主要通过数据源DataSorce的变化，整理出PageList数据，
将PageList的数据变化反馈到PageListAdapter，最后表现在RecycleView的界面的变化

其中几个关键的类：

#### DataSource 

是数据的来源（不论是通过本地数据库还是网络请求）
它有几个子类分别表示数据获取和加载方式

#### PageKeyedDataSource
#### ItemKeyedDataSource
#### PositionalDataSource

数据的获取分为预加载和增量加载

预加载就是在构建 ViewModel时，构建LivePagedListBuilder对象，创建LiveData的过程中，会
调到DataSource的loadInitial方法（进行数据的具体获取，从网络或者从数据库等）
这里需要异步回调，当获取到数据时，会使用loadInitial方法的参数，LoadInitialCallback callback
回调到onResult方法

当我们滑动RecycleView 进行数据浏览时，界面刷新会调用到getItem方法，这里会调到PageList的loadAround
进行是否需要再加载数据的逻辑判断，如果需要加载那么就会调到DataSource的loadAfter等方法，再次获取数据
只要滑动的item大于了已经加载的项就会自动请求数据
因为ViewModel构建中返回的是LiveData，对这个LiveData添加观察者，当数据变化时会回调，
并可以用adapter.submitList方法激发界面的变化

自己构建流程就是：
```text
    
    1、首先构建自己的DataSource根据自己的数据加载规则继承对应的父类进行操作，并定义数据加载规则
    2、构建DataSource.Factory
    3、构建ViewModel，在LivePageListBuider中根据DataFactory和配置参数，构建出LiveData
    4、利用ViewModel，得到LiveData并构建它的观察者，一次刷新界面
    5、构建PageListAdapter,用来承载RecycleView的数据项加载规则
```