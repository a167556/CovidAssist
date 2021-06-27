package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button btnRegister;
    TextView banner;
    EditText mFullName,mEmail,mPassword,mPhone;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        btnRegister=findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        mFullName=findViewById(R.id.inputName);
        mEmail=findViewById(R.id.inputEmail);
        mPassword=findViewById(R.id.inputPassword);
        mPhone=findViewById(R.id.inputPhone);

        progressBar=findViewById(R.id.progressBar2);

        banner=findViewById(R.id.banner);
        banner.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.banner:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.btnRegister:
                btnRegister();
                break;
        }
    }

    private void btnRegister() {
        String email=mEmail.getText().toString().trim();
        String password=mPassword.getText().toString().trim();
        String name=mFullName.getText().toString().trim();
        String phone=mPhone.getText().toString().trim();
        String type="normal";

        if(email.isEmpty()){
            mEmail.setError("Email is required!");
            mEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Please enter valid email!");
            mEmail.requestFocus();
            return;
        }

        if(name.isEmpty()){
            mFullName.setError("Name is required!");
            mFullName.requestFocus();
            return;
        }

        if(phone.isEmpty()){
            mPhone.setError("Phone is required!");
            mPhone.requestFocus();
            return;
        }

        if(password.isEmpty()){
            mPassword.setError("Password is required!");
            mPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            mPassword.setError("Minimum password length should be 6 characters!");
            mPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(name, phone, email, type);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Register.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(Register.this, Login.class));
                                    }
                                    else{
                                        Toast.makeText(Register.this, "Failed to register!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(Register.this, "Failed to register account!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}