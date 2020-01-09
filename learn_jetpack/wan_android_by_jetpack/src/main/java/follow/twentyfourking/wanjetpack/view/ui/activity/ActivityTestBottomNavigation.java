package follow.twentyfourking.wanjetpack.view.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import follow.twentyfourking.wanjetpack.R;
import follow.twentyfourking.wanjetpack.view.ui.adapter.MainHomeAdapter;
import follow.twentyfourking.jetpack_wanandroid.view.ui.fragment.AttentionFragment;
import follow.twentyfourking.jetpack_wanandroid.view.ui.fragment.DiscoveryFragment;
import follow.twentyfourking.jetpack_wanandroid.view.ui.fragment.ProfileFragment;

public class ActivityTestBottomNavigation extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;
    private MainHomeAdapter mFragmentAdapter;
    private MenuItem mMenuItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        initView();
    }

    private void initView() {
        mViewPager = findViewById(R.id.vp_fragment);
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mFragmentAdapter = new MainHomeAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, addFragmentData());
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab_menu_home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_menu_discovery:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.tab_menu_attention:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.tab_menu_profile:
                        mViewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mMenuItem != null) {
                    mMenuItem.setChecked(false);
                } else {
                    mBottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                mMenuItem = mBottomNavigationView.getMenu().getItem(position);
                mMenuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setAdapter(mFragmentAdapter);
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
