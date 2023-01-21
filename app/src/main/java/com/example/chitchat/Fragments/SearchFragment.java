package com.example.chitchat.Fragments;

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

import com.example.chitchat.Adapters.UsersAdapter;
import com.example.chitchat.Models.Users;
import com.example.chitchat.R;
import com.example.chitchat.databinding.FragmentChatsBinding;
import com.example.chitchat.databinding.FragmentSearchBinding;
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
        usersAdapter= new UsersAdapter(usersArrayList,getContext());
        binding.searchRecyclerView.setAdapter(usersAdapter);

        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    readUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return binding.getRoot();
    }

    private void readUsers(String s){

        Query query=FirebaseDatabase.getInstance()
                .getReference("Users").orderByChild("username")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                binding.searchBar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        binding.searchRecyclerView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if (binding.searchBar.getText().toString().equals("")){
                            binding.searchRecyclerView.setVisibility(View.GONE);
                        }else{
                            binding.searchRecyclerView.setVisibility(View.VISIBLE);

                            //----------------------------------- tüm kullanıcıları çekip tek tek arrayliste koyduk
                            usersArrayList.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                                Users user=dataSnapshot.getValue(Users.class);
                                usersArrayList.add(user);
                            }
                            usersAdapter.notifyDataSetChanged();
                            //--------------------------------------
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}