package follow.twentyfourking.learn_view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ScreenUtils;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import follow.twentyfourking.learn_view.R;
import follow.twentyfourking.learn_view.view.MyLinearLayout1;
import follow.twentyfourking.learn_view.view.MyTextView1;
import follow.twentyfourking.learn_view.view.MyTextView2;

public class TestActivity1 extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.my_tv_1)
    MyTextView1 mMyText1;
    @BindView(R.id.my_tv_2)
    MyTextView2 mMyText2;
    @BindView(R.id.my_linear_1)
    MyLinearLayout1 mLinearLayout1;

    @BindView(R.id.tv_1)
    TextView mTv1;
    @BindView(R.id.tv_2)
    TextView mTv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({
            R.id.tv_1,
            R.id.tv_2,
            R.id.tv_3,
            R.id.tv_4,
            R.id.tv_5,
            R.id.tv_6,
            R.id.tv_7,
            R.id.tv_8,
            R.id.tv_9
    })
    public void onClick(View view) {
        /**
         * 测试逻辑1 - 测试逻辑5 是关于invalidate()方法的
         *
         *
         */
        int id = view.getId();
        if (id == R.id.tv_1) {//测试逻辑1
            mMyText1.invalidate();
        } else if (id == R.id.tv_2) {//测试逻辑2
            mLinearLayout1.invalidate();
        } else if (id == R.id.tv_3) {//测试逻辑3
            mLinearLayout1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mLinearLayout1.invalidate();
        } else if (id == R.id.tv_4) {//测试逻辑4
            mMyText1.setTextColor(getResources().getColor(R.color.colorPrimary));
            mMyText1.invalidate();
        } else if (id == R.id.tv_5) {//测试逻辑5
            ViewGroup.LayoutParams params = mMyText1.getLayoutParams();
            params.width = 200;
            mMyText1.setLayoutParams(params);
            mMyText1.invalidate();
        } else if (id == R.id.tv_6) {//测试逻辑6
            mMyText1.requestLayout();
        } else if (id == R.id.tv_7) {//测试逻辑7
            mLinearLayout1.requestLayout();
        } else if (id == R.id.tv_8) {//测试逻辑8
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLinearLayout1.getLayoutParams();
            params.width = ScreenUtils.getScreenWidth() - 100;
            mLinearLayout1.setLayoutParams(params);
            mLinearLayout1.requestLayout();
        } else if (id == R.id.tv_9) {//测试逻辑9
            Field[] fields = mMyText1.getClass().getFields();
            Log.d("", "");
            for (Field field : fields) {
                String varName = field.getName();
                try {
                    if (varName.equals("mPrivateFlags")) {
                        boolean access = field.isAccessible();
                        if (!access) field.setAccessible(true);
                        Object o = field.get(mMyText1);
                        System.out.println("变量： " + varName + " = " + o);
                        if (!access) field.setAccessible(false);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
}
