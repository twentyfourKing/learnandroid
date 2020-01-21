package follow.twentyfourking.learning_arouter.testservice;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/service/bywho")
public class ByWhoServiceImpl implements ByWhoService {
    private Context mContext;

    @Override
    public void byWho(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        String show = stringBuilder.append("Hello ").append(str).toString();
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
        Log.d("Arouter_test", show);
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
