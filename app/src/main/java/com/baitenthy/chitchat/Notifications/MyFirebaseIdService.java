package com.baitenthy.chitchat.Notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        //String refreshToken=FirebaseMessaging.getInstance().token

        Log.d("NEW_TOKEN",token);

        if (firebaseUser!=null){
            updateToken(token);
        }
    }

    private void updateToken(String token) {

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");

        Token token1= new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);


    }
}
