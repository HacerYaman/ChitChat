package com.example.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chitchat.Adapters.ChatAdapter;
import com.example.chitchat.Models.Message;
import com.example.chitchat.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {

    private ActivityChatDetailBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide();

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        final String msg_senderId= firebaseAuth.getUid();

        String receiverId= getIntent().getStringExtra("userId");
        String recUserName= getIntent().getStringExtra("userName");
        String recProfilePic= getIntent().getStringExtra("profilePic");

        binding.userName.setText(recUserName);
        Picasso.get().load(recProfilePic).placeholder(R.drawable.userdefpic).into(binding.proPic);

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatDetailActivity.this, HomeActivity.class));
                finish();
            }
        });

        //---
        final ArrayList<Message> messageArrayList= new ArrayList<>();
        final ChatAdapter chatAdapter= new ChatAdapter(messageArrayList,this,receiverId);

        binding.chatrecyclerview.setHasFixedSize(true);
        binding.chatrecyclerview.setAdapter(chatAdapter);
        binding.chatrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        //---

        final String senderRoom= msg_senderId+receiverId;
        final String receiverRoom= receiverId+msg_senderId;

        firebaseDatabase.getReference()
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
        });
    }
}