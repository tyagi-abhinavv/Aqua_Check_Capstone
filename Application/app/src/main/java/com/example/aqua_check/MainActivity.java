package com.example.aqua_check;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();
        Intent intent;
        if(auth.getCurrentUser()!=null){
            intent=new Intent(MainActivity.this,Home_Page.class);
        }
        else
            intent=new Intent(MainActivity.this,Login_Page.class);
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 1.8 seconds
                    sleep(1800);

                    // After 5 seconds redirect to another intent
//                    Intent intent=new Intent(MainActivity.this,Login_Page.class);
                    startActivity(intent);

                    //Remove activity
                    finish();
                } catch (Exception e) {
                }
            }
        };
        background.start();
    }
}