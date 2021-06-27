package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    NavigationView navigationView;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    TextView txt_name_user, txt_email_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
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
                Toast.makeText(MainActivity.this, "database error", Toast.LENGTH_LONG).show();
            }
        });

        Dexter.withContext(this).withPermissions(new String[]{
            Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
        }).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
//                Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }
        }).check();

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);



        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CovidFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_message);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
                        Intent k = new Intent(MainActivity.this, AAppointmentMainActivity.class);
                        startActivity(k);
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
                        Toast.makeText(MainActivity.this, "Log out!", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, Login.class));
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}