package com.example.depran.nimoc.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.depran.nimoc.R;

public class EditPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        final EditText curPass = (EditText) findViewById(R.id.currentPass);
        final EditText changePass = (EditText) findViewById(R.id.changePass);
        final EditText reTypePass = (EditText) findViewById(R.id.reTypePassword);
        Button doneEdit = (Button) findViewById(R.id.btnDonePass);

        doneEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curPass.getText().toString().isEmpty()){
                    curPass.setError("Password tidak boleh kosong");
                }else if(curPass.getText().toString().length() < 5){
                    curPass.setError("Password minimal 5 karakter");
                }else if(changePass.getText().toString().isEmpty()){
                    changePass.setError("Password pengganti tidak boleh kosong");
                }else if (changePass.getText().toString().length() < 5){
                    changePass.setError("Password minimal 5 karakter");
                }else if(changePass.getText().toString().equals(curPass.getText().toString())){
                    changePass.setError("Password tidak boleh sama dengan password sebelumnya");
                }else{
                    String pswd = curPass.getText().toString().trim();
                    String changePswd = changePass.getText().toString().trim();
                    String rePass = reTypePass.getText().toString().trim();

                    if(rePass.equals(changePswd)){
                        //fungsi add ke database dan penyocokan current password dengan database

                    }else{
                        reTypePass.setError("Password tidak sama");
                    }
                }
            }
        });




    }
}
