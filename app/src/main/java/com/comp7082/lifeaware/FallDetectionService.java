package com.comp7082.lifeaware;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import android.hardware.SensorManager;


public class FallDetectionService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private FallDetector fallDetector;

    private static final String CHANNEL_ID = "service_notification_channel";


    @Override
    public void onCreate() {
        super.onCreate();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        fallDetector = new FallDetector(sensorManager);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Fall Detection Service")
                .setContentText("Running...")
                .setContentIntent(pendingIntent)
                .build();

        startForeground(NOTIFICATION_ID, notification);

        fallDetector.startListening();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        fallDetector.stopListening();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
