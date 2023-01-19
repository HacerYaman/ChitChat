package com.example.chitchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.Models.Message;
import com.example.chitchat.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<Message> messageArrayList;
    Context mContex;
    String recId;

    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;

    public ChatAdapter(ArrayList<Message> messageArrayList, Context mContex, String recId) {
        this.messageArrayList = messageArrayList;
        this.mContex = mContex;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType== SENDER_VIEW_TYPE){
            View view= LayoutInflater.from(mContex).inflate(R.layout.sample_sender,parent, false);
            return new SenderViewHolder(view);
        }else{
            View view= LayoutInflater.from(mContex).inflate(R.layout.sample_receiver,parent, false);
            return new ReceiverViewHolder(view);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (messageArrayList.get(position).getuId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            return SENDER_VIEW_TYPE;
        }else{
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {  // set text

        Message message= messageArrayList.get(position);        //pozisyonlara göre mesaj 0. pozisyondaki 1. pozisyon sıra sıra mesajları alcak

        if (holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMsg.setText(message.getMessage());
        }else{
            ((ReceiverViewHolder)holder).receiverMsg.setText(message.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView receiverMsg, receiverTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMsg=itemView.findViewById(R.id.receiver_txt);
            receiverTime=itemView.findViewById(R.id.receiver_time);

        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView senderMsg, senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsg=itemView.findViewById(R.id.sender_txt);
            senderTime=itemView.findViewById(R.id.sender_time);

        }
    }
}
