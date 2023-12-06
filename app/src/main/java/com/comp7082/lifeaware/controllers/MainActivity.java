package com.comp7082.lifeaware.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.comp7082.lifeaware.MainFragment;
import com.comp7082.lifeaware.ProfileFragment;
import com.comp7082.lifeaware.R;
import com.comp7082.lifeaware.SignUpActivity;
import com.comp7082.lifeaware.databinding.ActivityMainBinding;
import com.comp7082.lifeaware.models.Patient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    ActivityMainBinding binding;
    FirebaseAuth.AuthStateListener mAuthListener;


    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final int profile = R.id.profile;
        final int home = R.id.home;



        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()) {
                case profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case home:
                    replaceFragment(new MainFragment());
                    break;
            }
            return true;
        });

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser() == null) {
                    Intent signUpScreen = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(signUpScreen);
                }
            }
        });

        if(mAuth.getCurrentUser() != null) {
            DatabaseReference myRef = database.getReference(mAuth.getCurrentUser().getUid());
            myRef.child("isCaretaker").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Boolean caretaker = dataSnapshot.getValue(Boolean.class);
                    if (caretaker) {
                        Intent intent = new Intent(MainActivity.this, CaregiverActivity.class);
                        MainActivity.this.startActivity(intent);
                    } else {
                        replaceFragment(new MainFragment());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


        }


    }

    @SuppressLint("StaticFieldLeak")
    private void replaceFragment(Fragment fragment) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground( final Void ... params ) {
                // something you know that will take a few seconds
                patient =  new Patient();
                return null;
            }
            @Override
            protected void onPostExecute( final Void result ) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putParcelable("patient", patient);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.commit();

            }
        }.execute();

    }
}