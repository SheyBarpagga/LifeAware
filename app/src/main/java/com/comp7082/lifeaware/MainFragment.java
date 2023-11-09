package com.comp7082.lifeaware;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
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


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.ImageView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private Patient patient;
    private static final String CHANNEL_ID = "assistance_notification_channel";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground( final Void ... params ) {
                // something you know that will take a few seconds
                patient = new Patient();
                return null;
            }
            @Override
            protected void onPostExecute( final Void result ) {

                TextView name = view.findViewById(R.id.user_name);
                name.setText(patient.getName());
                TextView age = view.findViewById(R.id.user_age);
                age.setText(patient.getAge());

            }
        }.execute();

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Button logoutButton = view.findViewById(R.id.LogoutButton);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
        });

        ImageView assistanceButton = view.findViewById(R.id.imageView3);
        assistanceButton.setOnClickListener(v -> confirmAssistanceRequest());

        return view;
    }

    private void confirmAssistanceRequest() {
        sendNotificationWithDelay();
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.confirm_request))
                .setMessage(getString(R.string.confirm_request_detail))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    // User confirmed to send the notification
                    sendNotification();
                })
                .setNegativeButton(getString(R.string.no), (dialog, which) -> {
                    // User cancelled the request
                    Toast.makeText(getContext(), getString(R.string.assistance_cancelled), Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void sendNotificationWithDelay() {
        // Set a delay before sending the notification (if necessary)
        new Handler().postDelayed(this::sendNotification, 5000); // 5 second delay
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.PatientName); // Define this in strings.xml
            String description = getString(R.string.Help); // Define this in strings.xml
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification() {
        // Show a success message before sending the notification
        Toast.makeText(getContext(), getString(R.string.notification_sent), Toast.LENGTH_LONG).show();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_health_and_safety_24) // add icon later
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_message))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        // Ensure the toast has time to display before the notification is sent and the activity state changes.
        new Handler().postDelayed(() -> {
            notificationManager.notify(1, builder.build());
        }, Toast.LENGTH_LONG);
    }
}

