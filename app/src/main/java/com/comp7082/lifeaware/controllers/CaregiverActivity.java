package com.comp7082.lifeaware.controllers;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.comp7082.lifeaware.CaregiverHomeFragment;
import com.comp7082.lifeaware.CaregiverProfileFragment;
import com.comp7082.lifeaware.R;
import com.comp7082.lifeaware.SignUpActivity;
import com.comp7082.lifeaware.databinding.ActivityMainBinding;
import com.comp7082.lifeaware.models.Caregiver;
import com.comp7082.lifeaware.models.Patient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CaregiverActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    ActivityMainBinding binding;
    FirebaseAuth.AuthStateListener mAuthListener;
    Caregiver cg;
    ArrayList<Patient> patients;
    Bundle bundle;
    Patient pat;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new CaregiverHomeFragment());

        final int profile = R.id.profile;
        final int home = R.id.home;

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()) {
                case profile:
                    replaceFragment(new CaregiverProfileFragment());
                    break;
                case home:
                    replaceFragment(new CaregiverHomeFragment());
                    break;
            }
            return true;
        });
        database = FirebaseDatabase.getInstance();



        database.getReference(mAuth.getCurrentUser().getUid()).child("help").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String help = postSnapshot.getValue().toString();
                    Log.d(TAG, "Listening");
                    if(!help.equals("")) {
                        CharSequence text = help + "needs help!";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser() == null) {
                    Intent signUpScreen = new Intent(CaregiverActivity.this, SignUpActivity.class);
                    startActivity(signUpScreen);
                }
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    private void replaceFragment(Fragment fragment) {
        bundle = new Bundle();
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground( final Void ... params ) {
                // something you know that will take a few seconds
                cg = new Caregiver();
                //pat = new Patient("JFCwB4lc6UhfzjmXANE1RUSAe7h1");
                return null;
            }
            @Override
            protected void onPostExecute( final Void result ) {
                System.out.println(cg.getPatientIds());

                if(cg.getPatientIds() == null || cg.getPatientIds().size() == 0) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    //Bundle bundle = new Bundle();
                    bundle.putParcelable("caregiver", cg);
                    //bundle.putParcelable("patients", pat);
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame_layout, fragment);
                    fragmentTransaction.commit();
                }

                for(String patientID: cg.getPatientIds()) {
                    new AsyncTask<Void, Void, Void>() {
                        Patient patient;

                        @Override
                        protected Void doInBackground( final Void ... params ) {
                            patient = new Patient(patientID);
                            //patients.add(patient);
                            return null;
                        }
                        @Override
                        protected void onPostExecute( final Void result ) {
                            bundle.putParcelable("patient" + count, patient);
                            count++;
                            if(count == cg.getPatientIds().size()) {
                                count = 0;
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                bundle.putParcelable("caregiver", cg);
                                fragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.frame_layout, fragment);
                                fragmentTransaction.commit();
                            }
                        }
                    }.execute();

                }
            }
        }.execute();

    }


}