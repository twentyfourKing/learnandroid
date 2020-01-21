package follow.twentyfourking.learning_butterknife;


import android.app.Application;

import butterknife.ButterKnife;

public class SimpleApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(BuildConfig.DEBUG);
    }
}
