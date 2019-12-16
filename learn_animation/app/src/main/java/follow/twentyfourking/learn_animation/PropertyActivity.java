package follow.twentyfourking.learn_animation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PropertyActivity extends AppCompatActivity {
    @BindView(R.id.tv_animator_1)
    TextView mTv1;
    @BindView(R.id.tv_animator_2)
    TextView mTv2;
    @BindView(R.id.tv_animator_3)
    TextView mTv3;
    @BindView(R.id.tv_animator_4)
    TextView mTv4;
    @BindView(R.id.tv_animator_5)
    TextView mTv5;
    @BindView(R.id.tv_animator_6)
    TextView mTv6;
    @BindView(R.id.tv_animator_7)
    TextView mTv7;
    @BindView(R.id.tv_animator_8)
    TextView mTv8;
    @BindView(R.id.tv_animator_9)
    TextView mTv9;

    private ObjectAnimator mObjectAnimator1, mObjectAnimator2,
            mObjectAnimator3, mObjectAnimator4,
            mObjectAnimator5, mObjectAnimator6,
            mObjectAnimator7, mObjectAnimator8,
            mObjectAnimator9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_name);
        ButterKnife.bind(this);

        initAnimator();
    }

    private void initAnimator() {
        mObjectAnimator1 = ObjectAnimator.ofInt(mTv1, "textColor",
                0xffff0000, 0xff00ff00);
        mTv1.setText("textColor");
        mObjectAnimator1.setDuration(600);
        mObjectAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator1.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator1.start();

        mObjectAnimator2 = ObjectAnimator.ofFloat(mTv2, "scaleX",
                1f, 1.5f);
        mObjectAnimator2.setDuration(600);
        mObjectAnimator2.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator2.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator2.start();

        mObjectAnimator3 = ObjectAnimator.ofFloat(mTv3, "scaleY",
                1.0f, 1.5f);
        mTv3.setText("scaleY");
        mObjectAnimator3.setDuration(600);
        mObjectAnimator3.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator3.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator3.start();

        mObjectAnimator4 = ObjectAnimator.ofFloat(mTv4, "translationX",
                0, 50);
        mTv4.setText("translationX");
        mObjectAnimator4.setDuration(600);
        mObjectAnimator4.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator4.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator4.start();

        mObjectAnimator5 = ObjectAnimator.ofFloat(mTv5, "translationY",
                0, 50);
        mTv5.setText("translationY");
        mObjectAnimator5.setDuration(600);
        mObjectAnimator5.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator5.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator5.start();

        mObjectAnimator6 = ObjectAnimator.ofFloat(mTv6, "rotationX",
                0, 180);
        mTv6.setText("rotationX");
        mObjectAnimator6.setDuration(600);
        mObjectAnimator6.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator6.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator6.start();

        mObjectAnimator7 = ObjectAnimator.ofFloat(mTv7, "rotationY",
                0, 180);
        mTv7.setText("rotationY");
        mObjectAnimator7.setDuration(600);
        mObjectAnimator7.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator7.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator7.start();

        mObjectAnimator8 = ObjectAnimator.ofFloat(mTv8, "rotation",
                0, 180);
        mTv8.setText("rotation");
        mObjectAnimator8.setDuration(600);
        mObjectAnimator8.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator8.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator8.start();

        mObjectAnimator9 = ObjectAnimator.ofFloat(mTv9, "textSize",
                0, 50);
        mTv9.setText("rotation");
        mObjectAnimator9.setDuration(600);
        mObjectAnimator9.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator9.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator9.start();
    }
}
