package follow.twentyfourking.lookscaner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import follow.twentyfourking.lookscaner.scaner.AndroidObjectBrowser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new AndroidObjectBrowser().start(this);
    }
}
