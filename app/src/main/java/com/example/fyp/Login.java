package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private TextView gotoRegister;
    private Button btnLogin;
    private EditText loginEmail, loginPassword;
    private ProgressBar progressBar;
    private DatabaseReference dbReference;
    private String userID;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        gotoRegister=findViewById(R.id.gotoRegister);
        gotoRegister.setOnClickListener(this);

        loginEmail=findViewById(R.id.loginEmail);
        loginPassword=findViewById(R.id.loginPassword);
        progressBar=findViewById(R.id.progressBar);

        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gotoRegister:
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.btnLogin:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email=loginEmail.getText().toString().trim();
        String password=loginPassword.getText().toString().trim();

        if(email.isEmpty()){
            loginEmail.setError("Email is required!");
            loginEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            loginPassword.setError("Password is required!");
            loginPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    dbReference = FirebaseDatabase.getInstance().getReference("Users");
                    userID = user.getUid();
                    //Redirect user to appropriate activity
                    dbReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userProfile = snapshot.getValue(User.class);
                            String type = userProfile.type;

                            if(type.equals("doctor")){
                                startActivity(new Intent(Login.this, AdminDoctorActivity.class));
                            }
                            else if (type.equals("ngo")){
                                startActivity(new Intent(Login.this, AdminStatusSumbangan.class));
                            }
                            else {
                                startActivity(new Intent(Login.this, MainActivity.class));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Login.this, "Failed to login !", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    Toast.makeText(Login.this, "Failed to login!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}