package follow.twentyfourking.learn_animation;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SampleActivity extends AppCompatActivity {
    @BindView(R.id.drawee_view)
    SimpleDraweeView mImg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);

        //播放webp动效
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/logo" );
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setUri(uri)
//                .setAutoPlayAnimations(true)//设置是否自动播放
//                .build();
//        mImg.setController(controller);

    }

    public void do_invalidate(View view){
        view.invalidate();
    }

    public void do_requestLayout(View view){
        view.requestLayout();
    }

}
