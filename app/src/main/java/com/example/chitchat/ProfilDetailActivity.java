package com.example.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chitchat.databinding.ActivityProfilDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfilDetailActivity extends AppCompatActivity {

    private ActivityProfilDetailBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    Button addingButton;
    String searchedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfilDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        addingButton=binding.addFrien;

        //-------------
        searchedId= getIntent().getStringExtra("userId");                    //başka bir sayfada aynı get intent String recId olarak kullanıldu
        String searchedUserName= getIntent().getStringExtra("userName");
        String searchedProfilePic= getIntent().getStringExtra("profilePic");
        String searchedAboutMe= getIntent().getStringExtra("aboutMe");
        String searchedFullName= getIntent().getStringExtra("fullname");
        //-------------

        binding.fullname.setText(searchedFullName);
        binding.username.setText(searchedUserName);
        binding.aboutMe.setText(searchedAboutMe);
        Picasso.get().load(searchedProfilePic).placeholder(R.drawable.userdefpic).into(binding.imageProfile);


        checkFollowingStatus();

        binding.addFrien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addingButton.getText().toString().equals("Add")){

                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(firebaseUser.getUid()).child("following").child(searchedId).setValue(true);


                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(firebaseUser.getUid()).child("followers").child(searchedId).setValue(true);

                }else{
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(firebaseUser.getUid()).child("following").child(searchedId).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(firebaseUser.getUid()).child("followers").child(searchedId).removeValue();
                }
            }
        });
    }

    private void checkFollowingStatus() {
        FirebaseDatabase.getInstance()
                .getReference().child("Follow").child(firebaseUser.getUid())
                .child("following").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(searchedId).exists()){
                            binding.addFrien.setText("Added");
                        }else{
                            binding.addFrien.setText("Add");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

    }
}