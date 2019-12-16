package follow.twentyfourking.learn_animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PropertyAnimationActivity extends AppCompatActivity {
    @BindView(R.id.tv_by_xml_property_1)
    TextView mTv1;
    @BindView(R.id.tv_by_xml_property_2)
    TextView mTv2;
    @BindView(R.id.tv_by_xml_property_3)
    TextView mTv3;
    @BindView(R.id.tv_by_xml_property_4)
    TextView mTv4;
    @BindView(R.id.tv_by_xml_property_5)
    TextView mTv5;
    @BindView(R.id.tv_by_xml_property_6)
    TextView mTv6;
    @BindView(R.id.tv_by_xml_property_7)
    TextView mTv7;
    @BindView(R.id.tv_by_xml_property_8)
    TextView mTv8;
    @BindView(R.id.tv_by_xml_property_9)
    TextView mTv9;
    @BindView(R.id.tv_by_xml_property_10)
    TextView mTv10;
    @BindView(R.id.tv_by_xml_property_11)
    TextView mTv11;
    private Context myContext;

    private ObjectAnimator mAnimator1;
    private ObjectAnimator mAnimator2;
    private ValueAnimator mAnimator3;
    private AnimatorSet mAnimator4, mAnimator5, mAnimator6;
    private float mTv4LocationX, mTv4LocationY;
    private float mTv5LocationX, mTv5LocationY;

    private ObjectAnimator mObjectAnimator1, mObjectAnimator2, mObjectAnimator3, mObjectAnimator4, mObjectAnimator5;
    private ObjectAnimator mObjectAnimator6;
    private ValueAnimator mValueAnimator1,mValueAnimator2;
    private AnimatorSet mAnimatorSet1, mAnimatorSet2,mAnimatorSet3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myContext = this;
        setContentView(R.layout.activity_property);
        ButterKnife.bind(this);


        initAnimator_By_xml();

        initAnimator_By_code();
    }

    /**
     * 初始化 从xml中加载的动画设置
     */
    private void initAnimator_By_xml() {

        //<objectAnimator>标签，repeatMode 为 restart
        mAnimator1 = (ObjectAnimator) AnimatorInflater.loadAnimator(myContext, R.animator.property_animator_2);
        mAnimator1.setTarget(mTv1);

        //<objectAnimator>标签，repeatMode 为 reverse
        mAnimator2 = (ObjectAnimator) AnimatorInflater.loadAnimator(myContext, R.animator.property_animator_3);
        mAnimator2.setTarget(mTv2);

        //<animator>标签，通过监听器来实现具体值变化
        mAnimator3 = (ValueAnimator) AnimatorInflater.loadAnimator(myContext, R.animator.property_animator_4);
        mAnimator3.setTarget(mTv3);

        //<set>标签一起执行动画
        mAnimator4 = (AnimatorSet) AnimatorInflater.loadAnimator(myContext, R.animator.property_animator_5);
        mAnimator4.setTarget(mTv4);

        //<set>标签顺序执行动画
        mAnimator5 = (AnimatorSet) AnimatorInflater.loadAnimator(myContext, R.animator.property_animator_6);
        mAnimator5.setTarget(mTv5);

        //AnimatorSet <set>标签中嵌套<objectAnimator>和<set>标签
        mAnimator6 = (AnimatorSet) AnimatorInflater.loadAnimator(myContext, R.animator.property_animator_7);
        mAnimator6.setTarget(mTv6);

        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTv4LocationX = mTv4.getX();
                mTv4LocationY = mTv4.getY();
                mTv5LocationX = mTv5.getX();
                mTv5LocationY = mTv5.getY();
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    /**
     * 通过代码，操作ObjectAnimator ValueAnimator AnimatorSet 控制动画的执行
     */
    private void initAnimator_By_code() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //延时1 秒 ， 只执行一次,文字颜色变化
            mObjectAnimator1 = ObjectAnimator.ofArgb(mTv7, "textColor", 0xffF2BA38, 0xffDD70BC);
            mObjectAnimator1.setDuration(1000);
            mObjectAnimator1.setRepeatCount(0);
            mObjectAnimator1.setRepeatMode(ValueAnimator.RESTART);
            mObjectAnimator1.setInterpolator(new LinearInterpolator());
            mObjectAnimator1.setStartDelay(1000);//延时执行

            //设置ValueAnimator ,通过监听器来实现变化
            mValueAnimator1 = ValueAnimator.ofArgb(0xffF2BA38, 0xffDD70BC);
            mValueAnimator1.setDuration(1000);// 默认300 ms
            mValueAnimator1.setInterpolator(new LinearInterpolator());//默认 AccelerateDecelerateInterpolator
//            mObjectAnimator1.setRepeatCount(0);//默认 0
//            mObjectAnimator1.setRepeatMode(ValueAnimator.RESTART);// 默认 ValueAnimator.RESTART
            mValueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mTv8.setTextColor((int) animation.getAnimatedValue());
                }
            });
            mValueAnimator1.setTarget(mTv8);

            //AnimatorSet包含两个旋转动画，它们分别依次执行
            mObjectAnimator2 = ObjectAnimator.ofFloat(mTv9, "rotationX", 0, 360);
            mObjectAnimator2.setDuration(1000);
            mObjectAnimator3 = ObjectAnimator.ofFloat(mTv9, "rotationY", 0, 360);
            mObjectAnimator3.setDuration(1000);
            mAnimatorSet1 = new AnimatorSet();
            mAnimatorSet1.playSequentially(mObjectAnimator2, mObjectAnimator3);

            //AnimatorSet包含两个旋转动画，它们一起执行
            mObjectAnimator4 = ObjectAnimator.ofFloat(mTv10, "rotationX", 0, 360);
            mObjectAnimator4.setDuration(1000);
            mObjectAnimator5 = ObjectAnimator.ofFloat(mTv10, "rotationY", 0, 360);
            mObjectAnimator5.setDuration(1000);
            mAnimatorSet2 = new AnimatorSet();
            mAnimatorSet2.playTogether(mObjectAnimator4, mObjectAnimator5);

            //在 ObjectAnimator 中使用 PropertyValuesHolder
            PropertyValuesHolder propertyValuesHolder1 = PropertyValuesHolder.
                    ofFloat("translationX", 0, 50, 50,0);
            ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mTv11,propertyValuesHolder1);
            objectAnimator.setDuration(1000);
            mAnimatorSet3 = new AnimatorSet();
            mAnimatorSet3.playTogether(objectAnimator);

        }

    }

    @OnClick({
            R.id.tv_by_xml_property_1,
            R.id.tv_by_xml_property_2,
            R.id.tv_reverse,
            R.id.tv_by_xml_property_3,
            R.id.tv_by_xml_property_4,
            R.id.tv_by_xml_property_5,
            R.id.tv_by_xml_property_6,
            R.id.tv_by_xml_property_7,
            R.id.tv_by_xml_property_8,
            R.id.tv_by_xml_property_9,
            R.id.tv_by_xml_property_10,
            R.id.tv_by_xml_property_11,
            R.id.tv_by_xml_property_12,
            R.id.tv_start_property
    })
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_by_xml_property_1) {
            mAnimator1.start();
        } else if (id == R.id.tv_by_xml_property_2) {
            mAnimator2.start();
        } else if (id == R.id.tv_reverse) {
            mTv1.setAlpha(1.0f);
            mTv2.setAlpha(1.0f);

            mTv4.setAlpha(1.0f);
            mTv4.setX(mTv4LocationX);
            mTv4.setY(mTv4LocationY);

            mTv5.setAlpha(1.0f);
            mTv5.setX(mTv5LocationX);
            mTv5.setY(mTv5LocationY);

            mTv7.setTextColor(getResources().getColor(R.color.app_black));

            mTv8.setTextColor(getResources().getColor(R.color.app_black));


        } else if (id == R.id.tv_by_xml_property_3) {
            mAnimator3.start();
            mAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Log.d("TTT", " value = " + animation.getAnimatedValue());
                    mTv3.setAlpha((float) animation.getAnimatedValue());
                }
            });
        } else if (id == R.id.tv_by_xml_property_4) {
            mAnimator4.start();
        } else if (id == R.id.tv_by_xml_property_5) {
            mAnimator5.start();
        } else if (id == R.id.tv_by_xml_property_6) {
            mAnimator6.start();
        } else if (id == R.id.tv_by_xml_property_7) {
            mObjectAnimator1.start();
        } else if (id == R.id.tv_by_xml_property_8) {
            mValueAnimator1.start();
        } else if (id == R.id.tv_by_xml_property_9) {
            mAnimatorSet1.start();
        } else if (id == R.id.tv_by_xml_property_10) {
            mAnimatorSet2.start();
        }else if(id == R.id.tv_by_xml_property_11){
            mAnimatorSet3.start();
        }else if(id == R.id.tv_by_xml_property_12){

        }else if(id == R.id.tv_start_property){
            startActivity(new Intent(this,PropertyActivity.class));
        }

    }
}
