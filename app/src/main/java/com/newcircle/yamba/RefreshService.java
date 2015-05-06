package com.newcircle.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.thenewcircle.yamba.client.YambaClient;
import com.thenewcircle.yamba.client.YambaClientException;
import com.thenewcircle.yamba.client.YambaStatus;

import java.util.List;

/**
 * Created by geoff on 5/5/15.
 */
public class RefreshService extends IntentService {
    private static final String TAG = RefreshService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 101;
    private NotificationManagerCompat mNotificationManager;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public RefreshService() {
        super("Refresh Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        YambaClient yambaClient = new YambaClient("student", "password");
        try {
            List<YambaStatus> timeline = yambaClient.getTimeline(20);
            ContentValues values = new ContentValues();
            int count = 0;

            for(YambaStatus status : timeline) {
                values.clear();
                values.put(StatusContract.Column.ID, status.getId());
                values.put(StatusContract.Column.USER, status.getUser());
                values.put(StatusContract.Column.MESSAGE, status.getMessage());
                values.put(StatusContract.Column.CREATED_AT, status.getCreatedAt().getTime());

                Uri uri = getContentResolver().insert(StatusContract.CONTENT_URI, values);
                if(uri != null) {
                    Log.d(TAG, String.format("%d %s %s", status.getId(), status.getUser(),
                            status.getMessage()));
                    ++count;
                }
                else {
                    Log.d(TAG, String.format("ERROR: %d %s %s", status.getId(), status.getUser(),
                            status.getMessage()));
                }
            }
            if(count > 0 ) postStatusNotification(count);
        } catch (YambaClientException e) {
            Log.e(TAG, "Unable to get Timeline", e);
        }
    }

    private void postStatusNotification(int count) {
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("New Tweets!")
                .setContentText("You've got " + count + " new tweets")
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setAutoCancel(true)
                .build();
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

}
