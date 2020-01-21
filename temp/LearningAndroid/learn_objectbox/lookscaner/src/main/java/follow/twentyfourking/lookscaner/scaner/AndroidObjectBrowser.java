package follow.twentyfourking.lookscaner.scaner;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import follow.twentyfourking.lookscaner.R;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AndroidObjectBrowser {

    private static final String TAG = "ObjectBrowser";
    private static final String NOTIFICATION_CHANNEL_ID = "objectbox-browser";

    private int notificationId;

    public AndroidObjectBrowser() {

    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    /**
     * Starts ObjectBox data browser and displays a notification to launch the browser view. When
     * tapped, launches a foreground service to keep the app alive. Stop it from its notification.
     */
    public boolean start(Context context) {

        // compare with objectbox-android-objectbrowser/src/main/AndroidManifest.xml
        // SecurityException if no INTERNET permission
        context.enforcePermission(Manifest.permission.INTERNET, Process.myPid(), Process.myUid(), null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
                && context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.P) {
            // SecurityException if no FOREGROUND_SERVICE permission
            context.enforcePermission(Manifest.permission.FOREGROUND_SERVICE, Process.myPid(), Process.myUid(), null);
        }

//        int alreadyRunningPort = boxStore.getObjectBrowserPort();
//        if (alreadyRunningPort != 0) {
//            Log.w(TAG, "ObjectBrowser is already running at port " + alreadyRunningPort);
//            return false;
//        }
//        String url = boxStore.startObjectBrowser();
//        if (url == null) {
//            return false;
//        }
//        Log.i(TAG, "ObjectBrowser started: " + url);
//        int port = boxStore.getObjectBrowserPort();
//        Log.i(TAG, "Command to forward ObjectBrowser to connected host: adb forward tcp:" + port + " tcp:" + port);


//        if (notificationId == 0) {
//            notificationId = 19770000 + port;
//        }

        // build intent and show notification
        Intent intent = new Intent(context, AndroidObjectBrowserReceiver.class);
        intent.setAction(AndroidObjectBrowserReceiver.ACTION_KEEP_ALIVE);
        intent.putExtra(AndroidObjectBrowserService.EXTRA_KEY_URL, "www.");
        intent.putExtra(AndroidObjectBrowserService.EXTRA_KEY_PORT, 8090);
        intent.putExtra(AndroidObjectBrowserService.EXTRA_KEY_NOTIFICATION_ID, notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = buildBaseNotification(context, 8090, manager);
        builder.setContentIntent(pendingIntent);
        if (manager != null) {
            manager.notify(notificationId, builder.getNotification());
        }

        return true;
    }

    static Notification.Builder buildBaseNotification(Context context, int port, NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "ObjectBox Browser", NotificationManager.IMPORTANCE_DEFAULT);
            // if channel already exists, create call will be ignored
            manager.createNotificationChannel(channel);
        }

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID);
        } else {
            builder = new Notification.Builder(context);
        }

        builder.setContentTitle("BrowserNotificationTitle")
                .setContentText("Running on Port 8090")
                .setSmallIcon(R.mipmap.ic_launcher);

        return builder;
    }

    static Intent viewIntent(String url) {
        Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        viewIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        return viewIntent;
    }
}
