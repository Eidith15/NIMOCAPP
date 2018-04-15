package com.example.depran.nimoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

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
                    sleep(3000);
                    Intent intent = null;
                    SharedPreferences preferences = SplashScreenActivity.this
                            .getSharedPreferences(Session.PREF_NAME, 0);

                    try {
                        String id = preferences.getString("username", null);
                        Log.e("username", id);
                        intent = new Intent(SplashScreenActivity.this, DashboardBukuActivity.class);
                    } catch (Exception e) {
                        intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } catch (Exception ignored) {
                }
            }
        };
        background.start();
    }
}
