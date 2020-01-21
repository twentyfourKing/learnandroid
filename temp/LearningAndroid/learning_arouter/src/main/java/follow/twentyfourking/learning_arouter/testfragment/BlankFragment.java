package follow.twentyfourking.learning_arouter.testfragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;

import follow.twentyfourking.learning_arouter.testinject.TestObj;

@Route(path = "/test/fragment")
public class BlankFragment extends Fragment {
    @Autowired
    String name;
    @Autowired
    TestObj obj;

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        FragmentActivity fragmentActivity = getActivity();
        TextView textView = new TextView(fragmentActivity);
        return textView;
    }
}