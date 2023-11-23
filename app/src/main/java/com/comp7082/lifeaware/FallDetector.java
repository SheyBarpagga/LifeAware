package com.comp7082.lifeaware;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class FallDetector implements SensorEventListener {

    private static final String CHANNEL_ID = "assistance_notification_channel";
    private static final int NOTIFICATION_ID = 1;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private static final double FALL_THRESHOLD = 2.5f;
    private static final int SHAKE_WAIT_TIME_MS = 500;
    private long mShakeTimestamp;

    public FallDetector(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public void startListening() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopListening() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double netForce = getNetForce(event.values[0], event.values[1], event.values[2]);

            if (netForce < FALL_THRESHOLD) {
                long now = System.currentTimeMillis();

                if (mShakeTimestamp + SHAKE_WAIT_TIME_MS > now) {
                    return;
                }

                mShakeTimestamp = now;

                // Detected a fall! Send a notification.

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // We don't need to do anything here.
    }

    private double getNetForce(float x, float y, float z) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) / SensorManager.GRAVITY_EARTH;
    }

//    private void sendNotificationWithDelay() {
//        // Set a delay before sending the notification (if necessary)
//        new Handler().postDelayed(this::sendNotification, 5000); // 5 second delay
//    }
//
//    private void sendNotification() {
//        // Create an explicit intent for an Activity in your app
//        Intent intent = new Intent(this, AlertDetailsActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_baseline_health_and_safety_24)
//                .setContentTitle("Fall Detected")
//                .setContentText("A fall has been detected. Please check on the patient.")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);  // This line makes the notification cancelable
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//    }
}