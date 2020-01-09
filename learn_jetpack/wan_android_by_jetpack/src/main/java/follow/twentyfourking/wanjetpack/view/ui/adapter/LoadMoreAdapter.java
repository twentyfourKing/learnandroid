package follow.twentyfourking.wanjetpack.view.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import follow.twentyfourking.wanjetpack.R;

/**
 * 一个加载更多的基础Adapter
 */
public abstract class LoadMoreAdapter<T> extends RecyclerView.Adapter {
    //定义视图类型
    public static int NORMAL_TYPE = 1;
    public static int BOTTOM_TYPE = 2;
    public int loadState;
    //加载项的状态
    public static int IS_LOADING = 1;//"数据加载中"
    public static int IS_NOT_MORE = 2;//"没有更多数据"
    public static int IS_FAIL = 3;//"加载失败重新尝试"
    public static int IS_COMPLETE = 4;//"完成"

    public List<T> itemListData;
    public int mPageCount = -1;//分页加载方案，页数标记

    boolean isLoading = false;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == BOTTOM_TYPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.widget_load_more, parent, false);
            return new BottomItemViewHolder(view);
        } else {
            return createNormalViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (BOTTOM_TYPE == getItemViewType(position)) {
            final TextView bottomTextView = ((BottomItemViewHolder) holder).info;
            if (loadState == IS_LOADING) {
                bottomTextView.setVisibility(View.VISIBLE);
                bottomTextView.setText("加载中");
                holder.itemView.setOnClickListener(null);
            } else if (loadState == IS_NOT_MORE) {
                bottomTextView.setVisibility(View.VISIBLE);
                bottomTextView.setText("没有更多了");
                holder.itemView.setOnClickListener(null);
            } else if (loadState == IS_FAIL) {
                bottomTextView.setVisibility(View.VISIBLE);
                bottomTextView.setText("加载失败请点击重试");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomTextView.setText("加载中");
                        loadMoreData();
                    }
                });
            } else {
                bottomTextView.setVisibility(View.GONE);
            }
        } else {
            bindNormalViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (itemListData != null && itemListData.size() > 0) {
            return itemListData.size() + 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return BOTTOM_TYPE;
        } else {
            return NORMAL_TYPE;
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    protected abstract RecyclerView.ViewHolder createNormalViewHolder(ViewGroup parent, int viewType);

    protected abstract void bindNormalViewHolder(RecyclerView.ViewHolder holder, int position);

    protected abstract void loadMoreData();

    public class BottomItemViewHolder extends RecyclerView.ViewHolder {
        private TextView info;

        public BottomItemViewHolder(@NonNull View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.tv_more_loading);
        }
    }

    public interface ILoadMoreListener {
        void loadMore(int page);
    }

    public static class LoadMoreScrollListener extends RecyclerView.OnScrollListener {
        private RecyclerView recyclerView;

        public LoadMoreScrollListener(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            LoadMoreAdapter adapter = (LoadMoreAdapter) recyclerView.getAdapter();
            if (manager instanceof LinearLayoutManager) {
                int lastVisibleItemPosition = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                if (adapter.getItemCount() - 1 == lastVisibleItemPosition) {
                    if (!adapter.isLoading()) {
                        Log.d("TTT", "开始加载数据");
                        adapter.loadMoreData();
                    } else {
                        Log.d("TTT", "已经处于加载中");
                    }
                }
            }
        }
    }

}
