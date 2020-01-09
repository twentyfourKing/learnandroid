package follow.twentyfourking.wanjetpack.view.ui.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import follow.twentyfourking.wanjetpack.R;
import follow.twentyfourking.wanjetpack.view.ui.adapter.HomeArticleAdapter;
import follow.twentyfourking.wanjetpack.view.ui.adapter.LoadMoreAdapter;
import follow.twentyfourking.wanjetpack.viewmodel.dao.ArticlePageEntity;
import follow.twentyfourking.wanjetpack.viewmodel.data.factory.MainFactory;
import follow.twentyfourking.wanjetpack.viewmodel.data.repository.MainRepository;
import follow.twentyfourking.wanjetpack.viewmodel.data.viewmodel.ArticleListViewModel;
import follow.twentyfourking.wanjetpack.viewmodel.utils.AppExecutors;

public class ChildSystemArtActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArticleListViewModel mArticleListViewModel;
    private HomeArticleAdapter mAdapter;
    private Context mContext;
    private MainRepository mRepository;
    private int mId;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_system_art_list);
        mId = getIntent().getIntExtra("cid", -1);
        initView();
        initViewModel();
        setObserveData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRepository.getChildSystemArticleList(0, mId, 0);
    }

    private void initView() {
        setSwipeLayout();
        setRecyclerView();
    }

    private void setRecyclerView() {
        mRecyclerView = findViewById(R.id.rc_system_article);
        //
        mAdapter = new HomeArticleAdapter();
        mAdapter.setListener(new LoadMoreAdapter.ILoadMoreListener() {
            @Override
            public void loadMore(int page) {
                //开始加载更多数据
                mRepository.getChildSystemArticleList(page, 30, 1);
            }
        });
        //设置布局Manager
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext,
                manager.getOrientation());
        mRecyclerView.setLayoutManager(manager);

        //设置分割线
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.item_drawable_1);
        itemDecoration.setDrawable(drawable);
        mRecyclerView.addItemDecoration(itemDecoration);

        //设置回调
        LoadMoreAdapter.LoadMoreScrollListener loadMoreScrollListener =
                new LoadMoreAdapter.LoadMoreScrollListener(mRecyclerView);
        mRecyclerView.addOnScrollListener(loadMoreScrollListener);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void setSwipeLayout() {
        mSwipeRefreshLayout = findViewById(R.id.swipe_layout_article);
        mSwipeRefreshLayout.setProgressViewOffset(true, 50, 200);
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mRepository.getChildSystemArticleList(0, mId, 0);
                    }
                }
        );
    }

    private void initViewModel() {
        ViewModelProvider.Factory factory = new MainFactory();
        ViewModelStore store = getViewModelStore();
        ViewModelProvider provider = new ViewModelProvider(store, factory);
        mArticleListViewModel = provider.get(ArticleListViewModel.class);

        AppExecutors appExecutors = new AppExecutors();
        mRepository = new MainRepository(appExecutors, mArticleListViewModel, null);
    }

    private void setObserveData() {
        mArticleListViewModel.getRefreshState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mSwipeRefreshLayout.setRefreshing(aBoolean);
            }
        });

        mArticleListViewModel.getArticleListDataInit().observe(this, new Observer<List<ArticlePageEntity>>() {
            @Override
            public void onChanged(List<ArticlePageEntity> articlePageEntities) {
                mAdapter.refreshList(articlePageEntities);
            }
        });

        mArticleListViewModel.getArticleListDataMore().observe(this, new Observer<List<ArticlePageEntity>>() {
            @Override
            public void onChanged(List<ArticlePageEntity> articlePageEntities) {
                mAdapter.appendList(articlePageEntities);
            }
        });
    }
}
