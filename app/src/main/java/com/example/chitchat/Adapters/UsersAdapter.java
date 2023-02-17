package com.example.chitchat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.ChatDetailActivity;
import com.example.chitchat.Models.Users;
import com.example.chitchat.ProfilDetailActivity;
import com.example.chitchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    ArrayList<Users>  userList;
    Context mContext;
    FirebaseUser firebaseUser;
    boolean isChat;

    public UsersAdapter(ArrayList<Users> userList, Context mContext, boolean isChat) {
        this.userList = userList;
        this.mContext = mContext;
        this.isChat=isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.sample_show_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser =FirebaseAuth.getInstance().getCurrentUser();

        String currentUser=firebaseUser.getUid();

        Users user= userList.get(position);             //------SU AN ARATILIP SEÇİKEN HESAP BURADA!!!!!!!!!!!!!!!!!!!!!!!!!

        Picasso.get().load(user.getImageurl()).placeholder(R.drawable.userdefpic).into(holder.profile_image);
        holder.userName.setText(user.getUsername());
        holder.lastMessage.setText(user.getLastMessage());

        //----------------

        holder.itemView.setOnClickListener(new View.OnClickListener() {      //TIKLANILAN KULLANICININ İNFOLARI BURADA BURADAN CHAT DETAİLE ALIYORUZ
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference("Follow")
                        .child(currentUser).child("following").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child(user.getUserid()).exists()){

                                    FirebaseDatabase.getInstance().getReference("Follow")
                                            .child(user.getUserid()).child("following").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    if (snapshot.child(currentUser).exists()){

                                                        Intent intent= new Intent(mContext, ChatDetailActivity.class);
                                                        intent.putExtra("userId", user.getUserid());
                                                        intent.putExtra("profilePic", user.getImageurl());
                                                        intent.putExtra("userName", user.getUsername());

                                                        mContext.startActivity(intent);

                                                    }else{
                                                        Toast.makeText(mContext, "need to add each other", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                }else{
                                    Toast.makeText(mContext, "need to add each other", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

        holder.profile_image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent2= new Intent(mContext, ProfilDetailActivity.class);

                intent2.putExtra("userId", user.getUserid());
                intent2.putExtra("profilePic", user.getImageurl());
                intent2.putExtra("userName", user.getUsername());
                intent2.putExtra("aboutMe", user.getAboutMe());
                intent2.putExtra("fullname", user.getFullname());

                mContext.startActivity(intent2);

                return false;
            }
        });

        if (isChat){
            if (user.getStatus().equals("online")){
                holder.img_online.setVisibility(View.VISIBLE);
                holder.img_offline.setVisibility(View.GONE);
            }else{
                holder.img_online.setVisibility(View.GONE);
                holder.img_offline.setVisibility(View.VISIBLE);
            }
        }else{
            holder.img_online.setVisibility(View.GONE);
            holder.img_offline.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profile_image, img_online,img_offline;
        TextView userName, lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image=itemView.findViewById(R.id.profile_image);
            userName=itemView.findViewById(R.id.usernameList);
            lastMessage=itemView.findViewById(R.id.lastMessage);
            img_online=itemView.findViewById(R.id.img_online);
            img_offline=itemView.findViewById(R.id.img_offline);


        }
    }
}
