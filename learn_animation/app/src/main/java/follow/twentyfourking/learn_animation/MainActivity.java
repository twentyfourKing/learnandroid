package follow.twentyfourking.learn_animation;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void jumpToProperty(View view){
        startActivity(new Intent(this,PropertyAnimationActivity.class));
        TextView textView;
        Animation animation ;
        animation.setAnimationListener();
        textView.startAnimation();



    }

}
