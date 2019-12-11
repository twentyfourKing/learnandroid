package follow.twentyfourking.learn_gradle_dependency;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import follow.twentyfourking.library1.Library1Tool;
import follow.twentyfourking.library2.Library2Tool;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //调用library1执行任务
        Library1Tool.workForMainApp();
        Library2Tool.workForLib1();
    }
}
