package com.example.careify2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Patient_RecyclerViewAdapter extends RecyclerView.Adapter<Patient_RecyclerViewAdapter.MyViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<PatientModel> patientModels;
    public  Patient_RecyclerViewAdapter(Context context, ArrayList<PatientModel> patientModels, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.patientModels = patientModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public Patient_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_card, parent, false);
        return new Patient_RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Patient_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.patientName.setText(patientModels.get(position).getPatientName());
    }

    @Override
    public int getItemCount() {
        return patientModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView patientName;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            patientName = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onCardClick(pos);
                        }
                    }
                }
            });
        }
    }
}
