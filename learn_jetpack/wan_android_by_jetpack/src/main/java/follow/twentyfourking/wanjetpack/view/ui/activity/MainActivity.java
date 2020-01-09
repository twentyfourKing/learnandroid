package follow.twentyfourking.wanjetpack.view.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import follow.twentyfourking.wanjetpack.R;
import follow.twentyfourking.wanjetpack.model.delegate.MainActivityDelegate;

public class MainActivity extends AppCompatActivity {
    private MainActivityDelegate mDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDelegate = new MainActivityDelegate();
        mDelegate.setDelegateInit(findViewById(R.id.rl_main_container), this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取数据
        mDelegate.onResume();
    }
}
