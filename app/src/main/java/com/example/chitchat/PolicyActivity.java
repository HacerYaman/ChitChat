package com.example.chitchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PolicyActivity extends AppCompatActivity {

    private ImageView close;
    private TextView policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        close=findViewById(R.id.close_policy);
        policy= findViewById(R.id.policy_read);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PolicyActivity.this, RegisterActivity.class));
                finish();
            }
        });

        policy.setMovementMethod(new ScrollingMovementMethod());



    }
}