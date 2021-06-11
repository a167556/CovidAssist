package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class AAppointmentMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
//    NavigationView navigationView;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    TextView txt_name_user, txt_email_user;

    LocalBroadcastManager localBroadcastManager;
    AlertDialog dialog;
    CollectionReference doctorRef;

    @BindView(R.id.step_view)
    StepView stepView;
    @BindView(R.id.view_pager)
    NonSwipeViewPager viewPager;
    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;
    @BindView(R.id.btn_next_step)
    Button btn_next_step;
    @BindView(R.id.nav_view)
    NavigationView nav_view;

    @OnClick(R.id.btn_previous_step)
    void previousStep(){
        if(Common.step == 3 || Common.step > 0){
            Common.step--;
            viewPager.setCurrentItem(Common.step);

            if (Common.step<3){
                btn_next_step.setEnabled(true);
                setColorButton();
            }
        }
    }

    @OnClick(R.id.btn_next_step)
    void nextClick(){
        if (Common.step <3 || Common.step == 0){
            Common.step++;
            if (Common.step == 1){
                if (Common.currentHospital != null){
                    loadDoctorByHospital(Common.currentHospital.getHospitalID());
                }
            }
            else if(Common.step == 2){
                if (Common.currentDoctor != null){
                    loadTimeSlotOfDoctor(Common.currentDoctor.getDoctorID());
                }
            }
            else if(Common.step == 3){
                if (Common.currentTimeSlot != -1){
                    confirmAppointment();
                }
            }

            viewPager.setCurrentItem(Common.step);
        }
    }

    private void confirmAppointment() {
        Intent intent = new Intent(Common.KEY_CONFIRM_APPOINTMENT);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadTimeSlotOfDoctor(String doctorID) {
        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadDoctorByHospital(String hospitalID) {
        dialog.show();


        if (!TextUtils.isEmpty(Common.state)){
            doctorRef = FirebaseFirestore.getInstance()
                    .collection("AllState")
                    .document(Common.state)
                    .collection("Hospital")
                    .document(hospitalID)
                    .collection("Doctor");

            doctorRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<Doctor> doctors = new ArrayList<>();
                            for (QueryDocumentSnapshot doctorSnapshot:task.getResult()){
                                Doctor doctor = doctorSnapshot.toObject(Doctor.class);
                                doctor.setDoctorID(doctorSnapshot.getId());

                                doctors.add(doctor);
                            }
                            Intent intent = new Intent(Common.KEY_DOCTOR_LOAD_DONE);
                            intent.putParcelableArrayListExtra(Common.KEY_DOCTOR_LOAD_DONE, doctors);
                            localBroadcastManager.sendBroadcast(intent);

                            dialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                        }
                    });
        }

    }

    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int step = intent.getIntExtra(Common.KEY_STEP,0);
            if (step == 1)
                Common.currentHospital = intent.getParcelableExtra(Common.KEY_HOSPITAL_STORE);
            else if (step == 2)
                Common.currentDoctor = intent.getParcelableExtra(Common.KEY_DOCTOR_SELECTED);
            else if (step == 3)
                Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT, -1);

//            Common.currentHospital = intent.getParcelableExtra(Common.KEY_HOSPITAL_STORE);
            btn_next_step.setEnabled(true);
            setColorButton();
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aactivity_appointment_main);
        ButterKnife.bind(AAppointmentMainActivity.this);

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        setupStepView();
        setColorButton();

        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4); // 4 fragment
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {
                stepView.go(i, true);
               if (i == 0)
                   btn_previous_step.setEnabled(false);
               else
                   btn_previous_step.setEnabled(true);

               btn_next_step.setEnabled(false);
               setColorButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        nav_view=findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState==null){
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CovidFragment()).commit();
            nav_view.setCheckedItem(R.id.nav_profile);
        }

        View header = nav_view.getHeaderView(0);
        txt_name_user = header.findViewById(R.id.txt_name_user);
        txt_email_user = header.findViewById(R.id.txt_email_user);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    String fullname = userProfile.name;
                    String email = userProfile.email;

                    txt_name_user.setText(fullname);
                    txt_email_user.setText(email);

//                   fullnameTVBK.setText(fullname);
//                    progressBarSumbangan.setVisibility(View.GONE);
//                    Toast.makeText(getActivity(), "Full name loaded from profile", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AAppointmentMainActivity.this, "Database error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setColorButton() {
        if (btn_next_step.isEnabled()){
            btn_next_step.setBackgroundResource(R.color.colorButton);
        }
        else {
            btn_next_step.setBackgroundResource(android.R.color.darker_gray);
        }

        if (btn_previous_step.isEnabled()){
            btn_previous_step.setBackgroundResource(R.color.colorButton);
        }
        else {
            btn_previous_step.setBackgroundResource(android.R.color.darker_gray);
        }
    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Location");
        stepList.add("Medical Officer");
        stepList.add("Time");
        stepList.add("Confirmation");
        stepView.setSteps(stepList);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CovidFragment()).commit();
                break;
            case R.id.nav_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SumbanganFragment()).commit();
                break;
            case R.id.nav_profile:
                Intent k = new Intent(AAppointmentMainActivity.this, AAppointmentMainActivity.class);
                startActivity(k);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AppointmentFragment()).commit();
                nav_view.setCheckedItem(R.id.nav_profile);
                break;
            case R.id.nav_share:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new sSumbangan()).commit();
                item.setCheckable(true);
                item.setChecked(true);
                break;
            case R.id.nav_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new sAppointment()).commit();
                item.setCheckable(true);
                item.setChecked(true);
                break;
            case R.id.nav_send:
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}