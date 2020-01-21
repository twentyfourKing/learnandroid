package follow.twentyfourking.learning_arouter.testactivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import follow.twentyfourking.learning_arouter.R;

@Route(path = "/test/activity2")
public class Test2Activity extends AppCompatActivity {
    @Autowired
    String key1;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_test2);
        Intent intent = getIntent();
        String str2 = intent.getStringExtra("key1");
        boolean bool = TextUtils.isEmpty(str2);
        if (!bool) {
            StringBuilder stringBuilder = new StringBuilder();
            String str = stringBuilder.append("exist param :").append(str2).toString();
            Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
            toast.show();
        }
        setResult(999);
    }
}
