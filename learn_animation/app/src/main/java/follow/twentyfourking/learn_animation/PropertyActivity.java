package follow.twentyfourking.learn_animation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewTreeObserver;
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
    @BindView(R.id.tv_animator_10)
    TextView mTv10;
    @BindView(R.id.tv_animator_11)
    TextView mTv11;
    @BindView(R.id.tv_animator_12)
    TextView mTv12;
    @BindView(R.id.tv_animator_13)
    TextView mTv13;

    private ObjectAnimator mObjectAnimator1, mObjectAnimator2,
            mObjectAnimator3, mObjectAnimator4,
            mObjectAnimator5, mObjectAnimator6,
            mObjectAnimator7, mObjectAnimator8,
            mObjectAnimator9, mObjectAnimator10,
            mObjectAnimator11, mObjectAnimator12,
            mObjectAnimator13;

    private int mDuration = 3000;

    private float mTv12LocationY;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_name);
        mContext = this;
        ButterKnife.bind(this);


        initListener();


    }

    private void initListener() {
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTv12LocationY = mTv12.getY();
                initAnimator();
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    private void initAnimator() {
        mObjectAnimator1 = ObjectAnimator.ofInt(mTv1, "textColor",
                0xffff0000, 0xff00ff00);
        mTv1.setText("textColor");
        mObjectAnimator1.setDuration(mDuration);
        mObjectAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator1.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator1.start();

        mObjectAnimator2 = ObjectAnimator.ofFloat(mTv2, "scaleX",
                1f, 1.5f);
        mTv2.setText("scaleX");
        mObjectAnimator2.setDuration(mDuration);
        mObjectAnimator2.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator2.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator2.start();

        mObjectAnimator3 = ObjectAnimator.ofFloat(mTv3, "scaleY",
                1.0f, 1.5f);
        mTv3.setText("scaleY");
        mObjectAnimator3.setDuration(mDuration);
        mObjectAnimator3.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator3.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator3.start();

        mObjectAnimator4 = ObjectAnimator.ofFloat(mTv4, "translationX",
                0, 200);
        mTv4.setText("translationX");
        mObjectAnimator4.setDuration(mDuration);
        mObjectAnimator4.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator4.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator4.start();

        mObjectAnimator5 = ObjectAnimator.ofFloat(mTv5, "translationY",
                0, 50);
        mTv5.setText("translationY");
        mObjectAnimator5.setDuration(mDuration);
        mObjectAnimator5.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator5.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator5.start();

        mObjectAnimator6 = ObjectAnimator.ofFloat(mTv6, "rotationX",
                0, 180);
        mTv6.setText("rotationX");
        mObjectAnimator6.setDuration(mDuration);
        mObjectAnimator6.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator6.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator6.start();

        mObjectAnimator7 = ObjectAnimator.ofFloat(mTv7, "rotationY",
                0, 180);
        mTv7.setText("rotationY");
        mObjectAnimator7.setDuration(mDuration);
        mObjectAnimator7.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator7.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator7.start();

        mObjectAnimator8 = ObjectAnimator.ofFloat(mTv8, "rotation",
                0, 180);
        mTv8.setText("rotation");
        mObjectAnimator8.setDuration(mDuration);
        mObjectAnimator8.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator8.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator8.start();

        mObjectAnimator9 = ObjectAnimator.ofFloat(mTv9, "textSize",
                0, 30);
        mTv9.setText("textSize");
        mObjectAnimator9.setDuration(mDuration);
        mObjectAnimator9.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator9.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator9.start();

        mObjectAnimator10 = ObjectAnimator.ofFloat(mTv10, "textScaleX",
                1, 4);
        mTv10.setText("textScaleX");
        mObjectAnimator10.setDuration(mDuration);
        mObjectAnimator10.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator10.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator10.start();

        mObjectAnimator11 = ObjectAnimator.ofFloat(mTv11, "x",
                0, 200);
        mTv11.setText("x");
        mObjectAnimator11.setDuration(mDuration);
        mObjectAnimator11.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator11.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator11.start();

        mObjectAnimator12 = ObjectAnimator.ofFloat(mTv12, "y",
                mTv12LocationY, 100);
        mTv12.setText("y");
        mObjectAnimator12.setDuration(mDuration);
        mObjectAnimator12.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator12.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator12.start();

        mObjectAnimator13 = ObjectAnimator.ofFloat(mTv13, "alpha",
                1.0f, 0.3f);
        mTv13.setText("alpha");
        mObjectAnimator13.setDuration(mDuration);
        mObjectAnimator13.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator13.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator13.start();


    }
}
