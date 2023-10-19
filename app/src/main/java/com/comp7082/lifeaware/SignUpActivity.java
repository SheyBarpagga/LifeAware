package com.comp7082.lifeaware;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        Button signUp;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUp = (Button) findViewById(R.id.buttonSignUp);
        signUp.setOnClickListener(this);
        Log.d("before intent", "why are you here");

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        EditText nameEdit;
        EditText emailEdit;
        EditText passwordEdit;
        EditText ageEdit;

        String name;
        String email;
        String password;
        String age;

        nameEdit        = (EditText) findViewById(R.id.nameText);
        emailEdit       = (EditText) findViewById(R.id.emailAddressText);
        passwordEdit    = (EditText) findViewById(R.id.passwordText);
        ageEdit         = (EditText) findViewById(R.id.ageText);

        name        = nameEdit.getText().toString();
        email       = emailEdit.getText().toString();
        password    = passwordEdit.getText().toString();
        age         = ageEdit.getText().toString();


        Log.d("email print", email);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference myRef = database.getReference(user.getUid());

                            myRef.child("id").setValue(user.getUid());
                            myRef.child("name").setValue(name);
                            myRef.child("age").setValue(age);

                            finish();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });

    }
}