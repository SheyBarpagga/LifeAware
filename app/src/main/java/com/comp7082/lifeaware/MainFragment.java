package com.comp7082.lifeaware;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.telephony.SmsManager;
import android.widget.Toast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.ImageView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class MainFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private Accelerometer accelerometer;
    private Patient patient;
    private static final String CHANNEL_ID = "assistance_notification_channel";


    public MainFragment() {
    }


    public static MainFragment newInstance(String param1, String param2) {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        View view = inflater.inflate(R.layout.fragment_main, container, false);


        Bundle bundle = this.getArguments();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            patient = bundle.getParcelable("patient", Patient.class);
        }


        TextView name = view.findViewById(R.id.user_name);
        name.setText(patient.getName());
        TextView age = view.findViewById(R.id.user_age);
        age.setText(patient.getAge());

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Button logoutButton = view.findViewById(R.id.LogoutButton);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
        });

        ImageView assistanceButton = view.findViewById(R.id.imageView3);
        assistanceButton.setOnClickListener(v -> confirmAssistanceRequest());


        accelerometer = new Accelerometer(getContext());
        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) throws InterruptedException {
                if(ty < -3.5) {
                    System.out.println("Hello you are dropped!");
                    accelerometer.unregister();
                    confirmAssistanceRequest();
                }
                //Thread.sleep(1000);
                accelerometer.register();
            }
        });

        accelerometer.register();


        return view;
    }

    private void confirmAssistanceRequest() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        } else {
            showAssistanceDialog();
        }
    }
    private void showAssistanceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.confirm_request));
        builder.setMessage(getString(R.string.confirm_request_detail));

        final boolean[] userResponded = {false};

        builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> {
            userResponded[0] = true;
            String caregiverPhoneNumber = patient.getCaregiverPhone(); //HAHA Don't Leak my number!
            String assistanceMessage = patient.getName() + "Patient needs Help!";
            sendSMS(caregiverPhoneNumber, assistanceMessage);
        });

        builder.setNegativeButton(getString(R.string.no), (dialog, which) -> {
            userResponded[0] = true;
            Toast.makeText(getContext(), getString(R.string.assistance_cancelled), Toast.LENGTH_SHORT).show();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        new Handler().postDelayed(() -> {
            if (!userResponded[0]) { // Check if user hasn't responded
                dialog.dismiss();
                String caregiverPhoneNumber = "+12222222"; // HAHA Don't Leak my number!
                String assistanceMessage = "Patient needs Help!";
                sendSMS(caregiverPhoneNumber, assistanceMessage);
            }
        }, 5000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showAssistanceDialog();
            } else {
                Toast.makeText(getActivity(), "SMS Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getActivity(), "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "SMS Failed to Send", Toast.LENGTH_SHORT).show();
        }
    }

//        sendNotificationWithDelay();
//        new AlertDialog.Builder(getContext())
//                .setTitle(getString(R.string.confirm_request))
//                .setMessage(getString(R.string.confirm_request_detail))
//                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
//                    // User confirmed to send the notification
//                    sendNotification();
//                })
//                .setNegativeButton(getString(R.string.no), (dialog, which) -> {
//                    // User cancelled the request
//                    Toast.makeText(getContext(), getString(R.string.assistance_cancelled), Toast.LENGTH_SHORT).show();
//                })
//                .show();
//    }
//
//    private void sendNotificationWithDelay() {
//        // Set a delay before sending the notification (if necessary)
//        new Handler().postDelayed(this::sendNotification, 5000); // 5 second delay
//    }
//
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.PatientName); // Define this in strings.xml
//            String description = getString(R.string.Help); // Define this in strings.xml
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    private void sendNotification() {
//        // Show a success message before sending the notification
//        Toast.makeText(getContext(), getString(R.string.notification_sent), Toast.LENGTH_LONG).show();
//
//        CharSequence name = getString(R.string.PatientName); // Define this in strings.xml
//        String description = getString(R.string.Help); // Define this in strings.xml
//        int importance = NotificationManager.IMPORTANCE_HIGH;
//        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(getContext().NOTIFICATION_SERVICE);
//        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//        channel.setDescription(description);
//        notificationManager.createNotificationChannel(channel);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_baseline_health_and_safety_24) // add icon later
//                .setContentTitle(getString(R.string.notification_title))
//                .setContentText(getString(R.string.notification_message))
//                .setPriority(NotificationCompat.PRIORITY_HIGH);
//
//builder.setChannelId(CHANNEL_ID);
//        // Ensure the toast has time to display before the notification is sent and the activity state changes.
//        new Handler().postDelayed(() -> {
//            notificationManager.notify(1, builder.build());
//        }, Toast.LENGTH_LONG);
//    }
}

