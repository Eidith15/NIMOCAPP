package com.example.depran.nimoc.etc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.depran.nimoc.R;
import com.example.depran.nimoc.user.EditPasswordActivity;
import com.example.depran.nimoc.user.EditProfileActivity;

public class ProfileActivity extends AppCompatActivity {

    Button editPasswordBtn, editProfileBtn, deleteProfileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
