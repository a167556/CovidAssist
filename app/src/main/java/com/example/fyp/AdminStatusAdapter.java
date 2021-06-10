package com.example.fyp;

import android.os.Build;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminStatusAdapter extends FirebaseRecyclerAdapter<UserHelperClass,AdminStatusAdapter.statusViewHolder> {

    public AdminStatusAdapter(@NonNull FirebaseRecyclerOptions<UserHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull statusViewHolder holder, int position, @NonNull UserHelperClass model) {

       holder.uid.setText(model.getUid());
       holder.user.setText(model.getFullName());
       holder.ic.setText(model.getIcNo());
       holder.phone.setText(model.getPhoneNo());
       holder.address.setText(model.getUserAddress());
       holder.aid.setText(model.getUserAid());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Sumbangan");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (dataSnapshot.child("uid").getValue().equals(model.getUid())) {
                        if (dataSnapshot.child("status").getValue().equals("diterima")) {
                            holder.imgload.setImageResource(R.drawable.lulus);
                            holder.acceptButton.setVisibility(View.GONE);
                            holder.declinedButton.setVisibility(View.GONE);
                        } else if (dataSnapshot.child("status").getValue().equals("ditolak")) {
                            holder.imgload.setImageResource(R.drawable.rejected);
                            holder.acceptButton.setVisibility(View.GONE);
                            holder.declinedButton.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @NonNull
    @Override
    public statusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_sumbangan,parent,false);

        return new statusViewHolder(view);

    }

    class statusViewHolder extends RecyclerView.ViewHolder{
        TextView  user, ic, address, aid, phone, uid;
        public ImageView imgload;
        ConstraintLayout expandablView;
        public Button arrowBtn, acceptButton, declinedButton;
        CardView cardView;
        FirebaseDatabase rootNode;

        public statusViewHolder(@NonNull View itemView) {
            super(itemView);

            uid = (TextView)itemView.findViewById(R.id.uid);
            user = (TextView)itemView.findViewById(R.id.labelNama);
            ic = (TextView)itemView.findViewById(R.id.labelIC);
            address = (TextView)itemView.findViewById(R.id.labelAlamat);
            aid = (TextView)itemView.findViewById(R.id.aidTV);
            phone = (TextView)itemView.findViewById(R.id.labelTel);

           expandablView = (ConstraintLayout)itemView.findViewById(R.id.expandableview);
           arrowBtn = (Button)itemView.findViewById(R.id.arrowBtn);
           cardView = (CardView)itemView.findViewById(R.id.cardViewStart);

           acceptButton = (Button)itemView.findViewById(R.id.btnAccept);
           declinedButton = (Button)itemView.findViewById(R.id.declinebtn);
           imgload = (ImageView) itemView.findViewById(R.id.imageLoad);


            rootNode = FirebaseDatabase.getInstance();
//            reference = rootNode.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Sumbangan");
//            refSumbangan = rootNode.getReference().child("Sumbangan");







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
//                   openDialog();
                   FirebaseDatabase.getInstance().getReference("Sumbangan").child( uid.getText().toString()).child("status").setValue("diterima");
                   imgload.setImageResource(R.drawable.lulus);
               }
           });

           declinedButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   imgload.setImageResource(R.drawable.rejected);
                   FirebaseDatabase.getInstance().getReference("Sumbangan").child( uid.getText().toString()).child("status").setValue("ditolak");
               }
           });


        }

        public void openDialog() {
//            ConfirmDialog confirmDialog = new ConfirmDialog();
//            confirmDialog.show(getSupportFragmentManager(),"testing");
        }
    }
}
