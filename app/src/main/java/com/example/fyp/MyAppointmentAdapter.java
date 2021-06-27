package com.example.fyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAppointmentAdapter extends RecyclerView.Adapter<MyAppointmentAdapter.MyViewHolder> {

    Context context;
    ArrayList<AppointmentInformation> appointmentInformationArrayList;

    public MyAppointmentAdapter(Context context, ArrayList<AppointmentInformation> appointmentInformationArrayList) {
        this.context = context;
        this.appointmentInformationArrayList = appointmentInformationArrayList;
    }

    @NonNull
    @Override
    public MyAppointmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_appointment,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAppointmentAdapter.MyViewHolder holder, int position) {

        AppointmentInformation appointmentInformation = appointmentInformationArrayList.get(position);

        holder.customerName.setText(appointmentInformation.getCustomerName());
        holder.customerPhone.setText(appointmentInformation.getCustomerPhone());
        holder.time.setText(appointmentInformation.getTime());
        holder.hospitalName.setText(appointmentInformation.getHospitalName());
        holder.doctorName.setText(appointmentInformation.getDoctorName());

    }

    @Override
    public int getItemCount() {
        return appointmentInformationArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView customerName, customerPhone, time, hospitalName, doctorName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            customerName = itemView.findViewById(R.id.customerName);
            customerPhone = itemView.findViewById(R.id.customerPhone);
            time = itemView.findViewById(R.id.time);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            doctorName = itemView.findViewById(R.id.doctorName);
        }
    }
}
