package follow.twentyfourking.view;

import android.graphics.Rect;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //去ActionBar
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //去除状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.id_textview1);
        textView2 = findViewById(R.id.id_textview2);
        textView3 = findViewById(R.id.id_textview3);
        textView4 = findViewById(R.id.id_textview4);
        initView();
    }

    public void initView() {
        textView1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View rootView = getWindow().getDecorView();
                Rect rect = new Rect();
                Rect rect2 = new Rect();
                int[] location2 = new int[2];
                int[] location = new int[2];
                textView1.getLocationOnScreen(location);
                textView1.getWindowVisibleDisplayFrame(rect);
                textView1.getLocalVisibleRect(rect2);
                textView1.getLocationInWindow(location2);
                Rect rect3 = new Rect();
                int[] location3 = new int[2];
                Rect rect4 = new Rect();
                int[] location5 = new int[2];
                rootView.getLocationOnScreen(location3);
                rootView.getWindowVisibleDisplayFrame(rect3);
                rootView.getLocalVisibleRect(rect4);
                rootView.getLocationInWindow(location5);
                int w1 = textView1.getWidth();
                int h1 = textView1.getHeight();
                float x = textView1.getX();
                float y = textView1.getY();
                float x1 = textView1.getPivotX();
                float y1 = textView1.getPivotY();

                textView2.setTranslationY(60);
                int[] location11 = new int[2];
                Rect rect11 = new Rect();
                textView2.getLocationOnScreen(location11);
                textView2.getLocalVisibleRect(rect11);

                int[] location12 = new int[2];
                Rect rect12 = new Rect();
                textView3.getLocationOnScreen(location12);
                textView3.getLocalVisibleRect(rect12);



                Log.d("TAG", "");
            }
        });
    }

}
