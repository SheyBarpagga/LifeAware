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
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

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
                    mAuth.signOut();
                    replaceFragment(new MainFragment());
                    break;
            }
            return true;
        });


//        Log.d("after intent", "why are you here");
        if (mAuth.getCurrentUser() == null)
        {
            Intent aboutScreen = new Intent(MainActivity.this, SignUpActivity.class);
            this.startActivity(aboutScreen);
        }
//        user = mAuth.getCurrentUser();
////        Log.d("after intent 22222", "why are you here 222");
//
//        DatabaseReference databaseReference = database.getReference(user.getUid().toString());
//
//        Log.d("name stuff", databaseReference.child("name").toString());
//        databaseReference.child("name").toString();
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // this method is call to get the realtime
//                // updates in the data.
//                // this method is called when the data is
//                // changed in our Firebase console.
//                // below line is for getting the data from
//                // snapshot of our database.
//                String value = snapshot.getValue("name");
//
//                // after getting the value we are setting
//                // our value to our text view in below line.
//                Log.d("name stuff", value.toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // calling on cancelled method when we receive
//                // any error or we are not able to get the data.
//                Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}