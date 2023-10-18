package com.comp7082.lifeaware;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText nameEdit;
    EditText emailEdit;
    EditText passwordEdit;
    EditText ageEdit;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        View view;
        Button signUpButton;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        signUpButton = (Button) view.findViewById(R.id.buttonSignUp);




        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEdit   = (EditText) view.findViewById((R.id.nameText));
                emailEdit       = (EditText) view.findViewById((R.id.emailAddressText));
                passwordEdit    = (EditText) view.findViewById((R.id.passwordText));
                ageEdit         = (EditText) view.findViewById((R.id.ageText));

                String name         = nameEdit.getText().toString();
                String email        = emailEdit.getText().toString();
                String password     = passwordEdit.getText().toString();
                String age          = ageEdit.getText().toString();
                Log.d("this is the email", email);
                Log.d("this is the name", name);
                Log.d("this is the password", password);
                Log.d("this is the age", age);
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference myRef = database.getReference(user.getUid());

                            myRef.child("id").setValue(user.getUid());
                            myRef.child("name").setValue(name);
                            myRef.child("age").setValue(age);

                            getActivity().finish();
                        }else {
                            //display some message here
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
//        FirebaseAuth mAuth;
//        FirebaseDatabase database;
//
//        mAuth = FirebaseAuth.getInstance();
//        database = FirebaseDatabase.getInstance();
//
////        EditText nameEdit;
////        EditText emailEdit;
////        EditText passwordEdit;
////        EditText ageEdit;
//
//        nameEdit        = (EditText) view.findViewById((R.id.nameText));
//        emailEdit       = (EditText) view.findViewById((R.id.emailAddressText));
//        passwordEdit    = (EditText) view.findViewById((R.id.passwordText));
//        ageEdit         = (EditText) view.findViewById((R.id.ageText));
//
//        mAuth.createUserWithEmailAndPassword(emailEdit.getText().toString(), passwordEdit.getText().toString())
//                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            DatabaseReference myRef = database.getReference(user.getUid());
//
//                            myRef.child("id").setValue(user.getUid());
//                            myRef.child("name").setValue(nameEdit.getText().toString());
//                            myRef.child("age").setValue(ageEdit.getText().toString());
//
//                            getActivity().finish();
////                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
////                            Toast.makeText(getActivity(),"Toast your message" ,Toast.LENGTH_SHORT).show();
////                            updateUI(null);
//                        }
//                    }
//                });
    }
}