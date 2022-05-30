package com.example.labtestapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    private SharedPreferences preferences;
    private String email;
    private String password;
    private String dname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        email = preferences.getString("email", "");
        password = preferences.getString("password", "");
        dname = preferences.getString("dname", "");
    }


    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(!email.equals("") && !password.equals("")){
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }else if (!dname.equals("") && !password.equals("")){
                    intent = new Intent(getApplicationContext(), PathologyDetailsActivity.class);
                }else{
                    intent = new Intent(getApplicationContext(), LoginAct.class);
                }
                startActivity(intent);
            }
        }, 3000);
    }

}