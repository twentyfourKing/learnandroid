package follow.twentyfourking.learn_view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import follow.twentyfourking.learn_view.common.Constant;

public class MyLinearLayout1 extends LinearLayout {
    public MyLinearLayout1(Context context) {
        this(context, null);
    }

    public MyLinearLayout1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(Constant.TAG_TEST_VIEW, "MyLinearLayout1 --> onDraw");
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(Constant.TAG_TEST_VIEW, "MyLinearLayout1 --> onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(Constant.TAG_TEST_VIEW, "MyLinearLayout1 --> onLayout");
        super.onLayout(changed, left, top, right, bottom);
    }

}
