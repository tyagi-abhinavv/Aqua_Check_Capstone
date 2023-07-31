package com.example.aqua_check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup_Page extends AppCompatActivity {
    boolean isvis=false;
    boolean isvis2=false;
    EditText password;
    EditText cpassword;
    EditText email;
    ImageButton p_vis;
    ImageButton cp_vis;
    Button signup;
    ProgressDialog loader;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        getSupportActionBar().hide();
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        p_vis=findViewById(R.id.p_visible);
        loader=new ProgressDialog(this);
        p_vis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isvis) {
                    p_vis.setImageResource(R.drawable.invisible);
                    isvis = false;
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else {
                    p_vis.setImageResource(R.drawable.visible);
                    isvis = true;
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        cpassword=findViewById(R.id.cpassword);
        signup=findViewById(R.id.signup_button);
        cp_vis=findViewById(R.id.cp_visible);
        cp_vis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isvis2) {
                    cp_vis.setImageResource(R.drawable.invisible);
                    isvis2 = false;
                    cpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else {
                    cp_vis.setImageResource(R.drawable.visible);
                    isvis2 = true;
                    cpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_=email.getText().toString().trim();
                String password_=password.getText().toString().trim();
                String cpassword_=cpassword.getText().toString().trim();
                auth = FirebaseAuth.getInstance();
                if(TextUtils.isEmpty(email_)){
                    email.setError("Email is required");return;
                }
                if(TextUtils.isEmpty(password_)){
                    password.setError("Please enter your password");return;
                }
                if(TextUtils.isEmpty(cpassword_)){
                    cpassword.setError("Please confirm your password");return;
                }
                if(!isvalid(password_)){
                    password.setError("Password should contain a lowercase letter, an uppercase letter, a number and a special symbol");return;
                }
                if(password_.equals(cpassword_)){
                    loader.setMessage("Signup in progress ");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    auth.createUserWithEmailAndPassword(email_,password_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = auth.getCurrentUser();
//                                auth.signOut();
//                                startActivity(new Intent(Signup_Page.this, Login_Page.class));
                                sendEmailVerification(user);
//                                finish();
                                loader.dismiss();
                            }
                            else{
                                String error=task.getException().toString();
                                Toast.makeText(Signup_Page.this, "Signup Failed "+error, Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                        }
                    });
                }
                else{
                    cpassword.setError("Password doesn't match");return;
                }

            }
        });


    }
        Boolean isvalid(String pwd){
            String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
            Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
            Matcher matcher = pattern.matcher(pwd);
            return matcher.matches();
    }
        void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            auth.signOut();
                            Toast.makeText(Signup_Page.this, "Verification email sent. Please check your email.", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(Signup_Page.this, Login_Page.class));
                            finish();
                        } else {
                            Toast.makeText(Signup_Page.this, "Failed to send verification email. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}