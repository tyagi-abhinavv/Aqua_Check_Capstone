package com.example.aqua_check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget_Password extends AppCompatActivity {
    EditText email;
    Button forget;
    private FirebaseAuth auth;
    ProgressDialog loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().hide();
        email=findViewById(R.id.email);
        forget=findViewById(R.id.forget_button);
        auth=FirebaseAuth.getInstance();
        loader=new ProgressDialog(this);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_=email.getText().toString().trim();
                if(TextUtils.isEmpty(email_)){
                    email.setError("Email is required");return;
                }
                else{
                    loader.setMessage("Loading...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    auth.sendPasswordResetEmail(email_).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Password reset email sent", Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                                finish();
                            }
                            else{
                                loader.dismiss();
                                Toast.makeText(getApplicationContext(), "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}