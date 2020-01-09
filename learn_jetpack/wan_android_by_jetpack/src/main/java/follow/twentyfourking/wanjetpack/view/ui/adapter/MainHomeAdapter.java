package follow.twentyfourking.wanjetpack.view.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MainHomeAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        if (mFragmentList != null) {
            return mFragmentList.size();
        }
        return 0;
    }

    public MainHomeAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> mFragmentList) {
        super(fm, behavior);
        this.mFragmentList = mFragmentList;
    }
    //
//    @NonNull
//    @Override
//    public Fragment createFragment(int position) {
//        return mFragmentList.get(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        if (mFragmentList != null) {
//            return mFragmentList.size();
//        }
//        return 0;
//    }
//
//    public MainHomeAdapter(@NonNull FragmentActivity fragmentActivity) {
//        super(fragmentActivity);
//        mFragmentList = new ArrayList<>();
//    }
//
//    //添加数据
//    public void setData(List<Fragment> data) {
//        mFragmentList = data;
//    }

}
