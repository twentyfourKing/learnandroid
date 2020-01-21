package follow.twentyfourking.learn_recyleview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import android.os.Bundle;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

import follow.twentyfourking.learn_recyleview.adapter.TestAdapter;
import follow.twentyfourking.learn_recyleview.adapter.TestDiffUtils;
import follow.twentyfourking.learn_recyleview.adapter.TestItemBean;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void changeData() {
        List<TestItemBean> oldList = new ArrayList<>();
        List<TestItemBean> newList = new ArrayList<>();
        DiffUtil.Callback callback = new TestDiffUtils(oldList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        TestAdapter testAdapter  = new TestAdapter();
        diffResult.dispatchUpdatesTo(testAdapter);//根据数据的变化更新adapter
        diffResult.dispatchUpdatesTo(new ListUpdateCallback() {
            @Override
            public void onInserted(int position, int count) {

            }

            @Override
            public void onRemoved(int position, int count) {

            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {

            }

            @Override
            public void onChanged(int position, int count, @Nullable Object payload) {

            }
        });
    }
}
