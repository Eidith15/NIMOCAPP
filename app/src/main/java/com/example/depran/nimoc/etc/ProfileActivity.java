package com.example.depran.nimoc.etc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.depran.nimoc.R;
import com.example.depran.nimoc.buku.BerandaActivity;
import com.example.depran.nimoc.user.EditPasswordActivity;
import com.example.depran.nimoc.user.EditProfileActivity;
import com.example.depran.nimoc.utils.Session;

public class ProfileActivity extends AppCompatActivity {

    TextView usernameTextView,noHpTextView, emailTextView,jenisKTextView;
    Button editPasswordBtn, editProfileBtn, deleteProfileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameTextView = (TextView) findViewById(R.id.txtUsername);
        noHpTextView = (TextView) findViewById(R.id.txtNoHp);
        emailTextView = (TextView) findViewById(R.id.txtEmail);
        jenisKTextView = (TextView) findViewById(R.id.txtJenisKelamin);
        SharedPreferences preferences = ProfileActivity.this
                .getSharedPreferences(Session.PREF_NAME, 0);
        String username = preferences.getString("username", null);
        String email = preferences.getString("email", null);
        String no_hp = preferences.getString("no_hp", null);
        String jenis_kelamin = preferences.getString("jenis_kelamin", null);

        usernameTextView.setText(username);
        emailTextView.setText(email.isEmpty()?"-":email);
        noHpTextView.setText(no_hp.isEmpty()?"-":no_hp);
        jenisKTextView.setText(jenis_kelamin.isEmpty()?"-":((jenis_kelamin.equalsIgnoreCase("0"))?"perempuan":"laki-laki"));

        editPasswordBtn  = (Button) findViewById(R.id.edit_password_btn);
        editProfileBtn  = (Button) findViewById(R.id.edit_profile_btn);
        deleteProfileBtn  = (Button) findViewById(R.id.delete_akun_btn);

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
        editPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditPasswordActivity.class);
                startActivity(intent);
            }
        });
        deleteProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
