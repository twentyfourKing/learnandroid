package follow.twentyfourking.learning_arouter.testinterceptor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

import follow.twentyfourking.learning_arouter.MainActivity;
import follow.twentyfourking.learning_arouter.MainLooper;

@Interceptor(priority = 8, name = "test interceptor")
public class Test1Interceptor implements IInterceptor {
    Context mContext;

    public void init(Context pContext) {
        this.mContext = pContext;
        StringBuilder stringBuilder = new StringBuilder();
        String str2 = Test1Interceptor.class.getName();
        String str1 = stringBuilder.append(str2).append(" has init.").toString();
        Log.e("testService", str1);
    }

    public void process(final Postcard postcard, final InterceptorCallback interceptorCallback) {
        boolean bool = "/test/activity4".equals(postcard.getPath());
        if (bool) {
            Activity activity = MainActivity.getThis();
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setCancelable(false);
            builder.setTitle("温馨提醒");
            builder.setMessage("想要跳转到Test4Activity触发了（\"/inter/test1\"拦截器，拦截本次跳转)");
            builder.setNegativeButton("继续", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    interceptorCallback.onContinue(postcard);
                }
            });
            builder.setNeutralButton("算了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    interceptorCallback.onInterrupt(null);
                }
            });
            builder.setPositiveButton("加点料", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    postcard.withString("extra", "我是在拦截器中附加的参数");
                    interceptorCallback.onContinue(postcard);
                }
            });
            MainLooper.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    builder.create().show();
                }
            });
            return;
        }
        interceptorCallback.onContinue(postcard);
    }
}