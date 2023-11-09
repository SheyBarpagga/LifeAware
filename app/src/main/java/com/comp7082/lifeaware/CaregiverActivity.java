package com.comp7082.lifeaware;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;


import com.comp7082.lifeaware.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class CaregiverActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    ActivityMainBinding binding;
    FirebaseAuth.AuthStateListener mAuthListener;
    Caregiver cg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new CaregiverHomeFragment());


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()) {
                case R.id.profile:
                    replaceFragment(new CaregiverProfileFragment());
                    break;
                case R.id.home:
                    replaceFragment(new CaregiverHomeFragment());
                    break;
            }
            return true;
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

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground( final Void ... params ) {
                // something you know that will take a few seconds
                cg = new Caregiver();
                return null;
            }
            @Override
            protected void onPostExecute( final Void result ) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putSerializable("caregiver", cg);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.commit();

            }
        }.execute();

    }


}