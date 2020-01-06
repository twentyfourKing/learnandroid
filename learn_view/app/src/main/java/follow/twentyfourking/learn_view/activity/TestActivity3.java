package follow.twentyfourking.learn_view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import follow.twentyfourking.learn_view.R;
import follow.twentyfourking.learn_view.view.TrackerLinearLayout;
import follow.twentyfourking.lib_for_demo.activity.BarActivity;

public class TestActivity3 extends AppCompatActivity {
    @BindView(R.id.ll_tracker)
    TrackerLinearLayout mTrackerContainer;
    @BindView(R.id.tv_start_demo)
    TextView mTvDemo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_3);
        ButterKnife.bind(this);
    }

    @OnClick({
            R.id.tv_start_demo
    })
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_start_demo) {
            startActivity(new Intent(this, BarActivity.class));
        }
    }


}
