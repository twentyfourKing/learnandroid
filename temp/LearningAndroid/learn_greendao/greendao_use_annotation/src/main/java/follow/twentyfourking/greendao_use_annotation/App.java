package follow.twentyfourking.greendao_use_annotation;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import follow.twentyfourking.greendao_use_annotation.dao.DaoMaster;
import follow.twentyfourking.greendao_use_annotation.dao.DaoSession;

public class App extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        // regular SQLite database
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db");
        Database db = helper.getWritableDb();

        // encrypted SQLCipher database
        // note: you need to add SQLCipher to your dependencies, check the build.gradle file
        // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
        // Database db = helper.getEncryptedWritableDb("encryption-key");
        // 加密需要使用到 sqlcipher https://github.com/sqlcipher/android-database-sqlcipher
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}