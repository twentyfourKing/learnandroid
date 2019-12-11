#记录

## implementation
> 依赖隔离，依赖不会传递
> 在编译期间不可以用，在运行期间可以使用

## api
> 依赖会传递
> 编译和运行期间都可以使用

tips：
对于 implementation 和 api 的使用，一般我们都使用implementation，加快编译项目构建速度，
如果全部使用api方式引入依赖，只要依赖链上的一个依赖变化了，所有依赖项都会进行编译
如果全部使用implementation方式引入，那么依赖链上的一个依赖变化了，所影响到的依赖只局限
于有直接关系的依赖项


> testImplementation  只在单元测试代码的编译以及最终打包测试apk时有效
> debugImplementation  只在debug模式的编译和最终的debug apk打包时有效
> releaseImplementation  仅仅针对Release 模式的编译和最终的Release apk打包
> compileOnly 只在编译期间有作用，不会参与打包
> runtimeOnly 只参与打包，在编译期间没作用
> annotationProcessor 引入注解编译依赖库，使用该关键字是为了，
将正常编译和注解编译的依赖库分开，提高性能
