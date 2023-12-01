package com.comp7082.lifeaware;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientInfo extends Fragment {


    public PatientInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientInfo newInstance(String param1, String param2) {
        return new PatientInfo();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_info, container, false);

        TextView name = view.findViewById(R.id.patient_info_name);
        TextView age = view.findViewById(R.id.patient_info_age);
        TextView notes = view.findViewById(R.id.patient_info_notes);

        Bundle bundle = this.getArguments();

        name.setText(bundle.getString("name"));
        age.setText(bundle.getString("age"));
        notes.setText((CharSequence) "No notes yet!");


        return view;
    }
}