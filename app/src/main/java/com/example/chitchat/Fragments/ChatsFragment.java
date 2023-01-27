package com.example.chitchat.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.chitchat.Adapters.UsersAdapter;
import com.example.chitchat.Models.Users;
import com.example.chitchat.R;
import com.example.chitchat.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    public ChatsFragment() {
        // Required empty public constructor
    }

    private FragmentChatsBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    ArrayList<Users> usersArrayList= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding=FragmentChatsBinding.inflate(inflater,container,false);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        UsersAdapter usersAdapter= new UsersAdapter(usersArrayList,getContext());
        binding.rerecece.setAdapter(usersAdapter);

        LinearLayoutManager  layoutManager= new LinearLayoutManager(getContext());
        binding.rerecece.setLayoutManager(layoutManager);

        binding.rerecece.setHasFixedSize(true);


        //firebaseDatabase.getReference().child("Chats")



        firebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Users user= dataSnapshot.getValue(Users.class);
                    user.setUserid(dataSnapshot.getKey());

                    if(!user.getUserid().equals(firebaseAuth.getCurrentUser().getUid())){
                        usersArrayList.add(user);

                    }
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

}