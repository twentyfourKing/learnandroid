package follow.twentyfourking.learn_animation;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
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

    @OnClick({
            R.id.tv_animation_1,
            R.id.img_1,
            R.id.tv_animation_2
    })
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
                ((Animatable) drawable).start();
            }
        }
    }
}
