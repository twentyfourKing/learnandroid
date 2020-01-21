package follow.twentyfourking.learning_arouter.testservice;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;

@Route(path="/service/single")
public class SingleService implements IProvider {
    Context mContext;

    public void init(Context paramContext) {
        this.mContext = paramContext;
    }

    public void sayHello(String paramString) {
        Context context = this.mContext;
        StringBuilder stringBuilder = new StringBuilder();
        String str = stringBuilder.append("Hello ").append(paramString).toString();
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
