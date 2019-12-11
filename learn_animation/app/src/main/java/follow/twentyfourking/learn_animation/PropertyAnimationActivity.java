package follow.twentyfourking.learn_animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Property;
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
    @BindView(R.id.view_1)
    View mView1;
    private Context myContext;

    private Animator animator;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myContext = this;
        setContentView(R.layout.activity_property);
        ButterKnife.bind(this);

        animator =  AnimatorInflater.loadAnimator(myContext,R.animator.property_animator_1);
        animator.setTarget(mView1);

        ObjectAnimator.ofFloat()
    }

    @OnClick({
            R.id.tv_by_xml_property_1
    })
    public void onClick(View view){
        int id = view.getId();
        if(id == R.id.tv_by_xml_property_1){

            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    Log.d("TT","");
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.d("TT","");
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    Log.d("TT","");
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    Log.d("TT","");
                }
            });
        }

    }
}
