package follow.twentyfourking.learn_jetpack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String value = "123";
        long a = 123;
        if(Long.valueOf(value).longValue() == a){
            Log.d("TTT","真");
        }else{
            Log.d("TTT","不真");
        }

    }
}
