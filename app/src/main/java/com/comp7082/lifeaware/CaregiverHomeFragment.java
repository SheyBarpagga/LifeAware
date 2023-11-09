package com.comp7082.lifeaware;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CaregiverHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaregiverHomeFragment extends Fragment implements PatientAdapter.ItemClickListener {


    PatientAdapter adapter;


    private List<Patient> patients;
    private List<String> patientNames;

    Caregiver caregiver;



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

        View view = inflater.inflate(R.layout.fragment_caregiver_home, container, false);

        Bundle bundle = this.getArguments();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            caregiver = bundle.getSerializable("caregiver", Caregiver.class);
        }

//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground( final Void ... params ) {
//                // something you know that will take a few seconds
//                caregiver = ;
//                return null;
//            }
//            @Override
//            protected void onPostExecute( final Void result ) {

//            }
//        }.execute();

        TextView name = view.findViewById(R.id.user_name);
        name.setText(caregiver.getName());

        getPatients(caregiver.getPatientIds());
        RecyclerView recyclerView = view.findViewById(R.id.patientList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new PatientAdapter(view.getContext(), patientNames);
        adapter.setClickListener(CaregiverHomeFragment.this);
        recyclerView.setAdapter(adapter);

        Button logoutButton = view.findViewById(R.id.LogoutButton);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
        });

        return view;
    }

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

    private void getPatients(List<String> patientIDs) {
        patients = new ArrayList<Patient>();
        patientNames = new ArrayList<String>();
        for(String patientID: patientIDs) {
            Patient patient = new Patient(patientID);
            patients.add(patient);
            patientNames.add(patient.getName());
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //addFields(view);

    }

//    private void setData(View view) {
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        Runnable createCare = new Runnable() {
//            @Override
//            public void run() {
//
//                System.out.println("done");
//            }
//        };
//
//        Runnable setFields = new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        };
//        executor.(createCare);
//        executor.execute(setFields);
//        executor.shutdown();
//    }


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

        getPatients(caregiver.getPatientIds());
        RecyclerView recyclerView = view.findViewById(R.id.patientList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new PatientAdapter(view.getContext(), patientNames);
        adapter.setClickListener(CaregiverHomeFragment.this);
        recyclerView.setAdapter(adapter);
    }

}