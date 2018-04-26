package com.example.depran.nimoc.user;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.depran.nimoc.R;
import com.example.depran.nimoc.buku.BerandaActivity;

/**
 * Created by Eidith on 22/04/2018.
 */

public class LoginActivity extends AppCompatActivity {

    private Button btnSignUp;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        changeContent(SignInFragment.newInstance(this));

        btnSignIn = (Button) findViewById(R.id.btnLoginMenu);
        btnSignUp = (Button) findViewById(R.id.btnSignUpMenu);



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeContent(SignUpFragment.newInstance(LoginActivity.this));
                btnSignIn.setBackgroundResource(R.color.btnLogin);
                btnSignIn.setTextColor(getResources().getColor(R.color.black));

                btnSignUp.setBackgroundResource(R.color.login);
                btnSignUp.setTextColor(getResources().getColor(R.color.white));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeContent(SignInFragment.newInstance(LoginActivity.this));
                btnSignUp.setBackgroundResource(R.color.btnLogin);
                btnSignUp.setTextColor(getResources().getColor(R.color.black));

                btnSignIn.setBackgroundResource(R.color.login);
                btnSignIn.setTextColor(getResources().getColor(R.color.white));
            }
        });


    }


    public void changeContent(Fragment fragment){
        getFragmentManager().beginTransaction()
                .replace(R.id.contentLayout, fragment)
                .commitAllowingStateLoss();
    }
}
