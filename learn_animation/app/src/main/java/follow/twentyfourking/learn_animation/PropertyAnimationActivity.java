package follow.twentyfourking.learn_animation;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private Context myContext;

    private ObjectAnimator mAnimator1;
    private ObjectAnimator mAnimator2;
    private ValueAnimator mAnimator3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myContext = this;
        setContentView(R.layout.activity_property);
        ButterKnife.bind(this);


        initAnimator();
    }

    private void initAnimator() {
        mAnimator1 = (ObjectAnimator) AnimatorInflater.loadAnimator(myContext, R.animator.property_animator_2);
        mAnimator1.setTarget(mTv1);

        mAnimator2 = (ObjectAnimator) AnimatorInflater.loadAnimator(myContext, R.animator.property_animator_3);
        mAnimator2.setTarget(mTv2);

        mAnimator3 = (ValueAnimator) AnimatorInflater.loadAnimator(myContext, R.animator.property_animator_4);
        mAnimator3.setTarget(mTv3);
    }

    @OnClick({
            R.id.tv_by_xml_property_1,
            R.id.tv_by_xml_property_2,
            R.id.tv_reverse,
            R.id.tv_by_xml_property_3
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
        }else if(id == R.id.tv_by_xml_property_3){
            mAnimator3.start();
            mAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Log.d("TTT"," value = "+animation.getAnimatedValue());
                    mTv3.setAlpha((float)animation.getAnimatedValue());
                }
            });
        }

    }
}
