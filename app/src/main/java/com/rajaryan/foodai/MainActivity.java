package com.rajaryan.foodai;

import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timer t=new Timer();
        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }
    private void login() {
        Intent i=new Intent(MainActivity.this,Login.class);
        startActivity(i);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Timer t=new Timer();

        t.schedule(new TimerTask() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user!=null){
                    Intent o=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(o);
                }
                else {
                    login();
                }
            }
        },500);
    }
}