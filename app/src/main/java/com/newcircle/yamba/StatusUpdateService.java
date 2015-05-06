package com.newcircle.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.thenewcircle.yamba.client.YambaClient;
import com.thenewcircle.yamba.client.YambaClientException;

/**
 * Created by geoff on 5/5/15.
 */
public class StatusUpdateService extends IntentService {
    private static final String TAG = StatusUpdateService.class.getSimpleName();
    public static final String EXTRA_MESSAGE = "com.newcircle.StatusUpdate.Message";
    private static final int NOTIFICATION_ID = 100;
    private NotificationManagerCompat mNotificationManager;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public StatusUpdateService() {
        super("Status Update Service");
        Log.d(TAG, "constructor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent " + intent);
        final String status = intent.getStringExtra(EXTRA_MESSAGE);

        postProgressNotification(status);

        final YambaClient yambaClient = new YambaClient("student", "password");
        try {
            Thread.sleep(5000);
            yambaClient.postStatus(status);
            mNotificationManager.cancel(NOTIFICATION_ID);
        } catch (YambaClientException e) {
            Log.e(TAG, "Unable to post " + status, e);

            postErrorNotification(status);
        } catch (InterruptedException e) {


        }
    }

    private void postProgressNotification(String message) {
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Posting Status")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Status notification in progress: " + message))
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setOngoing(true)
                .build();
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void postErrorNotification(String originalMessage) {
        Intent intent = new Intent(this, StatusActivity.class);
        intent.putExtra(EXTRA_MESSAGE, originalMessage);

        PendingIntent operation = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Post Error")
                .setContentText("Error Posting status update. Tap to try again.")
                .setContentIntent(operation)
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setAutoCancel(true)
                .build();
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mNotificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
