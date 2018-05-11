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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.depran.nimoc.R;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;
import com.example.depran.nimoc.utils.Session;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity {

    EditText username, telepon, email;
    RadioButton perempuan, laki;
    Button doneEdit;
    RadioGroup jenisKelamin;
    String user, telp, mail, jk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        username = (EditText) findViewById(R.id.edtUsername);
        telepon = (EditText) findViewById(R.id.edtTelpon);
        email = (EditText) findViewById(R.id.edtEmail);
        laki = (RadioButton) findViewById(R.id.checkLaki);
        perempuan = (RadioButton) findViewById(R.id.checkPerempuan);
        doneEdit = (Button) findViewById(R.id.btnDoneProfile);
        jenisKelamin = (RadioGroup) findViewById(R.id.jk);

        SharedPreferences preferences = EditProfileActivity.this
                .getSharedPreferences(Session.PREF_NAME, 0);
        String sUsername = preferences.getString("username", null);
        String sEmail = preferences.getString("email", null);
        String sNo_hp = preferences.getString("no_hp", null);
        String sJenis_kelamin = preferences.getString("jenis_kelamin", null);
        username.setText(sUsername);
        telepon.setText(sNo_hp);
        email.setText(sEmail);
        if (sJenis_kelamin.equalsIgnoreCase("0")) {
            perempuan.setChecked(true);
        } else {
            laki.setChecked(true);
        }

        doneEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = username.getText().toString().trim();
                telp = telepon.getText().toString().trim();
                mail = email.getText().toString().trim();
                if (user.isEmpty()) {
                    username.setError("Username tidak boleh kosong");
                } else if (user.length() < 5) {
                    username.setError("Username minimal 5 karakter");
                } else if (!cekKarakterUnik(user)) {
                    username.setError("Symbol hanya boleh titik . atau garis bawah _ setelah huruf");
                } else if (telp.isEmpty()) {
                    telepon.setError("No.Telpon tidak boleh kosong");
                } else if (telp.length() > 13) {
                    telepon.setError("No.Telpon terlalu panjang");
                } else if (telp.length() < 5) {
                    telepon.setError("No.Telpon terlalu pendek");
                } else if (mail.isEmpty()) {
                    email.setError("Email tidak boleh kosong");
                } else if (!emailValidate(email.getText().toString().trim())) {
                    email.setError("Email Tidak Valid");
                } else {

                    if (jenisKelamin.getCheckedRadioButtonId() == -1) {
                        laki.setError("Harus memiilih jenis kelamin");
                        perempuan.setError("Harus memilikih jenis kelamin");
                    } else {
                        if (laki.isChecked()) {
                            jk = "1";
                        } else {
                            jk = "0";
                        }
                        //fungsi add ke database
                        new EditProfileAsyncTask().execute(user, mail, jk, telp);
                    }
                }
            }
        });


    }

    private boolean cekKarakterUnik(String s) {
        return s.matches("^[a-z A-Z]+[[._][a-zA-Z_0-9]]*");
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean emailValidate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    //berfungsi untuk menghapus data buku ke database
    private class EditProfileAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditProfileActivity.this);
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
                SharedPreferences preferences = EditProfileActivity.this
                        .getSharedPreferences(Session.PREF_NAME, 0);
                postParameters.add(new BasicNameValuePair("action", "update_account"));
                postParameters.add(new BasicNameValuePair("id_u", preferences.getString("id_u", null)));
                postParameters.add(new BasicNameValuePair("username", params[0]));
                postParameters.add(new BasicNameValuePair("email", params[1]));
                postParameters.add(new BasicNameValuePair("jenis_kelamin", params[2]));
                postParameters.add(new BasicNameValuePair("no_hp", params[3]));

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
                    Session.updateAccountSession(EditProfileActivity.this, user, mail, telp, jk);
                    Toast.makeText(EditProfileActivity.this, "Profile anda berhasil di ubah", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Profile anda gagal di ubah", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(EditProfileActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }
}
