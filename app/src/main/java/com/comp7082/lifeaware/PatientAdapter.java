package com.comp7082.lifeaware;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.comp7082.lifeaware.models.Patient;

import java.util.List;

public class PatientAdapter extends
        RecyclerView.Adapter<PatientAdapter.ViewHolder> {

    private List<Patient> data;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    // data is passed into the constructor
    PatientAdapter(Context context, List<Patient> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.patient_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.patient_name.setText(data.get(position).getName());
        holder.patient_age.setText(data.get(position).getAge());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return data.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView patient_name;
        TextView patient_age;

        ViewHolder(View itemView) {
            super(itemView);
            patient_name = itemView.findViewById(R.id.patient_name_list);
            patient_age = itemView.findViewById(R.id.patient_age_list);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }


    //String getItem(int id) {
    //    return data.get(id);
    //}


    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}