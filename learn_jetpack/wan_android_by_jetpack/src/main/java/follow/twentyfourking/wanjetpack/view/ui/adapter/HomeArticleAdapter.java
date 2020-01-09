package follow.twentyfourking.wanjetpack.view.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import follow.twentyfourking.base.BaseViewHolder;
import follow.twentyfourking.wanjetpack.R;
import follow.twentyfourking.wanjetpack.viewmodel.dao.ArticlePageEntity;

public class HomeArticleAdapter extends LoadMoreAdapter<ArticlePageEntity> {
    private ILoadMoreListener mLoadMoreListener;

    @Override
    protected RecyclerView.ViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_article, parent, false);
        return new HomeArticleAdapter.HomeArticleViewHolder(view);
    }

    @Override
    protected void bindNormalViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((HomeArticleViewHolder) holder).author.setText(itemListData.get(position).getAuthor());
        ((HomeArticleViewHolder) holder).title.setText(itemListData.get(position).getTitle());
        ((HomeArticleViewHolder) holder).chapter.setText(itemListData.get(position)
                .getSuperChapterName() + "/" + itemListData.get(position).getChapterName());
        ((HomeArticleViewHolder) holder).date.setText(itemListData.get(position).getNiceDate());
    }

    @Override
    protected void loadMoreData() {
        //开始网络加载数据
        loadState = IS_LOADING;
        setLoading(true);
        notifyItemChanged(getItemCount() - 1);//刷新最后一项的ui状态
        mLoadMoreListener.loadMore(mPageCount + 1);
    }

    class HomeArticleViewHolder extends BaseViewHolder {
        public HomeArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.tv_author);
            title = itemView.findViewById(R.id.tv_article_title);
            chapter = itemView.findViewById(R.id.tv_chapter);
            date = itemView.findViewById(R.id.tv_date);
        }
    }

    public void setListener(ILoadMoreListener listener) {
        this.mLoadMoreListener = listener;
    }

    //添加更多数据
    public void appendList(List appendData) {
        if (appendData == null) {
            loadState = IS_FAIL;
            notifyItemChanged(getItemCount() - 1);
        } else {
            if (appendData.size() == 0) {
                loadState = IS_NOT_MORE;
                notifyItemChanged(getItemCount() - 1);
            } else {
                mPageCount++;//数据加载成功后，才将当前页数加1
                loadState = IS_COMPLETE;
                int count = itemListData.size();
                itemListData.addAll(appendData);
                notifyItemRangeChanged(count, appendData.size());
                setLoading(false);
            }
        }
    }

    //刷新数据
    public void refreshList(List newList) {
        mPageCount = 0;
        if (itemListData != null) {
            itemListData.clear();
            itemListData.addAll(newList);
        } else {
            itemListData = newList;
        }
        notifyDataSetChanged();
    }
}
