package com.baitenthy.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.baitenthy.chitchat.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private ProgressDialog pd;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        binding.policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, PolicyActivity.class));
            }
        });

        binding.regButton.setEnabled(false);

        binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(binding.checkbox.isChecked()){
                    binding.regButton.setEnabled(true);
                }else{
                    binding.regButton.setEnabled(false);
                }
            }
        });

        binding.logTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        binding.regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd=new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Registering..");
                pd.show();

                String str_username= binding.username.getText().toString();
                String str_fullname= binding.fullname.getText().toString();
                String str_email=binding.email.getText().toString();
                String str_password= binding.password.getText().toString();

                if (TextUtils.isEmpty(str_username)|| TextUtils.isEmpty(str_fullname)||
                        TextUtils.isEmpty(str_email)|| TextUtils.isEmpty(str_password)){
                    Toast.makeText(RegisterActivity.this, "Incomplete Information!", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }else{
                    register(str_username,str_fullname,str_email,str_password);
                }
            }
        });
    }

    private void register(String username, String fullname, String email, String password){

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //-------------------------------------
                    HashMap<String, Object> usershashMap=new HashMap<>();
                    usershashMap.put("username",username);
                    usershashMap.put("fullname",fullname);
                    usershashMap.put("email",email);
                    usershashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/chitchat-f7376.appspot.com/o/userDefPic.png?alt=media&token=aa63bb94-c2a6-4108-9298-1ca07f6fabb4");
                    usershashMap.put("userid",firebaseAuth.getCurrentUser().getUid());
                    usershashMap.put("aboutMe","");

                    databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid())
                            .setValue(usershashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                pd.dismiss();
                                Toast.makeText(RegisterActivity.this, "nicely doeeenee", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    //---------------------------------------
                    pd.dismiss();
                    Intent intent=new Intent(RegisterActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else{
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}