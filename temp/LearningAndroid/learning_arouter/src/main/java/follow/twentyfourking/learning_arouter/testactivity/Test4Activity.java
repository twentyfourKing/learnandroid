package follow.twentyfourking.learning_arouter.testactivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import follow.twentyfourking.learning_arouter.R;

@Route(path = "/test/activity4")
public class Test4Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_test1);
        TextView textView = (TextView) findViewById(R.id.test);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = stringBuilder.append("I am ");
        String str2 = Test4Activity.class.getName();
        String str1 = stringBuilder.append(str2).toString();
        textView.setText(str1);
        Intent intent = getIntent();
        str1 = "extra";
        String str3 = intent.getStringExtra(str1);
        boolean bool = TextUtils.isEmpty(str3);
        if (!bool) {
            TextView textView1 = (TextView) findViewById(R.id.test2);
            textView1.setText(str3);
        }
    }
}
