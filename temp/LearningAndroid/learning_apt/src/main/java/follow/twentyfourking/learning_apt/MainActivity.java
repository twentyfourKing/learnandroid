package follow.twentyfourking.learning_apt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import follow.twentyfourking.lib_apt_anotation_define.AutoCreat;
import follow.twentyfourking.lib_apt_anotation_define.BindView;

@AutoCreat
public class MainActivity extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        InjectHelper.inject(this);
        Class cl = MainActivity.class;
    }
}
