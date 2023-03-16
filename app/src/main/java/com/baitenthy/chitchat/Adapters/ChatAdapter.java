package com.baitenthy.chitchat.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baitenthy.chitchat.Models.Message;
import com.baitenthy.chitchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        Message message= messageArrayList.get(position);

        ///----------------------
       /* if (position == messageArrayList.size()-1){
            if (message.isSeen()){
                ((SenderViewHolder)holder).txtSeen.setText("seen");
            }else{
                ((SenderViewHolder)holder).txtSeen.setText("delivered");            //crashliyo
            }
        }else {
            ((SenderViewHolder)holder).txtSeen.setVisibility(View.GONE);                //buralardan çöküyo
        }*/
        //-------------------------


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

               String current_user= FirebaseAuth.getInstance().getCurrentUser().getUid();

               //---------------
               if (current_user.equals(message.getuId())){
                   new AlertDialog.Builder(mContex)
                           .setTitle("Delete")
                           .setMessage("Delete this message from..")
                           .setPositiveButton("Everyone", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {
                                   FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();

                                   String senderRoom= current_user+recId;
                                   String receiverRoom= recId+current_user;

                                   String messageIddd= message.getMessage()+message.getTimestamp();

                                   firebaseDatabase.getReference("Chats")
                                           .child(senderRoom)           //null pointer exp.  mesajları sender room diye bir şeye kaydetmiyoruz o yüzden null.
                                           .child(messageIddd)
                                           .removeValue();

                                   firebaseDatabase.getReference("Chats")
                                           .child(receiverRoom)
                                           .child(messageIddd)
                                           .setValue(null);

                               }
                           }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {

                                   dialog.dismiss();


                               }
                           }).setNegativeButton("Just me", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {

                                   FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();

                                   String senderRoom= current_user+recId;
                                   String receiverRoom= recId+current_user;

                                   String messageIddd= message.getMessage()+message.getTimestamp();

                                   firebaseDatabase.getReference("Chats")
                                           .child(senderRoom)           //null pointer exp.  mesajları sender room diye bir şeye kaydetmiyoruz o yüzden null.
                                           .child(messageIddd)
                                           .removeValue();

                               }
                           }).show();
               }else {

                   //KARŞIDAKİNİN ATTIĞI MESAJI BENİM MESAJ KUTUMDAN SİLDİR

                   Toast.makeText(mContex, "You don't have permission to make changes.", Toast.LENGTH_LONG).show();
               }
               return false;
            }
        });
        //-----------

        if (holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMsg.setText(message.getMessage());

            //--------------saat ayarlaması------
            Date date= new Date(message.getTimestamp());
            SimpleDateFormat simpleDateFormat= new SimpleDateFormat("HH:mm");
            String strDate= simpleDateFormat.format(date);
            ((SenderViewHolder) holder).senderTime.setText(strDate);


        }else{
            ((ReceiverViewHolder)holder).receiverMsg.setText(message.getMessage());

            //-----------saat ayarlaması-------
            Date date= new Date(message.getTimestamp());
            SimpleDateFormat simpleDateFormat= new SimpleDateFormat("HH:mm");
            String strDate= simpleDateFormat.format(date);
            ((ReceiverViewHolder) holder).receiverTime.setText(strDate);

        }
        //-----------
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView receiverMsg, receiverTime, txtSeen;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMsg=itemView.findViewById(R.id.receiver_txt);
            receiverTime=itemView.findViewById(R.id.receiver_time);
            txtSeen=itemView.findViewById(R.id.txtSeen);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMsg, senderTime, txtSeen;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsg=itemView.findViewById(R.id.sender_txt);
            senderTime=itemView.findViewById(R.id.sender_time);
            txtSeen=itemView.findViewById(R.id.txtSeen);

        }
    }

}
