package com.comp7082.lifeaware;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comp7082.lifeaware.models.Caregiver;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CaregiverProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaregiverProfileFragment extends Fragment {

    Caregiver caregiver;

    public CaregiverProfileFragment() {
        // Required empty public constructor
    }


    public static CaregiverProfileFragment newInstance(String param1, String param2) {
        CaregiverProfileFragment fragment = new CaregiverProfileFragment();

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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_caregiver_profile, container, false);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground( final Void ... params ) {
                // something you know that will take a few seconds
                caregiver = new Caregiver();
                return null;
            }
            @Override
            protected void onPostExecute( final Void result ) {
                TextView name = view.findViewById(R.id.user_name);
                name.setText(caregiver.getName());

            }
        }.execute();

        return view;
    }
}