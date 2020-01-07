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
    @BindView(R.id.draw_1_1)
    DrawView mDraw11;
    @BindView(R.id.draw_1_2)
    DrawView mDraw12;

    @BindView(R.id.draw_2)
    DrawView mDraw2;
    @BindView(R.id.draw_2_1)
    DrawView mDraw21;
    @BindView(R.id.draw_2_2)
    DrawView mDraw22;
    @BindView(R.id.draw_2_3)
    DrawView mDraw23;
    @BindView(R.id.draw_2_4)
    DrawView mDraw24;


    @BindView(R.id.draw_3)
    DrawView mDraw3;
    @BindView(R.id.draw_3_1)
    DrawView mDraw31;


    @BindView(R.id.draw_4)
    DrawView mDraw4;
    @BindView(R.id.draw_4_1)
    DrawView mDraw41;

    @BindView(R.id.draw_5)
    DrawView mDraw5;

    @BindView(R.id.draw_6)
    DrawView mDraw6;
    @BindView(R.id.draw_6_1)
    DrawView mDraw61;

    @BindView(R.id.draw_7)
    DrawView mDraw7;

    @BindView(R.id.draw_8)
    DrawView mDraw8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_4);
        ButterKnife.bind(this);
        init();
    }


    private void init() {
        mDraw1.setType(1);
        mDraw11.setType(11);
        mDraw12.setType(12);


        mDraw2.setType(2);
        mDraw21.setType(21);
        mDraw22.setType(22);
        mDraw23.setType(23);
        mDraw24.setType(24);


        mDraw3.setType(3);
        mDraw31.setType(31);

        mDraw4.setType(4);
        mDraw41.setType(41);

        mDraw5.setType(5);
        mDraw6.setType(6);
        mDraw61.setType(61);

        mDraw7.setType(7);
        mDraw8.setType(8);
    }
}
