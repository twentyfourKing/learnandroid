package follow.twentyfourking.greendao_generator_example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;
import org.greenrobot.greendao.generator.ToMany;
import org.greenrobot.greendao.generator.ToOne;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * <p>
 * Run it as a Java application (not Android).
 *
 * @author Markus
 */
//public class ExampleDaoGenerator {
//
//    public static void main(String[] args) throws Exception {
//        Schema schema = new Schema(1000, "follow.twentyfourking.modle");
//        schema.setDefaultJavaPackageDao("follow.twentyfourking.modle.dao");
//        schema.enableKeepSectionsByDefault();
//        addNote(schema);
//        addCustomerOrder(schema);
//
//        new DaoGenerator().generateAll(schema, "./greendao_use_generator/src/main/java");
//    }
//
//    private static void addNote(Schema schema) {
//        Entity note = schema.addEntity("Note");
//        note.addIdProperty();
//        note.addStringProperty("text").notNull();
//        note.addStringProperty("comment");
//        note.addDateProperty("date");
//    }
//
//    private static void addCustomerOrder(Schema schema) {
//        Entity customer = schema.addEntity("Customer");
//        customer.addIdProperty();
//        customer.addStringProperty("name").notNull();
//
//        Entity order = schema.addEntity("Order");
//        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
//        order.addIdProperty();
//        Property orderDate = order.addDateProperty("date").getProperty();
//        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
//        order.addToOne(customer, customerId);
//
//        ToMany customerToOrders = customer.addToMany(order, customerId);
//        customerToOrders.setName("orders");
//        customerToOrders.orderAsc(orderDate);
//    }
//
//}

public class ExampleDaoGenerator {
    public static final int SCHEMA_VERSION = 40;

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(SCHEMA_VERSION, "follow.twentyfourking.model");
        schema.setDefaultJavaPackageDao("follow.twentyfourking.model.dao");
        schema.enableKeepSectionsByDefault();
        //schema.enableActiveEntitiesByDefault();
        addEntity(schema);
        addNote(schema);
        addCustomerOrder(schema);


        //创建关系型数据库
        addRelationshipEntity(schema);

        new DaoGenerator().generateAll(schema, "./greendao_use_generator/src/main/java");
    }
    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
    }

    private static void addCustomerOrder(Schema schema) {
        Entity customer = schema.addEntity("Customer");
        customer.addIdProperty();
        customer.addStringProperty("name").notNull();

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
        order.addIdProperty();
        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(customer, customerId);

        ToMany customerToOrders = customer.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }

    private static void addEntity(Schema schema) {
        Entity entity = schema.addEntity("TaskCall");
        entity.addImport("java.util.List");
        /*entity.addImport("com.alibaba.fastjson.annotation.JSONField");
        entity.addLongProperty("_id").columnName("_id").primaryKey().autoincrement().codeBeforeGetterAndSetter("@JSONField(name = \"_id\")");*/
        entity.addLongProperty("_id").columnName("_id").primaryKey().autoincrement();
        //entity.addIntProperty("_id").columnName("_id").primaryKey().autoincrement();//XXXXXX
        entity.addStringProperty("id").columnName("id");
        entity.addStringProperty("userId").columnName("userId").notNull();
        entity.addStringProperty("title").columnName("title");
        entity.addStringProperty("remarks").columnName("remarks");
        entity.addLongProperty("remindTime").columnName("remindTime").notNull();
    }

    //创建关系型数据库
    private static void addRelationshipEntity(Schema schema) {
        //主表
        Entity entityTableFather = schema.addEntity("TableFather");
        entityTableFather.setTableName("TableFather");//不设置的话数据库里面都变成大写了
        entityTableFather.addIdProperty().autoincrement();
        entityTableFather.addStringProperty("father");

        //子表
        Entity entityTableSon = schema.addEntity("TableSon");
        entityTableSon.setTableName("TableSon");
        entityTableSon.addIdProperty().autoincrement();//子表Id
        entityTableSon.addStringProperty("son").columnName("son");
        Property fatherId = entityTableSon.addLongProperty("fatherId").columnName("fatherId").notNull().getProperty();//这种方式生成的主键实体类中叫id,在数据库文件中是_id

        //设置外键关系
        ToMany toMany = entityTableFather.addToMany(entityTableSon, fatherId);
        toMany.setName("fatherList");

        ToOne toOne = entityTableSon.addToOne(entityTableFather,fatherId);
        toOne.setName("father");
    }
}

