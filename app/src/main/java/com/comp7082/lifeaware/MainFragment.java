package com.comp7082.lifeaware;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.comp7082.lifeaware.models.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import android.telephony.SmsManager;

import android.content.Context;
import android.os.Build;
import android.widget.ImageView;

public class MainFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private Accelerometer accelerometer;
    private Patient patient;



    public MainFragment() {
    }


    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
            public void onFallDetected() {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "Fall detected!", Toast.LENGTH_SHORT).show();
                    confirmAssistanceRequest();
                });
            }
        });

        accelerometer.register();


        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        accelerometer.register();
    }

    @Override
    public void onPause() {
        super.onPause();
        accelerometer.unregister();
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
            String caregiverPhoneNumber = patient.getCaregiverPhone();
            getLocationAndSendSMS(caregiverPhoneNumber);
        });

        builder.setNegativeButton(getString(R.string.no), (dialog, which) -> {
            userResponded[0] = true;
            Toast.makeText(getContext(), getString(R.string.assistance_cancelled), Toast.LENGTH_SHORT).show();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        new Handler().postDelayed(() -> {
            if (!userResponded[0]) {
                dialog.dismiss();
                String caregiverPhoneNumber = patient.getCaregiverPhone();
                getLocationAndSendSMS(caregiverPhoneNumber);
            }
        }, 5000);
    }

    private void getLocationAndSendSMS(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        locationManager.removeUpdates(this);
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        String assistanceMessage = patient.getName() + " needs help! \nTheir Location: \nhttp://maps.google.com/maps?q=" + latitude + "," + longitude;
                        sendSMS(phoneNumber, assistanceMessage);
                    }
                });
            } else {
                Toast.makeText(getActivity(), "GPS not enabled", Toast.LENGTH_SHORT).show();
            }
        }
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

        } else if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndSendSMS(patient.getCaregiverPhone());
            } else {
                Toast.makeText(getActivity(), "Location Permission Denied", Toast.LENGTH_SHORT).show();
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
}

