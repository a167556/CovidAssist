package com.example.fyp;

import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class AdminSumbangan extends AppCompatActivity {

    ConstraintLayout expandablView;
    Button arrowBtn, acceptButton, declinedButton;
    CardView cardView;
    ImageView imgload;

    RecyclerView recViewStatus;
    AdminStatusAdapter adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_sumbangan);

        recViewStatus =findViewById(R.id.recViewStatus);
        recViewStatus.setLayoutManager(new LinearLayoutManager(this));
//        progressBar = (ProgressBar) findViewById(R.id.progressBarStatusBorang);
//        progressBar.setVisibility(View.VISIBLE);
        FirebaseRecyclerOptions<UserHelperClass> options =
                new FirebaseRecyclerOptions.Builder<UserHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Sumbangan"), UserHelperClass.class)
                        .build();


        adapter= new AdminStatusAdapter(options);
        recViewStatus.setAdapter(adapter);
//        progressBar.setVisibility(View.GONE);



        expandablView = findViewById(R.id.expandableview);
        arrowBtn = findViewById(R.id.arrowBtn);
        cardView = findViewById(R.id.cardViewStart);


        acceptButton = findViewById(R.id.btnAccept);
        declinedButton = findViewById(R.id.declinebtn);
        imgload =  findViewById(R.id.imageLoad);

        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (expandablView.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandablView.setVisibility(View.VISIBLE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandablView.setVisibility(View.GONE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
            }
        });
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgload.setBackgroundResource(R.drawable.ic_baseline_check_24);
            }
        });
        declinedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgload.setImageResource(R.drawable.rejected);
            }
        });
    }
}