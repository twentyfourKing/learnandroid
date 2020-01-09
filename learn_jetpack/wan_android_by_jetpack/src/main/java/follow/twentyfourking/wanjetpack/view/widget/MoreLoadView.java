package follow.twentyfourking.jetpack_wanandroid.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import follow.twentyfourking.wanjetpack.R;

public class MoreLoadView extends View {

    private TextView mTvLoading;

    public MoreLoadView(Context context) {
        this(context, null);
    }

    public MoreLoadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoreLoadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context, R.layout.widget_load_more, null);
        mTvLoading = (TextView) view.findViewById(R.id.tv_more_loading);
    }

    public void loading() {
        mTvLoading.setText("已经在加载中");
    }

    public void failure(){
        mTvLoading.setText("加载失败");
    }

    public void complete(){
        mTvLoading.setText("加载完成");
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}
