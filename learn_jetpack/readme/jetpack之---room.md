room的基本使用
===

>学习于google的demo

##关于Room依赖于sqlite的分析

从room数据的构建开始

1) Room.databaseBuilder.build() 入参中有的继承类 RoomDatabase

2) RoomDatabase.build() 通过反射 构建 Room.getGeneratedImplementation(mDatabaseClass, DB_IMPL_SUFFIX);

3) mDatabaseClass就是真正的实现类。它是用@Database标注的类，通过apt工具自动生成了xxDatabase_impl类

4) 从db.init中会构建SupportSQLiteOpenHelper

具体的实现在xxDatabase_impl的createOpenHelper方法中
FrameworkSQLiteOpenHelper是SupportSQLiteOpenHelper的具体实现者，
FrameworkSQLiteOpenHelper对sqlite数据库操作靠的是
OpenHelper，而OpenHelper是SQLiteOpenHelper的子类，从这里我们就可以发现，
room底层逻辑还是使用的sqlite的相关api进行的操作


### demo分析

> 说明：下面的总结是对[谷歌原始demo](https://github.com/android/architecture-components-samples)的研究和分析

#### src/sqlite源
是通过android.jar包中sqlite的原生api构建数据库

主要涉及的功能类有：

SQLiteOpenHelper 主要功能是构建数据库、建表、数据库更新

SQLiteDatabase 是具体的数据库对象

在进行数据的增删改查时都需要先获取SQLiteDatabase实例，
```text
    1)查询数据：先获取可读数据库实例SQLiteOpenHelper.getReadableDatabase()

    2)增删改：先获取可写数据库实例 SQLiteOpenHelper.getWritableDatabase()
```


数据库版本为1

 "CREATE TABLE " + users+ " (" +
                     userid + " INTEGER  PRIMARY KEY NOT NULL," +
                     username + " TEXT )"


#### src/room src/room2 src/room3

这3个源就是为了演示数据库的迁移（数据库表单变化，表单类型变换，数据迁移等）

> room 数据库版本更新为2 ，主要是为了将用sqlite构建的数据迁移到room

> room2 数据库版本更新为3 ，数据库是在users中添加了一个column数据 last_update INTEGER

> room3 数据库版本更新为4 ，构建新表

 "CREATE TABLE users_new (userid TEXT NOT NULL,"
                             + "username TEXT,"
                             + "last_update INTEGER,"
                             + "PRIMARY KEY(userid))"
  并且userid不再使用随机数构建，改为了UUID.randomUUID().toString();

#### room
在apt构建代码过程中，会创建room_master_table 的表来保存数据的hash认证值

#### src/room

迁移测试中，正如demo中的描述一样

    //Room uses an own database hash to uniquely identify the database
    // Since version 1 does not use Room, it doesn't have the database hash associated.
    // By implementing a Migration class, we're telling Room that it should use the data
    // from version 1 to version 2.
    // If no migration is provided, then the tables will be dropped and recreated.
    // Since we didn't alter the table, there's nothing else to do here.


如果有数据库版本的变化，但是又没有迁移策略那么就会报错：

    java.lang.IllegalStateException: A migration from 1 to 2 was required but not found.
    Please provide the necessary Migration path via
    RoomDatabase.Builder.addMigration(Migration ...) or allow for destructive
    migrations via one of the
    RoomDatabase.Builder.fallbackToDestructiveMigration* methods.

    fallbackToDestructiveMigration() //重建数据库表单

#### src/room2
新数据库版本为3 在users表中添加了一个列字段

数据库版本2中的数据还是迁移到了数据库版本3

#### src/room3
数据库版本为4  ，在users表中改变了原始column的属性，
数据迁移的逻辑是：先构建新表users_new  ,然后将原始表users中的数据我们需要
的column选择并插入到users_new中，然后修改users_new的表名为users，完成数据的迁移

> 数据库版本的迁移可以一个跨度也可以一次性到位

