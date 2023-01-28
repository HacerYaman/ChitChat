package com.example.chitchat.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.chitchat.Adapters.UsersAdapter;
import com.example.chitchat.Models.Message;
import com.example.chitchat.Models.Users;
import com.example.chitchat.Notifications.Data;
import com.example.chitchat.R;
import com.example.chitchat.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    public ChatsFragment() {
        // Required empty public constructor
    }

    private FragmentChatsBinding binding;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    ArrayList<Users> usersArrayList= new ArrayList<>();
    ArrayList<String> filteredList= new ArrayList<>();
    UsersAdapter usersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding=FragmentChatsBinding.inflate(inflater,container,false);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();


        //usersAdapter= new UsersAdapter(usersArrayList,getContext());
        LinearLayoutManager  layoutManager= new LinearLayoutManager(getContext());
        binding.rerecece.setLayoutManager(layoutManager);
        binding.rerecece.setHasFixedSize(true);


        FirebaseDatabase.getInstance().getReference("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filteredList.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    Message message=dataSnapshot.getValue(Message.class);

                    if (message.getuId().equals(firebaseUser.getUid())){
                        filteredList.add(message.getReceiver());
                    }
                    if (message.getReceiver().equals(firebaseUser.getUid())){
                        filteredList.add(message.getuId());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*firebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
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
        });*/

        return binding.getRoot();
    }

    private void readChats(){

        databaseReference=FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    Users user= dataSnapshot.getValue(Users.class);

                    for (String id: filteredList ){

                        if (user.getUserid().equals(id)){
                            if (usersArrayList.size()!=0){
                                for (Users user1: usersArrayList){
                                    if(!user.getUserid().equals(user1.getUserid())){
                                        usersArrayList.add(user);
                                    }
                                }
                            }else {
                                usersArrayList.add(user);
                            }
                        }
                    }
                }
                usersAdapter= new UsersAdapter(usersArrayList,getContext());
                binding.rerecece.setAdapter(usersAdapter);
                usersAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}