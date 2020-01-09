## TabLayout的使用

在TabLayout 、ViewPager、Fragment配合使用时

1) TabLayout的tab项，可以自定义View，然后通过setCustomView()进行设置

2) ViewPager与TabLayout的联动关系，官方api提供了setupWithViewPager()方法

setupWithViewPager的内部原理其实就是分别为ViewPager和TabLayout设置了监听者
在各自变化回调方法中，设置联动效果

为了灵活性，我们根据setupWithViewPager里面构建联动的方式，自己为ViewPager和TabLayout
设置联动关系

3)  
```java
    //为ViewPager设置监听
    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        //...省略其他代码
        @Override
        public void onPageSelected(int position) {
            //
            mTabLayout.selectTab(mTabLayout.getTabAt(position), true);
        }
        //...
    }
```
```java
    //为TabLayout设置监听
    mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
        //...省略其他代码
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mViewPager.setCurrentItem(tab.getPosition());
        }
        //...
    }
```
    