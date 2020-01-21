package follow.twentyfourking.learning_arouter;


import android.os.Handler;
import android.os.Looper;

public class MainLooper extends Handler {
    private static MainLooper instance;

    static {
        Looper looper = Looper.getMainLooper();
        MainLooper mainLooper = new MainLooper(looper);
        instance = mainLooper;
    }

    protected MainLooper(Looper paramLooper) {
        super(paramLooper);
    }

    public static MainLooper getInstance() {
        return instance;
    }

    public static void runOnUiThread(Runnable paramRunnable) {
        Looper looper1;
        Looper looper2 = (looper1 = Looper.getMainLooper()).myLooper();
        boolean bool = looper1.equals(looper2);
        if (bool) {
            paramRunnable.run();
            return;
        }
        MainLooper mainLooper = instance;
        mainLooper.post(paramRunnable);
    }
}
