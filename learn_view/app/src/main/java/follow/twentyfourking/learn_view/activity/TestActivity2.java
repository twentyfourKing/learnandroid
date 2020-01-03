package follow.twentyfourking.learn_view.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import follow.twentyfourking.learn_view.R;

import static follow.twentyfourking.learn_view.common.Constant.TAG_TEST_LOCATION;

public class TestActivity2 extends AppCompatActivity implements View.OnTouchListener {
    @BindView(R.id.tv_test_motion)
    TextView mTvMotion;

    private int mTouchSlop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_2);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
//        ViewTreeObserver treeObserver = getWindow().getDecorView().getViewTreeObserver();
//        treeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                return false;
//            }
//        });
//        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//
//            }
//        });

        mTvMotion.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float lastX = 0;
        float lastY = 0;
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            //相对view自身（0,0）
            Log.d(TAG_TEST_LOCATION, " 当前按下的位置 x = " + event.getX() + " y = "
                    + event.getY() + " --> by getX和getY");
            //相对屏幕（0，0）
            Log.d(TAG_TEST_LOCATION, " 当前按下的位置 x = " + event.getRawX() + " y = "
                    + event.getRawY() + " --> by getRawX和getRawY");
            lastX = event.getX();
            lastY = event.getY();
        }
        if (action == MotionEvent.ACTION_MOVE) {
            float currentX = event.getX();
            float currentY = event.getY();
            if ((currentX - lastX) > mTouchSlop || (currentY - lastY) > mTouchSlop) {
                Log.d(TAG_TEST_LOCATION, "触发了移动");
            }
        }
        if (action == MotionEvent.ACTION_UP) {

        }
        return true;
    }

    @OnClick({
            R.id.tv_test_getLocation
    })
    @SuppressWarnings("newapi")
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_test_getLocation) {
            Log.d(TAG_TEST_LOCATION, " view x = " + view.getX() +
                    " y = " + view.getY() + " --> by getX和getY");

            //获取view相对屏幕的位置信息
            int[] locationOnScreen = new int[2];
            view.getLocationOnScreen(locationOnScreen);
            Log.d(TAG_TEST_LOCATION, " view x = " + locationOnScreen[0] +
                    " y = " + locationOnScreen[1] + " --> by getLocationOnScreen()");

            //获取view相对所属window的位置信息
            int[] locationInWindow = new int[2];
            view.getLocationInWindow(locationInWindow);
            Log.d(TAG_TEST_LOCATION, " view x = " + locationInWindow[0] +
                    " y = " + locationInWindow[1] + " --> by getLocationInWindow()");

            //获取view相对Surface的位置信息
            int[] locationInSurface = new int[2];
            view.getLocationInSurface(locationInSurface);
            Log.d(TAG_TEST_LOCATION, " view x = " + locationInSurface[0] +
                    " y = " + locationInSurface[1] + " --> by getLocationInSurface()");

            //获取view 相对view自身(0,0)的可见的矩形区域
            Rect rectLocal = new Rect();
            view.getLocalVisibleRect(rectLocal);
            Log.d(TAG_TEST_LOCATION, " view width = " + (rectLocal.right - rectLocal.left) +
                    " height = " + (rectLocal.bottom - rectLocal.top) + " --> by getLocalVisibleRect()");

            //获取view的宽高值
            Log.d(TAG_TEST_LOCATION, " view width = " + view.getWidth() +
                    " height = " + view.getHeight() + " --> by getWidth 和 getHeight()");

            //获取view 相对屏幕(0,0)的可见的矩形区域
            Rect rectGlobal = new Rect();
            view.getGlobalVisibleRect(rectGlobal);
            Log.d(TAG_TEST_LOCATION, " view width = " + (rectGlobal.right - rectGlobal.left) +
                    " height = " + (rectGlobal.bottom - rectGlobal.top) + " --> by getLocalVisibleRect()");
            Log.d(TAG_TEST_LOCATION, " view x = " + rectGlobal.left +
                    " y = " + rectGlobal.top + " --> by getGlobalVisibleRect()");
        }
    }
}
