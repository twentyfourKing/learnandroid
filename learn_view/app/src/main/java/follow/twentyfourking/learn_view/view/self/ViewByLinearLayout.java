package follow.twentyfourking.learn_view.view.self;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ViewByLinearLayout extends ViewGroup {
    public ViewByLinearLayout(Context context) {
        this(context, null);
    }

    public ViewByLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewByLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int useHeight = 0;
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            //奇数放左边，偶数放右边
            ViewGroup.LayoutParams params = childView.getLayoutParams();
            if ((i + 1) % 2 == 0) {
                childView.layout(r - childView.getMeasuredWidth(), useHeight, r, childView.getMeasuredHeight() + useHeight);
            } else {
                childView.layout(l, useHeight, childView.getMeasuredWidth() + l, childView.getMeasuredHeight() + useHeight);
            }
            useHeight += childView.getMeasuredHeight();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int remainWidth = widthSize;
        int remainHeight = heightSize;
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            int childWidthMeasureSpec;
            int childHeightMeasureSpec;
            ViewGroup.LayoutParams params = childView.getLayoutParams();
            //宽
            if (params.width == LayoutParams.MATCH_PARENT) {
                if (widthMode == MeasureSpec.EXACTLY) {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(remainWidth, MeasureSpec.EXACTLY);
                } else if (widthMode == MeasureSpec.AT_MOST) {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(remainWidth, MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                }
            } else if (params.width == LayoutParams.WRAP_CONTENT) {
                if (widthMode == MeasureSpec.EXACTLY) {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(remainWidth, MeasureSpec.AT_MOST);
                } else if (widthMode == MeasureSpec.AT_MOST) {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(remainWidth, MeasureSpec.AT_MOST);
                } else {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                }
            } else {//确切的大小值
                if (widthMode == MeasureSpec.EXACTLY) {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
                } else if (widthMode == MeasureSpec.AT_MOST) {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                }
            }
            //高
            if (params.height == LayoutParams.MATCH_PARENT) {
                if (heightMode == MeasureSpec.EXACTLY) {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(remainHeight, MeasureSpec.EXACTLY);
                } else if (widthMode == MeasureSpec.AT_MOST) {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(remainHeight, MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                }
            } else if (params.width == LayoutParams.WRAP_CONTENT) {
                if (widthMode == MeasureSpec.EXACTLY) {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(remainHeight, MeasureSpec.AT_MOST);
                } else if (widthMode == MeasureSpec.AT_MOST) {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(remainHeight, MeasureSpec.AT_MOST);
                } else {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                }
            } else {//确切的大小值
                if (widthMode == MeasureSpec.EXACTLY) {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
                } else if (widthMode == MeasureSpec.AT_MOST) {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                }
            }
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
//            remainWidth -= childView.getMeasuredWidth();
//            remainHeight -= childView.getMeasuredHeight();
        }
    }
}
