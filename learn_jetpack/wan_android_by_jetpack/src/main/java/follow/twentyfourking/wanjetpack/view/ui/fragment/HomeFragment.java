package follow.twentyfourking.wanjetpack.view.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import follow.twentyfourking.wanjetpack.R;
import follow.twentyfourking.wanjetpack.view.ui.adapter.HomeArticleAdapter;
import follow.twentyfourking.wanjetpack.view.ui.adapter.LoadMoreAdapter;
import follow.twentyfourking.wanjetpack.viewmodel.dao.ArticlePageEntity;
import follow.twentyfourking.wanjetpack.viewmodel.data.viewmodel.MainViewModel;

public class HomeFragment extends Fragment {
    private String mFrom;
    private RecyclerView mRecyclerView;
    private HomeArticleAdapter mHomeAdapter;
    private Context mContext;
    private IHomeCallback mCallback;

    public static HomeFragment newInstance(String from, IHomeCallback callback) {
        HomeFragment fragment = new HomeFragment();
        fragment.setCallback(callback);
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFrom = getArguments().getString("from");
        }
    }

    private void setCallback(IHomeCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        observeViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_layout, null);
        initContent(view);
        return view;
    }

    private void initContent(View view) {
        mRecyclerView = view.findViewById(R.id.rc_fragment_home_article);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(RecyclerView.VERTICAL);
        mHomeAdapter = new HomeArticleAdapter();
        mRecyclerView.setLayoutManager(manager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext,
                manager.getOrientation());
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.item_drawable_1);
        itemDecoration.setDrawable(drawable);
        mRecyclerView.addItemDecoration(itemDecoration);
        LoadMoreAdapter.LoadMoreScrollListener loadMoreScrollListener =
                new LoadMoreAdapter.LoadMoreScrollListener(mRecyclerView);
        mRecyclerView.addOnScrollListener(loadMoreScrollListener);
        mHomeAdapter.setListener(new LoadMoreAdapter.ILoadMoreListener() {
            @Override
            public void loadMore(int page) {
                //开始加载更多数据
                mCallback.onGetArticleListMore(page, 0);
            }
        });
        mRecyclerView.setAdapter(mHomeAdapter);
    }

    private void observeViewModel() {
        mCallback.onGetMainViewModel().getArticleListData().observe(getActivity(), new Observer<List<ArticlePageEntity>>() {
            @Override
            public void onChanged(List<ArticlePageEntity> articleInfoBeans) {
                mHomeAdapter.refreshList(articleInfoBeans);
            }
        });
        mCallback.onGetMainViewModel().getArticleListMoreData().observe(getActivity(), new Observer<List<ArticlePageEntity>>() {
            @Override
            public void onChanged(List<ArticlePageEntity> articleInfoBeans) {
                mHomeAdapter.appendList(articleInfoBeans);
            }
        });
    }

    public interface IHomeCallback {
        void onGetArticleListMore(int pageCount, int type);

        MainViewModel onGetMainViewModel();
    }

}
