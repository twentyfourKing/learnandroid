package follow.twentyfourking.lookscaner.scaner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class AndroidObjectBrowserReceiver extends BroadcastReceiver {

    private static final String TAG = "ObjectBrowserReceiver";
    static final String ACTION_KEEP_ALIVE = "io.objectbox.action.KEEP_ALIVE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ACTION_KEEP_ALIVE.equals(intent.getAction())) {
            return;
        }
        if (!intent.hasExtra(AndroidObjectBrowserService.EXTRA_KEY_URL)) {
            Log.w(TAG, "Ignoring keep alive intent due to incomplete data");
            return;
        }

        // start foreground service to keep app process alive
        Intent serviceIntent = new Intent(context, AndroidObjectBrowserService.class);
        serviceIntent.putExtras(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }

        // launch browser
        String url = intent.getStringExtra(AndroidObjectBrowserService.EXTRA_KEY_URL);
        context.startActivity(AndroidObjectBrowser.viewIntent(url));
    }

}
