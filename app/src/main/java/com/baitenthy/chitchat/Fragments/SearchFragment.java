package com.baitenthy.chitchat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baitenthy.chitchat.Adapters.UsersAdapter;
import com.baitenthy.chitchat.Models.Users;
import com.baitenthy.chitchat.R;
import com.baitenthy.chitchat.databinding.FragmentChatsBinding;
import com.baitenthy.chitchat.databinding.FragmentSearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private UsersAdapter usersAdapter;
    private ArrayList<Users> usersArrayList;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding=FragmentSearchBinding.inflate(inflater,container,false);

        binding.searchRecyclerView.setHasFixedSize(true);
        binding.searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        usersArrayList= new ArrayList<>();
        usersAdapter= new UsersAdapter(usersArrayList,getContext(),false);
        binding.searchRecyclerView.setAdapter(usersAdapter);

        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (binding.searchBar.getText().toString().equals("")){
                    binding.searchRecyclerView.setVisibility(View.GONE);
                }else{
                    binding.searchRecyclerView.setVisibility(View.VISIBLE);
                    searchUsers(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                    if (binding.searchBar.getText().toString().equals("")){
                        binding.searchRecyclerView.setVisibility(View.GONE);
                    }else{
                        binding.searchRecyclerView.setVisibility(View.VISIBLE);
                    }
            }
        });

        return binding.getRoot();
    }

    private void searchUsers(String s){
        Query query= FirebaseDatabase.getInstance()
                .getReference("Users").orderByChild("username")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersArrayList.clear();
                FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

                for (DataSnapshot snapshot: dataSnapshot.getChildren() ){
                    Users user= snapshot.getValue(Users.class);

                    if(firebaseAuth.getUid()!= user.getUserid()){
                        usersArrayList.add(user);
                    }
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}