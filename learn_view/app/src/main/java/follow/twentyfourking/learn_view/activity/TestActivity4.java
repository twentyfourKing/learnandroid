package follow.twentyfourking.learn_view.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import follow.twentyfourking.learn_view.R;
import follow.twentyfourking.learn_view.view.DrawView;

public class TestActivity4 extends AppCompatActivity {
    @BindView(R.id.draw_1)
    DrawView mDraw1;
    @BindView(R.id.draw_2)
    DrawView mDraw2;
    @BindView(R.id.draw_3)
    DrawView mDraw3;
    @BindView(R.id.draw_4)
    DrawView mDraw4;
    @BindView(R.id.draw_5)
    DrawView mDraw5;
    @BindView(R.id.draw_6)
    DrawView mDraw6;
    @BindView(R.id.draw_7)
    DrawView mDraw7;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_4);
        ButterKnife.bind(this);
        init();
    }


    private void init(){
        mDraw1.setType(1);
        mDraw2.setType(2);
        mDraw3.setType(3);
        mDraw4.setType(4);
        mDraw5.setType(5);
    }
}
