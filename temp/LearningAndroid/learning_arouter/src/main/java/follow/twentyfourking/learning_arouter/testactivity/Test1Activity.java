package follow.twentyfourking.learning_arouter.testactivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.List;
import java.util.Map;

import follow.twentyfourking.learning_arouter.R;
import follow.twentyfourking.learning_arouter.testinject.TestObj;
import follow.twentyfourking.learning_arouter.testinject.TestParcelable;
import follow.twentyfourking.learning_arouter.testinject.TestSerializable;
import follow.twentyfourking.learning_arouter.testservice.HelloService;


@Route(path = "/test/activity1")
public class Test1Activity extends AppCompatActivity {
    int age = 10;
    char ch = 'A';
    double dou = 12.01D;
    float fl = 12.0F;
    boolean girl;
    int height = 175;

    @Autowired(name = "/get/service/orig")
    HelloService helloService;

    @Autowired(name = "/get/service/red")
    HelloService helloServiceRed;

    private long high;
    @Autowired
    Map map;

    String name = "jack";
    @Autowired
    TestObj obj;
    @Autowired
    List objList;
    @Autowired
    TestParcelable pac;
    @Autowired
    TestSerializable ser;
    @Autowired
    String url;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_test1);
        ARouter.getInstance().inject(this);
        Object[] arrayOfObject = new Object[14];
        arrayOfObject[0] = this.name;
        arrayOfObject[1] = Integer.valueOf(this.age);
        arrayOfObject[2] = Integer.valueOf(this.height);
        arrayOfObject[3] = Boolean.valueOf(this.girl);
        arrayOfObject[4] = Long.valueOf(this.high);
        arrayOfObject[5] = this.url;
        arrayOfObject[6] = this.ser;
        arrayOfObject[7] = this.pac;
        arrayOfObject[8] = this.obj;
        arrayOfObject[9] = Character.valueOf(this.ch);
        arrayOfObject[10] = Float.valueOf(this.fl);
        arrayOfObject[11] = Double.valueOf(this.dou);
        arrayOfObject[12] = this.objList;
        arrayOfObject[13] = this.map;
        String str4 = String.format("name=%s,\n age=%s, \n height=%s,\n girl=%s,\n high=%s,\n url=%s,\n ser=%s,\n pac=%s,\n obj=%s \n ch=%s \n fl = %s, \n dou = %s, \n objList=%s, \n map=%s", arrayOfObject);
        this.helloService.sayHello("Hello moto.");
        this.helloServiceRed.sayHello("这个是通过red打招呼");
        TextView textView = (TextView) findViewById(R.id.test);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = stringBuilder.append("I am ");
        String str1 = stringBuilder.append(Test1Activity.class.getName()).toString();
        textView.setText(str1);
        ((TextView) findViewById(R.id.test2)).setText(str4);
    }
}
