package follow.twentyfourking.learn_view.activity;

import android.os.Bundle;
import android.view.VelocityTracker;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import follow.twentyfourking.learn_view.R;

public class TestActivity3 extends AppCompatActivity {
    @BindView(R.id.ll_tracker)
    LinearLayout mTrackerContainer;

    private VelocityTracker mVelocityTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_3);
        ButterKnife.bind(this);
        initTracker();
    }

    private void initTracker() {
        mVelocityTracker = VelocityTracker.obtain();
        mTrackerContainer.
    }
}
