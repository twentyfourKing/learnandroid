package follow.twentyfourking.wanjetpack.viewmodel.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ArticlePageEntity.class}, version = 1)
public abstract class ArticleDatabase extends RoomDatabase {
    private static ArticleDatabase INSTANCE;

    public abstract ArticleDao getDao();

    private static final Object sLock = new Object();
    public static ArticleDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, ArticleDatabase.class, "wan.db")
                        .build();
            }
        }
        return INSTANCE;
    }
}
