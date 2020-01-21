package follow.twentyfourking.learn_recyleview.adapter;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class TestDiffUtils extends DiffUtil.Callback {
    private List<TestItemBean> mOldList;
    private List<TestItemBean> mNewList;

    public TestDiffUtils(List<TestItemBean> oldList, List<TestItemBean> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList == null ? 0 : mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList == null ? 0 : mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).getName().
                equals(mNewList.get(newItemPosition).getName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        TestItemBean oldBean = mOldList.get(oldItemPosition);
        TestItemBean newBean = mNewList.get(newItemPosition);
        if (!oldBean.getName().equals(newBean.getName())) {
            return false;
        }
        if (!oldBean.getAge().equals(newBean.getAge())) {
            return false;
        }
        return true;
    }
}
