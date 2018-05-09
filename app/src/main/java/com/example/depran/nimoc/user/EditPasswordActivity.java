package com.example.depran.nimoc.user;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.depran.nimoc.R;
import com.example.depran.nimoc.etc.ProfileActivity;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;
import com.example.depran.nimoc.utils.Session;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class EditPasswordActivity extends AppCompatActivity {
    EditText curPass,changePass,reTypePass;
    String pswd,changePswd,rePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        curPass = (EditText) findViewById(R.id.currentPass);
        changePass = (EditText) findViewById(R.id.changePass);
        reTypePass = (EditText) findViewById(R.id.reTypePassword);
        Button doneEdit = (Button) findViewById(R.id.btnDonePass);

        doneEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curPass.getText().toString().isEmpty()) {
                    curPass.setError("Password tidak boleh kosong");
                } else if (curPass.getText().toString().length() < 5) {
                    curPass.setError("Password minimal 5 karakter");
                } else if (changePass.getText().toString().isEmpty()) {
                    changePass.setError("Password pengganti tidak boleh kosong");
                } else if (changePass.getText().toString().length() < 5) {
                    changePass.setError("Password minimal 5 karakter");
                } else if (changePass.getText().toString().equals(curPass.getText().toString())) {
                    changePass.setError("Password tidak boleh sama dengan password sebelumnya");
                } else {
                    pswd = curPass.getText().toString().trim();
                    changePswd = changePass.getText().toString().trim();
                    rePass = reTypePass.getText().toString().trim();
                    SharedPreferences preferences = EditPasswordActivity.this
                            .getSharedPreferences(Session.PREF_NAME, 0);
                    String password = preferences.getString("password", null);
                    Log.d("password",password.toString());
                    if (rePass.equals(changePswd)) {
                        //fungsi add ke database dan penyocokan current password dengan database
                        try {
                            if (encMd5(pswd).equals(password)){
                                new EditPasswordAsyncTask().execute(rePass);
                            }else{
                                curPass.setError("Password tidak sama");
                            }
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    } else {
                        reTypePass.setError("Password tidak sama");
                    }
                }
            }
        });


    }

    private String encMd5(String password) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    //berfungsi untuk menghapus data buku ke database
    private class EditPasswordAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditPasswordActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String respon = "";
            try {
                String url = Constrants.URL_USER;
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                SharedPreferences preferences = EditPasswordActivity.this
                        .getSharedPreferences(Session.PREF_NAME, 0);
                postParameters.add(new BasicNameValuePair("action", "update_password"));
                postParameters.add(new BasicNameValuePair("id_u", preferences.getString("id_u",null)));
                postParameters.add(new BasicNameValuePair("password", params[0]));

                respon = CustomHttpClient.executeHttpPost(url, postParameters);

            } catch (Exception e) {
                respon = e.toString();
            }
            return respon;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.e("Respon", " -> " + result);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getString("status").equalsIgnoreCase("success")) {
                    Session.updatePasswordSession(EditPasswordActivity.this, encMd5(rePass));
                    Toast.makeText(EditPasswordActivity.this, "Password anda telah berhasil di ubah", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EditPasswordActivity.this, "Password anda gagal diubah", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(EditPasswordActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }
}
