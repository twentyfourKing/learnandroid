package follow.twentyfourking.lookscaner.scaner;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import follow.twentyfourking.lookscaner.R;

/**
 * Foreground service to keep app alive with a notification to view object browser URL or stop
 * the service.
 */
public class AndroidObjectBrowserService extends Service {

    private static final String ACTION_STOP = "objectBox_objectBrowserStop";

    static final String EXTRA_KEY_PORT = "port";
    static final String EXTRA_KEY_URL = "url";
    static final String EXTRA_KEY_NOTIFICATION_ID = "notificationId";

    private static final String TAG = "ObjectBrowserService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_STOP.equals(intent.getAction())) {
            Log.d(TAG, "Stopping");
            stopForeground(true);
            stopSelf();
            return START_NOT_STICKY;
        }
        String url = intent.getStringExtra(EXTRA_KEY_URL);
        int port = intent.getIntExtra(EXTRA_KEY_PORT, 0);
        int notificationId = intent.getIntExtra(EXTRA_KEY_NOTIFICATION_ID, 0);

        if (url != null && url.startsWith("http") && port > 0 && notificationId > 0) {
            Intent stopIntent = new Intent(this, getClass());
            stopIntent.setAction(ACTION_STOP);
            PendingIntent stopPendingIntent = PendingIntent.getService(this, 0, stopIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    AndroidObjectBrowser.viewIntent(url), 0);

            NotificationManager manager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder =
                    AndroidObjectBrowser.buildBaseNotification(this, port, manager);
            builder.setContentIntent(pendingIntent);
            // Actually useless because Foreground notifications cannot be deleted
            builder.setDeleteIntent(stopPendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                builder.addAction(
                        new Notification.Action.Builder(R.drawable.objectbox_stop, "Stop", stopPendingIntent).build()
                );
            }

            startForeground(notificationId, builder.getNotification());
            Log.d(TAG, "Started");
            return START_REDELIVER_INTENT; // with START_STICKY would not get intent on restart
        } else {
            Log.w(TAG, "Ignoring start command due to incomplete data");
            return START_NOT_STICKY;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
