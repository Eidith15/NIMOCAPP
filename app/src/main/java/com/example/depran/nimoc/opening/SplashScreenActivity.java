package com.example.depran.nimoc.opening;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.depran.nimoc.R;

import com.example.depran.nimoc.buku.BerandaActivity;
import com.example.depran.nimoc.buku.DashboardBukuActivity;
import com.example.depran.nimoc.user.LoginActivity;


import com.example.depran.nimoc.utils.Session;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                    Intent intent = null;
                    SharedPreferences preferences = SplashScreenActivity.this
                            .getSharedPreferences(Session.PREF_NAME, 0);


                    Log.e("awalan : ", String.valueOf(isFirstTimeStart()));
                    if ( !isFirstTimeStart()) {
                        try {
                            String id = preferences.getString("username", null);
                            Log.e("username", id);
                            intent = new Intent(SplashScreenActivity.this, BerandaActivity.class);
                        } catch (Exception e) {
                            intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        }
                    }else{
                        intent = new Intent(SplashScreenActivity.this, FirstSliderActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } catch (Exception ignored) {
                }
            }
        };
        background.start();
    }
    private boolean isFirstTimeStart(){
        SharedPreferences preferencesIntro = SplashScreenActivity.this
                .getSharedPreferences(Session.PREF_NAME_INTRO, 0);
        return preferencesIntro.getBoolean("awalan", true);
    }
}
