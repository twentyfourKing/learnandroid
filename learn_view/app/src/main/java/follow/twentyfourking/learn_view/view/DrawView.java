package follow.twentyfourking.learn_view.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Locale;

import follow.twentyfourking.learn_view.R;

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
        mPaint.setColor(Color.parseColor("#88ff0000"));
    }

    public void setType(int type) {
        this.mType = type;
    }

    @SuppressWarnings("newapi")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mType) {
            /******************************/
            case 1://绘制背景或者蒙层
                canvas.drawColor(Color.RED);
                break;
            case 11:
                canvas.drawARGB(255, 23, 234, 123);
                break;
            case 12:
                canvas.drawRGB(125, 0, 0);
                break;
/******************************/
            case 2://绘制圆形
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(5);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(50, 50, 40, mPaint);
                break;
            case 21://绘制弧形
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(5);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawArc(10, 10, 200, 100, -50,
                        100, false, mPaint);
                break;
            case 22://绘制椭圆 在一个区域里绘制，可以是横向的椭圆，也可以是竖直方向的果园
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(5);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawOval(10, 10, 100, 200, mPaint);
                break;
            case 23://绘制圆角矩形
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(5);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawRoundRect(10, 10, 100, 100, 10, 10, mPaint);
                break;
            case 24://绘制矩形
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(5);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(10, 10, 100, 100, mPaint);
                break;
/******************************/
            case 3://Bitmap
                mPaint.setAntiAlias(true);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.raw.ic_launcher);
                canvas.drawBitmap(bitmap, 10, 10, mPaint);
                break;
            case 31://Picture
                Picture mPicture = new Picture();
                Canvas canvas1 = mPicture.beginRecording(500, 500);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setStyle(Paint.Style.FILL);
                canvas1.translate(50, 50);
                canvas1.drawCircle(0, 0, 50, paint);
                mPicture.endRecording();
                canvas.drawPicture(mPicture);
                break;
/******************************/
            case 4://绘制点
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(30);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeCap(Paint.Cap.SQUARE);
                canvas.drawPoint(50, 50, mPaint);
                break;
            case 41://绘制多点
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(30);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeCap(Paint.Cap.ROUND);
                float[] pts = {50, 50, 100, 100, 50, 100, 100, 50};
                canvas.drawPoints(pts, mPaint);
                break;
/******************************/
            case 5://绘制文字 (x,y)是文字的起始点，不过它是文字的左下角位置
                mPaint.setAntiAlias(true);
                mPaint.setTextSize(40);
                canvas.drawText("还不错哟", 100, 100, mPaint);

                break;
/******************************/
            case 6://绘制线
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(10);//线宽
                canvas.drawLine(0, 0, 200, 200, mPaint);
                break;
            case 61://多线绘制
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(10);
                mPaint.setStrokeCap(Paint.Cap.ROUND);//线交的样式，圆角
                float[] pts1 = {0, 10, 50, 10, 50, 10, 50, 60, 50, 60, 100, 10, 100, 10, 100, 60, 100, 60, 200, 60};
                canvas.drawLines(pts1, mPaint);
                break;
/******************************/
            case 7://
                Path path = new Path();
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(10);
                path.lineTo(100, 100);
                path.arcTo(100, 100, 300, 300, -90, 90, false);
                canvas.drawPath(path, mPaint);
                break;
            case 8:
                mPaint.setTextSize(10);
                mPaint.setTextAlign(Paint.Align.LEFT);
                mPaint.setUnderlineText(true);
                mPaint.setLinearText(true);
                mPaint.setTextLocale(Locale.JAPAN);
                canvas.save();
                canvas.restore();
                break;
        }


    }

}
