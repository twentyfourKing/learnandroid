package follow.twentyfourking.learning_arouter.testactivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;

import follow.twentyfourking.learning_arouter.R;

public class Test3Activity extends AppCompatActivity {
    @Autowired
    int age;
    @Autowired
    boolean girl;
    @Autowired
    long high;
    @Autowired
    String name;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_test1);
        Object[] arrayOfObject = new Object[4];
        arrayOfObject[0] = name;
        arrayOfObject[1] = Integer.valueOf(this.age);
        arrayOfObject[2] = Boolean.valueOf(this.girl);
        arrayOfObject[3] = Long.valueOf(high);
        String str3 = String.format("name=%s, age=%s, girl=%s, high=%s", arrayOfObject);
        TextView textView = (TextView) findViewById(R.id.test);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = stringBuilder.append("I am ");
        textView.setText(stringBuilder.append(Test3Activity.class.getName()).toString());
        ((TextView) findViewById(R.id.test2)).setText(str3);
    }
}
