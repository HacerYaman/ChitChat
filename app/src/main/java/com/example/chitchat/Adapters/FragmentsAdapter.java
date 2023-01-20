package com.example.chitchat.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chitchat.Fragments.ChatsFragment;
import com.example.chitchat.Fragments.ProfileFragment;
import com.example.chitchat.Fragments.SearchFragment;

public class FragmentsAdapter extends FragmentPagerAdapter {

    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new ChatsFragment();
            case 1: return new SearchFragment();
            case 2: return new ProfileFragment();
            default: return new ChatsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title= null;

        if (position==0){
            title="CHATS";
        }
        if (position==1){
            title="SEARCH";
        }
        if (position==2){
            title="PROFILE";
        }
        return title;
    }
}
