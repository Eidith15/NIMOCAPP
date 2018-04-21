
package com.example.depran.nimoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {
    EditText us, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sign_in);

        us = findViewById(R.id.username);
        pass = findViewById(R.id.password);

        findViewById(R.id.btnSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = us.getText().toString().trim();
                String pswd = pass.getText().toString().trim();
                if (user.equalsIgnoreCase("username") && pswd.equalsIgnoreCase("password")) {
                    Session.createSignInSession(SignInActivity.this, user);
                    startActivity(new Intent(SignInActivity.this, BerandaActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignInActivity.this, "user invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

}
