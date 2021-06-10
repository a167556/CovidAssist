package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SumbanganFragment extends Fragment {
    private FirebaseUser user;
    private DatabaseReference reference, refSumbangan;
    private FirebaseDatabase rootNode;
    private String userID;
    private TextInputLayout textFieldFullSumb, textFieldPhoneSumb, textFieldAmount, textFieldAdrressSumb, textFieldJenisSumb;
    private Button textButtonSubmitS;
//    private ProgressBar progressBarSumbangan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sumbangan, container, false);

        String[] typeaid = { "Financial", "Food & Water", "Clothes", "Education" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.select_dialog_singlechoice, typeaid);
        //Find TextView control
        AutoCompleteTextView acTextView = (AutoCompleteTextView) v.findViewById(R.id.typeaid);
        //Set the adapter
        acTextView.setAdapter(adapter);

        acTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acTextView.showDropDown();
            }
        });

        // function
//        progressBarSumbangan = (ProgressBar) v.findViewById(R.id.progressBarSumbangan);
//        progressBarSumbangan.setVisibility(View.VISIBLE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        textFieldFullSumb = v.findViewById(R.id.fullname);
        textFieldPhoneSumb = v.findViewById(R.id.phone);
        textFieldAmount = v.findViewById(R.id.amount);
        textFieldAdrressSumb = v.findViewById(R.id.address);
        textFieldJenisSumb = v.findViewById(R.id.typeofaid);
        textButtonSubmitS = v.findViewById(R.id.submitsumbangan);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    String fullname = userProfile.name;

                    textFieldFullSumb.getEditText().setText(fullname);

//                   fullnameTVBK.setText(fullname);
//                    progressBarSumbangan.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Full name loaded from profile", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "database error", Toast.LENGTH_LONG).show();
            }
        });

        textButtonSubmitS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBarSumbangan.setVisibility(View.VISIBLE);

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Sumbangan");
                refSumbangan = rootNode.getReference().child("Sumbangan");

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh-mm-ss");
                String format = simpleDateFormat.format(new Date());
                String format2 = simpleTimeFormat.format(new Date());


                //Get all values
                String fullName = textFieldFullSumb.getEditText().getText().toString();
                String phoneNo = textFieldPhoneSumb.getEditText().getText().toString();
                String amount = textFieldAmount.getEditText().getText().toString();
                String userAddress = textFieldAdrressSumb.getEditText().getText().toString();
                String userAid = textFieldJenisSumb.getEditText().getText().toString();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String idSumbangan = refSumbangan.push().getKey();

                UserHelperClass helperClass = new UserHelperClass(fullName, phoneNo, amount, userAddress,userAid,uid);
                reference.setValue(helperClass);
                reference.child("date").setValue(format);


                FirebaseDatabase.getInstance().getReference("Sumbangan").child(uid).setValue(helperClass);
                FirebaseDatabase.getInstance().getReference("Sumbangan").child(uid).child("date").setValue(format);
                FirebaseDatabase.getInstance().getReference("Sumbangan").child(uid).child("status").setValue("dihantar");
                //refSumbangan.child(idSumbangan).setValue(helperClass);

                Toast.makeText(getActivity(), "Your form has been sent!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getActivity(), MainActivity.class));

            }
        });
        return v;
    }

}
