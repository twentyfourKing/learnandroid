package follow.twentyfourking.wanjetpack.view.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import follow.twentyfourking.wanjetpack.R;
import follow.twentyfourking.wanjetpack.viewmodel.protocol.bean.ChildArticleBean;

public class SystemArticleAdapter extends RecyclerView.Adapter<SystemArticleAdapter.SystemArticleViewHolder> {

    private List<ChildArticleBean> mListData;
    private ISystemArticleCallback mCallback;


    @NonNull
    @Override
    public SystemArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.view_system_article_list_item, parent, false);
        return new SystemArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SystemArticleViewHolder holder, int position) {
        String content = mListData.get(position).getName();
        String tag = content.substring(0, 1);
        String title = content.substring(1, content.length());
        holder.tvTitle.setText(title);
        holder.tvIndexTag.setText(tag);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TTT", "点击了 mListData " + mListData.get(position).getName());
                mCallback.startActivity(mListData.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListData != null) {
            return mListData.size();
        }
        return 0;
    }

    class SystemArticleViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvTitle;
        protected TextView tvIndexTag;
        protected RelativeLayout container;

        public SystemArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_system_article_title);
            tvIndexTag = itemView.findViewById(R.id.tv_index_tag);
            container = itemView.findViewById(R.id.rl_system_list_item_container);
        }
    }

    public void setData(List<ChildArticleBean> data) {
        mListData = data;
        notifyDataSetChanged();
    }

    public void setCallback(ISystemArticleCallback callback) {
        this.mCallback = callback;
    }

    public interface ISystemArticleCallback {
        void startActivity(int cId);
    }
}
