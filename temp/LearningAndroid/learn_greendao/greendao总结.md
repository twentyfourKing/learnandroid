#greendao
orm（Object Relational Mapping 对象关系映射）数据库
greendao有两种使用方式
> 1. 通过generator方式生成代码模板
> 通过java的main方法执行代码生成的逻辑，定义生成的位置，
> 具体的实体类（具体的表单）

> 2. 通过注解的方式在编译器生成代码模板
> 主要的注解有：@Entity (定义对象实体，即数据库的表信息)
> @Id (定义表的主键信息,还可以定义综合主键) 
> @NotNull （定义字段数据不能为空）
> @Index (定义索引数据)
> @Convert (定义类型的转换，用于存储复杂的数据形式，常用的将复杂的数据序列化为json)

在定义好实体（表）数据之后，编译一下工程，就会使用plugin: 'org.greenrobot.greendao'
插件解析源码根据注解生成代码（代码位置 build/generated/source/greendao）

自动生成的代码除了表文件(xxDao)以外还有，DaoMaster、DaoSession
DaoMaster 继承了DatabaseOpenHelper实现了onCreate和onUpgrade

一般在application中初始化数据库：
DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db");
当然还可以进行数据加密，加密需要结合sqlcipher（https://github.com/sqlcipher/android-database-sqlcipher）
> tips: 对数据库框架的解析 https://juejin.im/entry/58f599f261ff4b005806d43a
> 3.2.2使用参考：https://blog.csdn.net/yechaoa/article/details/83543013

#源码流程分析

1、从初始化数据库开始

    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "数据库名称");
    //数据库未创建时会，调用DatabaseOpenHelper 的onCreate创建表单
    
    
