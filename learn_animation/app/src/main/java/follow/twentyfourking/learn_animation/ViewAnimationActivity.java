package follow.twentyfourking.learn_animation;

import android.animation.AnimatorSet;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewAnimationActivity extends AppCompatActivity {
    @BindView(R.id.tv_animation_1)
    TextView mTv1;
    @BindView(R.id.img_1)
    ImageView mImg1;
    @BindView(R.id.img_2)
    ImageView mImg2;
    @BindView(R.id.img_3)
    ImageView mImg3;
    @BindView(R.id.img_4)
    ImageView mImg4;
    @BindView(R.id.tv_animation_alpha)
    TextView mTvAlpha;
    @BindView(R.id.tv_animation_translate)
    TextView mTvTranslate;
    @BindView(R.id.tv_animation_scale)
    TextView mTvScale;
    @BindView(R.id.tv_animation_rotation)
    TextView mTvRotation;
    @BindView(R.id.tv_animation_set)
    TextView mTvSet;
    @BindView(R.id.tv_animation_tween)
    TextView mTvTween;

    @BindView(R.id.tv_animation_alpha_code)
    TextView mTvAlphaCode;
    @BindView(R.id.tv_animation_translate_code)
    TextView mTvTranslateCode;
    @BindView(R.id.tv_animation_scale_code)
    TextView mTvScaleCode;
    @BindView(R.id.tv_animation_rotation_code)
    TextView mTvRotationCode;
    @BindView(R.id.tv_animation_set_code)
    TextView mTvSetCode;


    public static String TAG = "ViewAnimationActivity";
    private AnimationDrawable mAnimationDrawable;
    private AnimationDrawable mAnimationDrawable2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        frameAnimationByXml();

        frameAnimationByCode();
    }

    private void frameAnimationByXml() {
        //1) 用代码配置
//        mImg1.setBackgroundResource(R.drawable.frame_list_animation);

        //2) 在xml中已经将drawable配置为ImageView的背景了
        mAnimationDrawable = (AnimationDrawable) (mImg1.getBackground());
    }

    private void frameAnimationByCode() {
        mAnimationDrawable2 = new AnimationDrawable();
        mAnimationDrawable2.addFrame(getResources().getDrawable(R.drawable.leopard_8), 500);
        mAnimationDrawable2.addFrame(getResources().getDrawable(R.drawable.leopard_7), 500);
        mAnimationDrawable2.addFrame(getResources().getDrawable(R.drawable.leopard_6), 500);
        mAnimationDrawable2.addFrame(getResources().getDrawable(R.drawable.leopard_5), 500);
        mAnimationDrawable2.addFrame(getResources().getDrawable(R.drawable.leopard_4), 500);
        mAnimationDrawable2.addFrame(getResources().getDrawable(R.drawable.leopard_3), 500);
        mAnimationDrawable2.addFrame(getResources().getDrawable(R.drawable.leopard_2), 500);
        mAnimationDrawable2.addFrame(getResources().getDrawable(R.drawable.leopard_1), 500);
        mImg2.setBackgroundDrawable(mAnimationDrawable2);
    }

    private void tweenAnimationByCode() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 50, 0, 0);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setRepeatCount(-1);
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setDuration(2000);
        mTvTranslateCode.startAnimation(translateAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.3f);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setRepeatCount(-1);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setDuration(2000);
        mTvAlphaCode.startAnimation(alphaAnimation);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f,2.0f,0.5f,2.0f);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(-1);
        scaleAnimation.setInterpolator(new LinearInterpolator());
        scaleAnimation.setDuration(2000);
        mTvScaleCode.startAnimation(scaleAnimation);

        RotateAnimation rotateAnimation = new RotateAnimation(0,360,0,0);
        rotateAnimation.setRepeatMode(Animation.REVERSE);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(2000);
        mTvRotationCode.startAnimation(rotateAnimation);

        AnimationSet animatorSet = new AnimationSet(true);
        animatorSet.addAnimation(translateAnimation);
        animatorSet.addAnimation(alphaAnimation);
        mTvSetCode.startAnimation(animatorSet);

    }

    private void tweenAnimationByXml() {
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.animation_alpha);
        mTvAlpha.startAnimation(animation1);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.animation_rotate);
        mTvRotation.startAnimation(animation2);
        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.animation_scale);
        mTvScale.startAnimation(animation3);
        Animation animation4 = AnimationUtils.loadAnimation(this, R.anim.animation_translate);
        mTvTranslate.startAnimation(animation4);
        Animation animation5 = AnimationUtils.loadAnimation(this, R.anim.animation_set);
        animation5.setRepeatCount(-1);
        animation5.setRepeatMode(Animation.REVERSE);
        mTvSet.startAnimation(animation5);
    }

    private void startTweenAnimation() {
        tweenAnimationByXml();
        tweenAnimationByCode();
    }

    @OnClick({
            R.id.tv_animation_1,
            R.id.img_1,
            R.id.tv_animation_2,
            R.id.tv_animation_tween
    })
    @SuppressWarnings("newApi")
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_animation_1) {
            mAnimationDrawable.start();//执行帧动画
            mAnimationDrawable2.start();
        } else if (id == R.id.img_1) {
            mAnimationDrawable.stop();//停止帧动画
        } else if (id == R.id.tv_animation_2) {
            Drawable drawable1 = mImg3.getDrawable();
            if (drawable1 instanceof Animatable) {
                ((Animatable) drawable1).start();
            }
            Drawable drawable = mImg4.getDrawable();
            if (drawable instanceof Animatable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ((AnimatedVectorDrawable) drawable).start();
                    ((AnimatedVectorDrawable) drawable).registerAnimationCallback(animationCallback);
                } else {
                    ((Animatable) drawable).start();
                }

            }
        } else if (id == R.id.tv_animation_tween) {
            startTweenAnimation();
        }
    }

    /********callback*********/
    @SuppressWarnings("newApi")
    private Animatable2.AnimationCallback animationCallback = new Animatable2.AnimationCallback() {
        @Override
        public void onAnimationStart(Drawable drawable) {
            super.onAnimationStart(drawable);
            Log.d(TAG, " onAnimationStart ");
        }

        @Override
        public void onAnimationEnd(Drawable drawable) {
            super.onAnimationEnd(drawable);
            Log.d(TAG, " onAnimationEnd ");
        }
    };
}
