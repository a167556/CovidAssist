package com.example.fyp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminDoctorActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<AppointmentInformation> appointmentInformationArrayList;
    MyAppointmentAdapter myAppointmentAdapter;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    Button buttonSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_doctor);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerView =findViewById(R.id.recyclerViewAppointment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
        appointmentInformationArrayList = new ArrayList<AppointmentInformation>();
        myAppointmentAdapter = new MyAppointmentAdapter(AdminDoctorActivity.this,appointmentInformationArrayList);

        recyclerView.setAdapter(myAppointmentAdapter);

        EventChangeListener();

        buttonSignOut = findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AdminDoctorActivity.this,Login.class);
                startActivity(intent);
            }
        });
    }

    private void EventChangeListener() {

        firebaseFirestore.collection("Appointment History").orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error !=  null){

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            Log.e("Database error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){

                            if (dc.getType() == DocumentChange.Type.ADDED){
                                appointmentInformationArrayList.add(dc.getDocument().toObject(AppointmentInformation.class));

                            }
                            myAppointmentAdapter.notifyDataSetChanged();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                });
    }
}