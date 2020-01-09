package follow.twentyfourking.wanjetpack.view.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import follow.twentyfourking.wanjetpack.R;
import follow.twentyfourking.wanjetpack.view.ui.adapter.MainHomeAdapter;
import follow.twentyfourking.jetpack_wanandroid.view.ui.fragment.AttentionFragment;
import follow.twentyfourking.jetpack_wanandroid.view.ui.fragment.DiscoveryFragment;
import follow.twentyfourking.jetpack_wanandroid.view.ui.fragment.ProfileFragment;

public class ActivityTestTabLayout extends AppCompatActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private int[] imgId = new int[]{R.drawable.ic_tab_strip_icon_category_selected
            , R.drawable.ic_tab_strip_icon_feed_selected,
            R.drawable.ic_tab_strip_icon_pgc_selected,
            R.drawable.ic_tab_strip_icon_profile_selected};
    private int[] strId = new int[]{R.string.tab1,
            R.string.tab2, R.string.tab3, R.string.tab4};
    private MainHomeAdapter mAdapter;
    private TabLayout.Tab[] mTabs = new TabLayout.Tab[4];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        mTabLayout = findViewById(R.id.tl_main);
        mViewPager = findViewById(R.id.vp_main);
        initViews();
    }


    public void initViews() {
        TabLayout.Tab tab1 = mTabLayout.newTab().
                setCustomView(createTabView(imgId[0], strId[0]));
        mTabs[0] = tab1;
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

        mAdapter = new MainHomeAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                addFragmentData());

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
        View view = LayoutInflater.from(this).inflate(R.layout.view_tab_item,
                null, false);
        view.findViewById(R.id.img_tab_icon).setBackgroundResource(imgId);
        ((TextView) view.findViewById(R.id.tv_tab_title)).setText(strId);
        return view;
    }

    private List<Fragment> addFragmentData() {
        List<Fragment> list = new ArrayList<>();
        String from = "";
//        list.add(HomeFragment.newInstance(from));
        list.add(DiscoveryFragment.newInstance(from));
        list.add(AttentionFragment.newInstance(from));
        list.add(ProfileFragment.newInstance(from));
        return list;
    }

}
