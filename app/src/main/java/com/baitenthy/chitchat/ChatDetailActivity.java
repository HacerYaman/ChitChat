package com.baitenthy.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.baitenthy.chitchat.Adapters.ChatAdapter;
import com.baitenthy.chitchat.Models.Message;
import com.baitenthy.chitchat.databinding.ActivityChatDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatDetailActivity extends AppCompatActivity {


    private ActivityChatDetailBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Message> messageArrayList;
    private ChatAdapter chatAdapter;
    ValueEventListener seenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth=FirebaseAuth.getInstance();

        final String msg_senderId= firebaseAuth.getUid();

        String receiverId= getIntent().getStringExtra("userId");
        String recUserName= getIntent().getStringExtra("userName");
        String recProfilePic= getIntent().getStringExtra("profilePic");

        binding.userName.setText(recUserName);
        Picasso.get().load(recProfilePic).placeholder(R.drawable.userdefpic).into(binding.proPic);

        readMessages(msg_senderId,receiverId,receiverId);

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatDetailActivity.this, HomeActivity.class));
                finish();
            }
        });

        //---
        //messageArrayList= new ArrayList<>();
        //chatAdapter= new ChatAdapter(messageArrayList,this,receiverId);

        binding.chatrecyclerview.setHasFixedSize(true);
        //binding.chatrecyclerview.setAdapter(chatAdapter);
        binding.chatrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        //---

        final String senderRoom= msg_senderId+receiverId;
        final String receiverRoom= receiverId+msg_senderId;

        /*firebaseDatabase.getReference()
                .child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageArrayList.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Message messageModel= dataSnapshot.getValue(Message.class);
                            messageModel.setMessageId(dataSnapshot.getKey());
                            messageArrayList.add(messageModel);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message= binding.message.getText().toString();
                final Message message1= new Message(msg_senderId,message);
                message1.setTimestamp(new Date().getTime());
                binding.message.setText("");                    //clear edit txt

                firebaseDatabase.getReference()
                        .child("Chats").child(senderRoom).push().setValue(message1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                firebaseDatabase.getReference()
                                        .child("Chats").child(receiverRoom).push().setValue(message1)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });
            }
        });*/

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sendedMessage =binding.message.getText().toString();
                final Message message= new Message(msg_senderId, sendedMessage);
                message.setTimestamp(new Date().getTime());

                if (!sendedMessage.equals("")){
                    sendMessage(msg_senderId,receiverId,sendedMessage, message.getTimestamp());
                    
                }else{
                    Toast.makeText(ChatDetailActivity.this, "empty", Toast.LENGTH_SHORT).show();
                }
                binding.message.setText("");
            }
        });

        seenMessage(receiverId);
    }

    //--------------------------

    private void seenMessage(String userid){

        DatabaseReference  reference= FirebaseDatabase.getInstance().getReference("Chats");

        seenListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    Message message= dataSnapshot.getValue(Message.class);

                    if (message.getReceiver().equals(firebaseAuth.getCurrentUser().getUid()) && message.getuId().equals(userid)){

                        HashMap<String, Object> hashMap= new HashMap<>();

                        hashMap.put("isSeen", true);

                        dataSnapshot.getRef().updateChildren(hashMap);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message, Long timestamp){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

        HashMap<String , Object> hashMap= new HashMap<>();

        hashMap.put("uId",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("isSeen", false );

        reference.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatRef= FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(firebaseAuth.getUid())       //current user id oldu??una g??re sender buras??
                .child(receiver);                //receiver olmas?? gerek


        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    chatRef.child("id").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessages(String myid, String userid, String receiverId){

        messageArrayList= new ArrayList<>();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    Message message=dataSnapshot.getValue(Message.class);

                    if (message.getReceiver().equals(myid) && message.getuId().equals(userid)
                            || message.getReceiver().equals(userid) && message.getuId().equals(myid)){

                        messageArrayList.add(message);
                    }
                    chatAdapter= new ChatAdapter(messageArrayList,ChatDetailActivity.this,receiverId);
                    binding.chatrecyclerview.setAdapter(chatAdapter);
                    chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //-------------online offline status

    private void status(String status){
        HashMap<String, Object> hashMap= new HashMap<>();
        hashMap.put("status",status);
        FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid()).updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference().removeEventListener(seenListener);
        status("offline");
    }

    //----------------------------



}