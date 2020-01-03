package follow.twentyfourking.learn_view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import follow.twentyfourking.learn_view.R;
import follow.twentyfourking.learn_view.activity.TestActivity1;
import follow.twentyfourking.learn_view.activity.TestActivity2;

public class MainActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({
            R.id.tv_test_1,
            R.id.tv_test_2
    })
    public void onClick(View view){
        int id = view.getId();
        if(id == R.id.tv_test_1){
            startActivity(new Intent(this, TestActivity1.class));
        }else if(id == R.id.tv_test_2){
            startActivity(new Intent(this, TestActivity2.class));
        }
    }
}
