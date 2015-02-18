package fr.iridia.pulpspulllist.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BlogUpdaterService extends Service {

    public static final String TAG = "BlogUpdaterService";

    protected NotificationManager notificationManager;
    protected int NOTIFICATION = 999989;

    private final IBinder binder = new BlogUpdaterBinder();

    public class BlogUpdaterBinder extends Binder {
        BlogUpdaterService getService() {
            return BlogUpdaterService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service stopped.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
