package com.baitenthy.chitchat.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baitenthy.chitchat.Adapters.UsersAdapter;
import com.baitenthy.chitchat.Models.Chatlist;
import com.baitenthy.chitchat.Models.Message;
import com.baitenthy.chitchat.Models.Users;
import com.baitenthy.chitchat.Notifications.Token;
import com.baitenthy.chitchat.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatsFragment extends Fragment {

    public ChatsFragment() {
        // Required empty public constructor
    }

    private FragmentChatsBinding binding;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    ArrayList<Users> usersArrayList;

    //ArrayList<String> filteredList= new ArrayList<>();
    private ArrayList<Chatlist> filteredList= new ArrayList<>();

    UsersAdapter usersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding=FragmentChatsBinding.inflate(inflater,container,false);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        LinearLayoutManager  layoutManager= new LinearLayoutManager(getContext());
        binding.rerecece.setLayoutManager(layoutManager);
        binding.rerecece.setHasFixedSize(true);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               filteredList.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren() ){

                    Chatlist chatlist= dataSnapshot.getValue(Chatlist.class);
                    filteredList.add(chatlist);
                }
                filteredList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*FirebaseDatabase.getInstance().getReference("Chats").addValueEventListener(new ValueEventListener() {
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
            public void onCancelled(@NonNull DatabaseError error) {}
        });*/

        //updateToken();

        return binding.getRoot();
    }

    private void filteredList() {
        usersArrayList= new ArrayList<>();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    Users users= dataSnapshot.getValue(Users.class);

                    for (Chatlist chatlist: filteredList){                              //??????????
                        if (users.getUserid().equals(chatlist.getId())){
                            usersArrayList.add(users);
                        }
                    }
                }
                usersAdapter= new UsersAdapter(usersArrayList, getContext() , true);
                binding.rerecece.setAdapter(usersAdapter);
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*private void readChats(){

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
                                for (Users user1: usersArrayList){   //????kertiyo
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
                usersAdapter= new UsersAdapter(usersArrayList,getContext(),true);
                binding.rerecece.setAdapter(usersAdapter);
                usersAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    private void status(String status){

        HashMap<String, Object> hashMap= new HashMap<>();
        hashMap.put("status",status);
        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).updateChildren(hashMap);
    }

    @Override
    public void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        //FirebaseDatabase.getInstance().getReference().removeEventListener(seenListener);
        status("offline");
    }

    private void updateToken(String token){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1= new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }

}