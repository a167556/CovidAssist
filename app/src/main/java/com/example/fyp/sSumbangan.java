package com.example.fyp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class sSumbangan extends Fragment {
    private Chip chipSumbangan, chipAppointment;
    TextView username,date,ditolak;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    CardView progressCountParent, progressCountParent2,progressCountParent3;
    Toolbar toolbar1;
    ImageView imgwrong;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_sumbangan, container, false);

        chipSumbangan = view.findViewById(R.id.chipSumbangan);
        chipAppointment = view.findViewById(R.id.chipAppointment);

        progressCountParent = view.findViewById(R.id.progressCountParent);
        progressCountParent2 = view.findViewById(R.id.progressCountParent2);
        progressCountParent3 = view.findViewById(R.id.progressCountParent3);
        ditolak = view.findViewById(R.id.textView7);
        imgwrong = view.findViewById(R.id.truetick);
        date = view.findViewById(R.id.dateTv);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Sumbangan");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (dataSnapshot.child("uid").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        if (dataSnapshot.child("status").getValue().equals("dihantar")){
                            progressCountParent.setCardBackgroundColor(Color.parseColor("#866BDF"));
                            date.setText(dataSnapshot.child("date").getValue(String.class));
                        } else if (dataSnapshot.child("status").getValue().equals("diterima")){
                            progressCountParent.setCardBackgroundColor(Color.parseColor("#866BDF"));
                            progressCountParent2.setCardBackgroundColor(Color.parseColor("#866BDF"));
                            progressCountParent3.setCardBackgroundColor(Color.parseColor("#866BDF"));
                            date.setText(dataSnapshot.child("date").getValue(String.class));
                        }
                        else if (dataSnapshot.child("status").getValue().equals("ditolak")){
                            progressCountParent.setCardBackgroundColor(Color.parseColor("#e50000"));
                            imgwrong.setImageResource(R.drawable.ic_baseline_remove_24);
                            ditolak.setText("Your application is rejected");
                            date.setText(dataSnapshot.child("date").getValue(String.class));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Sumbangan").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        username = view.findViewById(R.id.userName);
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setText(snapshot.child("fullName").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        progressBar = (ProgressBar) findViewById(R.id.progressBarStatusBorang);
//        progressBar.setVisibility(View.VISIBLE);

        return view;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chipAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipSumbangan.setChecked(false);
                chipAppointment.setChecked(true);
                Fragment sAppointment = new sAppointment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(((ViewGroup)getView().getParent()).getId(), sAppointment,null )
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

}
