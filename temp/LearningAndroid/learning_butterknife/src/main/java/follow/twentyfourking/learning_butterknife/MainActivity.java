package follow.twentyfourking.learning_butterknife;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindColor;
import butterknife.BindFont;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_hello)
    TextView mHello;
    @BindView(R.id.tv_click)
    TextView mClick;

    Unbinder mUnbinder;
    @BindColor(android.R.color.black)
    @ColorInt
    int blackColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        mHello.setText("看啥呢");
        mClick.setText("就看你");
        mUnbinder.unbind();
    }

    @OnClick({
            R.id.tv_hello,
            R.id.tv_click
    })
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_hello) {
            mHello.setText("弄你");
        } else if (id == R.id.tv_click) {
            mClick.setText("怕你");
        }
    }
}
