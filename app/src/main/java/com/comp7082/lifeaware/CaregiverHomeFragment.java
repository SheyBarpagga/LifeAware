package com.comp7082.lifeaware;

import static android.content.ContentValues.TAG;

//import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CaregiverHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaregiverHomeFragment extends Fragment implements PatientAdapter.ItemClickListener {


    PatientAdapter adapter;

    Object lockObject = new Object();
    private ArrayList<Patient> patients;
    private List<String> patientNames;
    Patient pat;
    Caregiver caregiver;
    FirebaseDatabase database;



    public CaregiverHomeFragment() {
        // Required empty public constructor
    }

    public static CaregiverHomeFragment newInstance(String param1, String param2) {
        CaregiverHomeFragment fragment = new CaregiverHomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_caregiver_home, container, false);

        database = FirebaseDatabase.getInstance();
        Bundle bundle = this.getArguments();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            caregiver = bundle.getParcelable("caregiver", Caregiver.class);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //pat =  bundle.getParcelable("patients", Patient.class);
            pat = bundle.getParcelable("patient0", Patient.class);
        }
        //System.out.println(pat.getName() + "hello");

        ArrayList<String> test = new ArrayList<String>();
        test.add("test");
        TextView name = view.findViewById(R.id.user_name);
        name.setText(caregiver.getName());
        RecyclerView recyclerView = view.findViewById(R.id.patientList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PatientAdapter(getContext(), test);
        adapter.setClickListener(CaregiverHomeFragment.this);
        recyclerView.setAdapter(adapter);

        getPatients(caregiver.patientIds, bundle);

        patientNames = new ArrayList<>();
        //System.out.println(patients.get(0).getName());
        for (int x = 0; x < patients.size(); x++) {
            Patient p = patients.get(x);
            System.out.println(p.getName() + "efief");
            patientNames.add(p.getName());
            database.getReference(p.getId()).child("help").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        String help = postSnapshot.getValue().toString();
                        Log.d(TAG, "Listening");
                        if (!help.equals("")) {
                            CharSequence text = help + "needs help!";
                            Toast.makeText(getContext().getApplicationContext(), text, Toast.LENGTH_LONG).show();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(patientNames.size() == 0) {
            TextView t = view.findViewById(R.id.nopatients);
            CharSequence charSequence = "Add a patient to get started!";
            t.setText(charSequence);
        }
            //RecyclerView recyclerView = view.findViewById(R.id.patientList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new PatientAdapter(getContext(), patientNames);
            adapter.setClickListener(CaregiverHomeFragment.this);
            recyclerView.setAdapter(adapter);


            Button logoutButton = view.findViewById(R.id.LogoutButton);
            logoutButton.setOnClickListener(v -> {

                mAuth.signOut();
            });

            Button addPatient = view.findViewById(R.id.addPatient);
            addPatient.setOnClickListener(v -> {
                EditText givenId = view.findViewById(R.id.enter_patient_id);
                if (givenId.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "You must enter their ID", Toast.LENGTH_SHORT).show();
                } else {
                    caregiver.addPatientId(givenId.getText().toString());

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(givenId.getText().toString());
                    myRef.child("caregiver").setValue(mAuth.getCurrentUser().getUid());

                    Toast.makeText(getActivity(), "New patient added", Toast.LENGTH_SHORT).show();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.detach(this).attach(this).commit();
                }
            });


            database.getReference(mAuth.getCurrentUser().getUid()).child("help").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        String help = postSnapshot.getValue().toString();
                        Log.d(TAG, "Listening");
                        if (!help.equals("")) {
                            CharSequence text = help + "needs help!";
                            Toast.makeText(getContext().getApplicationContext(), text, Toast.LENGTH_LONG).show();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        return view;
    }


    //KtE8cUTutJciGkrF0e1z00YxDaNj2




    @Override
    public void onItemClick(View view, int position) {
        PatientInfo frag = new PatientInfo();
        Bundle bundle = new Bundle();
        bundle.putString("name", adapter.getItem(position));
        bundle.putString("age", patients.get(position).getAge());
        bundle.putString("notes", patients.get(position).getAge());
        frag.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, frag);
        fragmentTransaction.commit();

    }

    @SuppressLint("StaticFieldLeak")
    private void getPatients(List<String> patientIDs, Bundle bundle) {
        patients = new ArrayList<Patient>();
        Patient patient;
            for(int x = 0; x < patientIDs.size(); x++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    patient = bundle.getParcelable("patient" + x, Patient.class);
                    patients.add(patient);
                }
            }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //addFields(view);

    }



    private synchronized void addFields(View view) {
        while (caregiver == null) {
            try{
                wait();
            }catch (Exception e) {
                System.out.println(e);
            }
        }
        TextView name = view.findViewById(R.id.user_name);
        name.setText(caregiver.getName());

        System.out.println(caregiver.getName() + "gello");

        //getPatients(caregiver.getPatientIds());
        RecyclerView recyclerView = view.findViewById(R.id.patientList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new PatientAdapter(view.getContext(), patientNames);
        adapter.setClickListener(CaregiverHomeFragment.this);
        recyclerView.setAdapter(adapter);
    }

}