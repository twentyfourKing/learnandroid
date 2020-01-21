package follow.twentyfourking.learning_arouter.testservice;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/get/service/orig", name = "orig")
public class HelloServiceImpl implements HelloService {
    Context mContext;

    public void init(Context paramContext) {
        this.mContext = paramContext;
        StringBuilder stringBuilder = new StringBuilder();
        String str2 = HelloService.class.getName();
        String str1 = stringBuilder.append(str2).append(" has init.").toString();
        Log.d("Arouter_test", str1);
    }

    public void sayHello(String paramString) {
        Context context = this.mContext;
        StringBuilder stringBuilder = new StringBuilder();
        String str = stringBuilder.append("Hello ").append(paramString).toString();
        Log.d("Arouter_test", str);
    }
}