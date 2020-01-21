package follow.twentyfourking.learning_arouter.testservice;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.DegradeService;

@Route(path = "/service/degrade")
public class DegradeServiceImpl implements DegradeService {
    @Override
    public void onLost(Context context, Postcard postcard) {
        // do something.t6
//        postcard.setPath("/test/webview");
//        postcard.navigation();
        Log.d("Arouter_test","");
    }

    @Override
    public void init(Context context) {

    }
}
