package com.newcircle.yamba;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by geoff on 5/8/15.
 */
public class YambaApp extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = YambaApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        updateInterval(prefs);
        prefs.registerOnSharedPreferenceChangeListener(this);

    }

    private void updateInterval(SharedPreferences prefs) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        long interval = Long.parseLong(prefs.getString("interval", "0"));
        Log.d(TAG, "refresh interval: " + interval);
        PendingIntent operation = PendingIntent.getService(this, -1,
                new Intent(this, RefreshService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        if(interval == 0) {
            alarmManager.cancel(operation);
        }
        else {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, interval,
                    operation);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("interval")) {
            updateInterval(sharedPreferences);
        }
    }
}
