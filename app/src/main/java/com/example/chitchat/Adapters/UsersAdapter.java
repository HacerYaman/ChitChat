package com.example.chitchat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.ChatDetailActivity;
import com.example.chitchat.Models.Users;
import com.example.chitchat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    ArrayList<Users>  userList;
    Context mContext;

    public UsersAdapter(ArrayList<Users> userList, Context mContext) {
        this.userList = userList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.sample_show_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user= userList.get(position);

        Picasso.get().load(user.getImageurl()).placeholder(R.drawable.userdefpic).into(holder.profile_image);
        holder.userName.setText(user.getUsername());
        holder.lastMessage.setText(user.getLastMessage());

        //----------------

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mContext, ChatDetailActivity.class);

                intent.putExtra("userId", user.getUserid());
                intent.putExtra("profilePic", user.getImageurl());
                intent.putExtra("userName", user.getUsername());

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profile_image;
        TextView userName, lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image=itemView.findViewById(R.id.profile_image);
            userName=itemView.findViewById(R.id.usernameList);
            lastMessage=itemView.findViewById(R.id.lastMessage);

        }
    }
}
