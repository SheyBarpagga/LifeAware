package com.comp7082.lifeaware;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comp7082.lifeaware.controllers.CaregiverActivity;
import com.comp7082.lifeaware.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Boolean isCaretaker;

    EditText nameEdit;
    EditText emailEdit;
    EditText passwordEdit;
    EditText ageEdit;
    TextInputLayout helpLayout;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    private InputValidation inputValidation;

    ActivityMainBinding binding;

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
        inputValidation = new InputValidation();

        View view;
        Button signUpButton;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        SingleSelectToggleGroup single = (SingleSelectToggleGroup) view.findViewById(R.id.group_choices);
        single.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                Log.d(TAG, "onCheckedChanged(): checkedId = " + checkedId);
                if (checkedId ==  R.id.Patient)
                {
                    isCaretaker = false;
                }
                if (checkedId == R.id.buttonCaregiver)
                {
                    isCaretaker = true;
                }
            }
        });



        signUpButton = (Button) view.findViewById(R.id.buttonSignUp);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, String.valueOf(isCaretaker));
                nameEdit        = (EditText) view.findViewById((R.id.nameText));
                emailEdit       = (EditText) view.findViewById((R.id.emailAddressText));
                passwordEdit    = (EditText) view.findViewById((R.id.passwordText));
                ageEdit         = (EditText) view.findViewById((R.id.ageText));
                helpLayout      = (TextInputLayout) view.findViewById(R.id.textInputLayoutSignUp);

                String name         = nameEdit.getText().toString();
                String email        = emailEdit.getText().toString();
                String password     = passwordEdit.getText().toString();
                String age          = ageEdit.getText().toString();

                if (!inputValidation.inputTextFieldEditEmpty(nameEdit,
                        helpLayout,
                        getString(R.string.error_empty_name)))
                {
                    return;
                }

                if (!inputValidation.inputTextFieldEditEmpty(emailEdit,
                        helpLayout,
                        getString(R.string.error_empty_email)))
                {
                    return;
                }

                if (!inputValidation.inputTextEmailEditValid(emailEdit,
                        helpLayout,
                        getString(R.string.error_invalid_email)))
                {
                    return;
                }

                if (!inputValidation.inputTextFieldEditEmpty(passwordEdit,
                        helpLayout,
                        getString(R.string.error_empty_password)))
                {
                    return;
                }

                if (!inputValidation.inputTextPasswordEditValid(passwordEdit,
                        helpLayout,
                        getString(R.string.error_invalid_password)))
                {
                    return;
                }

                if (!inputValidation.inputTextFieldEditEmpty(ageEdit,
                        helpLayout,
                        getString(R.string.error_empty_age)))
                {
                    return;
                }

                if (!inputValidation.inputTextAgeValid(ageEdit,
                        helpLayout,
                        getString(R.string.error_invalid_age)))
                {
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference myRef = database.getReference(user.getUid());

                            myRef.child("id").setValue(user.getUid());
                            myRef.child("name").setValue(name);
                            myRef.child("age").setValue(age);
                            myRef.child("isCaretaker").setValue(isCaretaker);
                            myRef.child("isCaretaker").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Boolean caretaker = dataSnapshot.getValue(Boolean.class);
                                    if (caretaker) {
                                        Intent intent = new Intent(getActivity(), CaregiverActivity.class);
                                        startActivity(intent);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                            getActivity().finish();
                        }else {
                            //display some message here
                            Toast.makeText(getActivity(), "Sign Up failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return view;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}