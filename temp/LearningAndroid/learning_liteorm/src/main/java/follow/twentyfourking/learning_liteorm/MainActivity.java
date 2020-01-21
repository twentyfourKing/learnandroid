package follow.twentyfourking.learning_liteorm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.litesuits.orm.LiteOrm;

public class MainActivity extends AppCompatActivity {
    static LiteOrm liteOrm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getApplication().getDatabasePath("liteorm.db").getPath();

        if (liteOrm == null) {
            liteOrm = LiteOrm.newSingleInstance(this, "liteorm.db");
        }
        liteOrm.setDebugged(true); // open the log
    }
}
