package com.comp7082.lifeaware;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.comp7082.lifeaware.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    ActivityMainBinding binding;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference myRef = database.getReference((Objects.requireNonNull(mAuth.getCurrentUser())).getUid());
            myRef.child("isCaretaker").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Boolean caretaker = dataSnapshot.getValue(Boolean.class);
                    if (Boolean.TRUE.equals(caretaker)) {
                        Intent intent = new Intent(MainActivity.this, CaregiverActivity.class);
                        MainActivity.this.startActivity(intent);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            myRef.child("isPatient").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Boolean patient = dataSnapshot.getValue(Boolean.class);
                    if (Boolean.TRUE.equals(patient)) {
                        Intent intent = new Intent(MainActivity.this, PatientActivity.class);
                        MainActivity.this.startActivity(intent);
                        Intent intent2 = new Intent(MainActivity.this, FallDetectionService.class);
                        startForegroundService(intent2);

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser() == null) {
                    Intent signUpScreen = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(signUpScreen);
                }
            }
        });
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new MainFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()) {
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.home:
                    replaceFragment(new MainFragment());
                    break;
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}