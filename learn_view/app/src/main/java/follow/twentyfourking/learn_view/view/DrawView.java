package follow.twentyfourking.learn_view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawView extends View {
    private Paint mPaint;
    private int mType;

    public DrawView(Context context) {
        this(context, null);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#88880000"));
    }

    public void setType(int type) {
        this.mType = type;
    }

    @SuppressWarnings("newapi")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mType) {
            case 1:
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(5);
                canvas.drawLine(0, 0, 50, 100, mPaint);
                break;
            case 2:
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(5);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(50, 50, 50, mPaint);
                break;
            case 3:
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(5);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawOval(10, 10, 300, 90, mPaint);
                break;
            case 4:
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(5);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawRoundRect(50, 50, 200, 200, 50, 50, mPaint);
                break;
            case 5:
                mPaint.setAntiAlias(true);
                canvas.drawText("还不错哟",100,100,mPaint);
                break;
        }


    }

}
