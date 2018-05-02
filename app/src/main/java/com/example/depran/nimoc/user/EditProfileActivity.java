package com.example.depran.nimoc.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.depran.nimoc.R;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final EditText username = (EditText) findViewById(R.id.edtUsername);
        final EditText telepon = (EditText)findViewById(R.id.edtTelpon);
        final EditText email = (EditText)findViewById(R.id.edtEmail);
        final RadioButton laki = (RadioButton) findViewById(R.id.checkLaki);
        final RadioButton perempuan = (RadioButton)findViewById(R.id.checkPerempuan);
        Button doneEdit = (Button) findViewById(R.id.btnDoneProfile);
        final RadioGroup jenisKelamin = (RadioGroup)findViewById(R.id.jk);

        doneEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (username.getText().toString().isEmpty()) {
                        username.setError("Username tidak boleh kosong");
                    } else if (username.getText().toString().length() < 5) {
                        username.setError("Username minimal 5 karakter");
                    } else if (telepon.getText().toString().isEmpty()) {
                        telepon.setError("No.Telpon tidak boleh kosong");
                    } else if (telepon.getText().toString().length() > 13 ) {
                        telepon.setError("No.Telpon terlalu panjang");
                    } else if (telepon.getText().toString().length() < 5 ) {
                        telepon.setError("No.Telpon terlalu pendek");
                    } else if (email.getText().toString().isEmpty()) {
                        email.setError("Email tidak boleh kosong");
                    } else if (email.getText().toString().length() < 5) {
                        email.setError("Email Terlalu pendek");
                    } else {
                        String user = username.getText().toString().trim();
                        String telp = telepon.getText().toString().trim();
                        String mail = email.getText().toString().trim();
                        String jk;

                        if (jenisKelamin.getCheckedRadioButtonId() == -1) {
                            laki.setError("Harus memiilih jenis kelamin");
                            perempuan.setError("Harus memilikih jenis kelamin");
                        } else {
                            if(laki.isChecked()){
                                jk = laki.getText().toString().trim();
                            }else{
                                jk = perempuan.getText().toString().trim();
                            }
                        //fungsi add ke database

                        }
                    }
                }
        });


    }
}
