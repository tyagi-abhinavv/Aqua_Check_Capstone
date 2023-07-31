package com.example.aqua_check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login_Page extends AppCompatActivity {
    TextView tosignup;
    TextView forgetpassword;
    ImageButton p_vis;
    boolean isvis=false;
    EditText password;
    EditText email;
    Button login;
    ProgressDialog loader;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener aut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getSupportActionBar().hide();
        tosignup=findViewById(R.id.tosignup);
        String to_signup_text="New User? Register Now";
        SpannableString to_signup_ss=new SpannableString(to_signup_text);

        ForegroundColorSpan link=new ForegroundColorSpan(Color.parseColor("#FF90BB"));
        ClickableSpan tolink_signup=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent=new Intent(Login_Page.this,Signup_Page.class);
                startActivity(intent);
            }
        };

        to_signup_ss.setSpan(tolink_signup,10,22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        to_signup_ss.setSpan(link,10,22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tosignup.setText(to_signup_ss);
        tosignup.setMovementMethod(new LinkMovementMethod().getInstance());


        forgetpassword=findViewById(R.id.forget_password);
        String to_forget_password_text="Forget Password?";
        SpannableString to_forget_password_ss=new SpannableString(to_forget_password_text);
        ClickableSpan tolink_forget_password=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent=new Intent(Login_Page.this,Forget_Password.class);
                startActivity(intent);
            }
        };

        to_forget_password_ss.setSpan(tolink_forget_password,0,to_forget_password_text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        to_forget_password_ss.setSpan(link,0,to_forget_password_text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgetpassword.setText(to_forget_password_ss);
        forgetpassword.setMovementMethod(new LinkMovementMethod().getInstance());
        password=findViewById(R.id.password);

        p_vis=findViewById(R.id.p_visible);
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

        email=findViewById(R.id.email);
        login=findViewById(R.id.login_button);
        auth = FirebaseAuth.getInstance();
        aut=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= auth.getCurrentUser();
                if(user!=null){
                    Intent i=new Intent(Login_Page.this,Home_Page.class);
                    startActivity(i);
                    finish();
                }

            }
        };
        loader=new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_=email.getText().toString().trim();
                String password_=password.getText().toString().trim();
                if(TextUtils.isEmpty(email_)){
                    email.setError("Email is required");return;
                }
                if(TextUtils.isEmpty(password_)){
                    password.setError("Please enter your password");return;
                }
                else{
                    loader.setMessage("Logging you in...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    auth.signInWithEmailAndPassword(email_,password_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if(auth.getCurrentUser().isEmailVerified()) {
                                    Intent i = new Intent(Login_Page.this, Home_Page.class);
                                    startActivity(i);
                                    finish();
                                    loader.dismiss();
                                }
                                else{
                                    String error="E-mail not verified";
                                    Toast.makeText(Login_Page.this, "Login Failed "+error, Toast.LENGTH_SHORT).show();
                                    loader.dismiss();
                                }
//                                Intent i = new Intent(Login_Page.this, Home_Page.class);
//                                startActivity(i);
//                                loader.dismiss();
//                                finish();

                            }
                            else{
                                String error=task.getException().toString();
                                Toast.makeText(Login_Page.this, "Login Failed "+error, Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                        }
                    });
                }

            }
        });




    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        auth.addAuthStateListener(aut);
//
//    }

//    Boolean isvalid(String pwd){
//        String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
//        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
//        Matcher matcher = pattern.matcher(pwd);
//        return matcher.matches();
//    }
}