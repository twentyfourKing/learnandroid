package follow.twentyfourking.wanjetpack.model.delegate;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import follow.twentyfourking.jetpack_wanandroid.view.ui.fragment.AttentionFragment;
import follow.twentyfourking.jetpack_wanandroid.view.ui.fragment.ProfileFragment;
import follow.twentyfourking.wanjetpack.R;
import follow.twentyfourking.wanjetpack.view.ui.activity.MainActivity;
import follow.twentyfourking.wanjetpack.view.ui.adapter.MainHomeAdapter;
import follow.twentyfourking.wanjetpack.view.ui.fragment.HomeFragment;
import follow.twentyfourking.wanjetpack.view.ui.fragment.SystemFragment;
import follow.twentyfourking.wanjetpack.viewmodel.dao.ArticleDatabase;
import follow.twentyfourking.wanjetpack.viewmodel.data.factory.MainFactory;
import follow.twentyfourking.wanjetpack.viewmodel.data.repository.MainRepository;
import follow.twentyfourking.wanjetpack.viewmodel.data.viewmodel.MainViewModel;
import follow.twentyfourking.wanjetpack.viewmodel.utils.AppExecutors;

public class MainActivityDelegate {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MainActivity mActivity;
    private int[] imgId = new int[]{R.drawable.ic_tab_strip_icon_category_selected
            , R.drawable.ic_tab_strip_icon_feed_selected,
            R.drawable.ic_tab_strip_icon_pgc_selected,
            R.drawable.ic_tab_strip_icon_profile_selected};
    private int[] strId = new int[]{R.string.tab1,
            R.string.tab2, R.string.tab3, R.string.tab4};
    private MainHomeAdapter mAdapter;
    private TabLayout.Tab[] mTabs = new TabLayout.Tab[4];
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private MainViewModel mMainViewModel;
    private MainRepository mMainRepository;

    public MainActivityDelegate() {

    }

    public void setDelegateInit(View view, Activity activity) {
        this.mActivity = (MainActivity) activity;
        setViewModel();
        AppExecutors appExecutors = new AppExecutors();
        mMainRepository = new MainRepository(appExecutors, mMainViewModel,
                ArticleDatabase.getInstance(mActivity));
        initViews(view);
    }

    public void onResume() {
        //加载数据
        mMainRepository.getArticleListInit(0);
        mMainRepository.getTechSystemList();
    }

    private void initViews(View view) {
        mTabLayout = view.findViewById(R.id.tl_main);
        mViewPager = view.findViewById(R.id.vp_main);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout_main);
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
                        //刷新数据只加载第一页
                        if (mViewPager.getCurrentItem() == 0) {
                            mMainRepository.getArticleListInit(0);
                        } else {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }
        );

        TabLayout.Tab tab1 = mTabLayout.newTab().
                setCustomView(createTabView(imgId[0], strId[0]));
        mTabs[0] = tab1;
        //默认选择第一项，通过设置字体颜色进行标识
        ((TextView) (tab1.getCustomView().findViewById(R.id.tv_tab_title))).setTextColor(Color.RED);
        mTabLayout.addTab(tab1);

        TabLayout.Tab tab2 = mTabLayout.newTab().
                setCustomView(createTabView(imgId[1], strId[1]));
        mTabs[1] = tab2;
        mTabLayout.addTab(tab2);

        TabLayout.Tab tab3 = mTabLayout.newTab().
                setCustomView(createTabView(imgId[2], strId[2]));
        mTabs[2] = tab3;
        mTabLayout.addTab(tab3);

        TabLayout.Tab tab4 = mTabLayout.newTab().
                setCustomView(createTabView(imgId[3], strId[3]));
        mTabs[3] = tab4;
        mTabLayout.addTab(tab4);

        mAdapter = new MainHomeAdapter(mActivity.getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                createFragmentData());

        mViewPager.setOffscreenPageLimit(2);//预加载2
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.selectTab(mTabLayout.getTabAt(position), true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                //切换当前项是否进行滚动
//                mViewPager.setCurrentItem(position);
                mViewPager.setCurrentItem(position, false);
                ((TextView) (tab.getCustomView().findViewById(R.id.tv_tab_title))).setTextColor(Color.RED);
                if (position == 2) {
                    ((TextView) (tab.getCustomView().findViewById(R.id.tv_tab_red))).setVisibility(View.VISIBLE);
                    ((TextView) (tab.getCustomView().findViewById(R.id.tv_tab_red))).setText("11");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                ((TextView) (tab.getCustomView().findViewById(R.id.tv_tab_title))).setTextColor(Color.BLACK);
                if (position == 2) {
                    ((TextView) (tab.getCustomView().findViewById(R.id.tv_tab_red))).setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.setAdapter(mAdapter);
    }

    private View createTabView(int imgId, int strId) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_tab_item,
                null, false);
        view.findViewById(R.id.img_tab_icon).setBackgroundResource(imgId);
        ((TextView) view.findViewById(R.id.tv_tab_title)).setText(strId);
        return view;
    }

    private List<Fragment> createFragmentData() {
        List<Fragment> list = new ArrayList<>();
        String from = "";
        list.add(HomeFragment.newInstance(from, iHomeCallback));
        list.add(SystemFragment.newInstance(iSystemCallback));
        list.add(AttentionFragment.newInstance(from));
        list.add(ProfileFragment.newInstance(from));
        return list;
    }

    private void setViewModel() {
        ViewModelStore viewModelStore = mActivity.getViewModelStore();
        ViewModelProvider.Factory factory = new MainFactory();
        ViewModelProvider vmProvider = new ViewModelProvider(viewModelStore, factory);
        mMainViewModel = vmProvider.get(MainViewModel.class);
        mMainViewModel.getStateFresh().observe(mActivity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mSwipeRefreshLayout.setRefreshing(aBoolean);
            }
        });
    }


    /****************callback- start******************/
    private HomeFragment.IHomeCallback iHomeCallback = new HomeFragment.IHomeCallback() {
        @Override
        public void onGetArticleListMore(int pageCount, int type) {
            mMainRepository.getArticleListMore(pageCount, type);
        }

        @Override
        public MainViewModel onGetMainViewModel() {
            return mMainViewModel;
        }
    };

    private SystemFragment.ISystemCallback iSystemCallback = new SystemFragment.ISystemCallback() {
        @Override
        public MainViewModel onGetMainViewModel() {
            return mMainViewModel;
        }
    };

    /****************callback-end*****************/
}
