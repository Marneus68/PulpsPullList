package fr.iridia.pulpspulllist.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NewsSourceUpdaterService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
