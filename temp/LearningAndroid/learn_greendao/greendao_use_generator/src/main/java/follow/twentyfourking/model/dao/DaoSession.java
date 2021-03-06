package follow.twentyfourking.model.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import follow.twentyfourking.model.TaskCall;
import follow.twentyfourking.model.Note;
import follow.twentyfourking.model.Customer;
import follow.twentyfourking.model.Order;
import follow.twentyfourking.model.TableFather;
import follow.twentyfourking.model.TableSon;

import follow.twentyfourking.model.dao.TaskCallDao;
import follow.twentyfourking.model.dao.NoteDao;
import follow.twentyfourking.model.dao.CustomerDao;
import follow.twentyfourking.model.dao.OrderDao;
import follow.twentyfourking.model.dao.TableFatherDao;
import follow.twentyfourking.model.dao.TableSonDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig taskCallDaoConfig;
    private final DaoConfig noteDaoConfig;
    private final DaoConfig customerDaoConfig;
    private final DaoConfig orderDaoConfig;
    private final DaoConfig tableFatherDaoConfig;
    private final DaoConfig tableSonDaoConfig;

    private final TaskCallDao taskCallDao;
    private final NoteDao noteDao;
    private final CustomerDao customerDao;
    private final OrderDao orderDao;
    private final TableFatherDao tableFatherDao;
    private final TableSonDao tableSonDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        taskCallDaoConfig = daoConfigMap.get(TaskCallDao.class).clone();
        taskCallDaoConfig.initIdentityScope(type);

        noteDaoConfig = daoConfigMap.get(NoteDao.class).clone();
        noteDaoConfig.initIdentityScope(type);

        customerDaoConfig = daoConfigMap.get(CustomerDao.class).clone();
        customerDaoConfig.initIdentityScope(type);

        orderDaoConfig = daoConfigMap.get(OrderDao.class).clone();
        orderDaoConfig.initIdentityScope(type);

        tableFatherDaoConfig = daoConfigMap.get(TableFatherDao.class).clone();
        tableFatherDaoConfig.initIdentityScope(type);

        tableSonDaoConfig = daoConfigMap.get(TableSonDao.class).clone();
        tableSonDaoConfig.initIdentityScope(type);

        taskCallDao = new TaskCallDao(taskCallDaoConfig, this);
        noteDao = new NoteDao(noteDaoConfig, this);
        customerDao = new CustomerDao(customerDaoConfig, this);
        orderDao = new OrderDao(orderDaoConfig, this);
        tableFatherDao = new TableFatherDao(tableFatherDaoConfig, this);
        tableSonDao = new TableSonDao(tableSonDaoConfig, this);

        registerDao(TaskCall.class, taskCallDao);
        registerDao(Note.class, noteDao);
        registerDao(Customer.class, customerDao);
        registerDao(Order.class, orderDao);
        registerDao(TableFather.class, tableFatherDao);
        registerDao(TableSon.class, tableSonDao);
    }
    
    public void clear() {
        taskCallDaoConfig.clearIdentityScope();
        noteDaoConfig.clearIdentityScope();
        customerDaoConfig.clearIdentityScope();
        orderDaoConfig.clearIdentityScope();
        tableFatherDaoConfig.clearIdentityScope();
        tableSonDaoConfig.clearIdentityScope();
    }

    public TaskCallDao getTaskCallDao() {
        return taskCallDao;
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public TableFatherDao getTableFatherDao() {
        return tableFatherDao;
    }

    public TableSonDao getTableSonDao() {
        return tableSonDao;
    }

}
