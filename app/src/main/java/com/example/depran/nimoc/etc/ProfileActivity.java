package com.example.depran.nimoc.etc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.depran.nimoc.R;
import com.example.depran.nimoc.buku.BerandaActivity;
import com.example.depran.nimoc.user.EditPasswordActivity;
import com.example.depran.nimoc.user.EditProfileActivity;
import com.example.depran.nimoc.user.LoginActivity;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;
import com.example.depran.nimoc.utils.Session;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    TextView usernameTextView, noHpTextView, emailTextView, jenisKTextView;
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
        emailTextView.setText(email.isEmpty() ? "-" : email);
        noHpTextView.setText(no_hp.isEmpty() ? "-" : no_hp);
        jenisKTextView.setText(jenis_kelamin.isEmpty() ? "-" : ((jenis_kelamin.equalsIgnoreCase("0")) ? "perempuan" : "laki-laki"));

        editPasswordBtn = (Button) findViewById(R.id.edit_password_btn);
        editProfileBtn = (Button) findViewById(R.id.edit_profile_btn);
        deleteProfileBtn = (Button) findViewById(R.id.delete_akun_btn);

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
                MaterialDialog.Builder builder = new MaterialDialog.Builder(ProfileActivity.this)
                        .title("Hapus Akun")
                        .content("Apakah anda yakin ingin menghapus akun anda? \nsemua data keuangan anda akan terhapus secara permanen")
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .input("Masukkan Password", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(final MaterialDialog dialog, CharSequence input) {
                                new HapusProfileAsyncTask().execute();
                            }
                        });

                final MaterialDialog dialog = builder.build();
                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                final EditText pasET = dialog.getInputEditText();
                pasET.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {

                        // you can call or do what you want with your EditText here
                        String passwordInput = pasET.getText().toString().trim();
                        Log.d("password",passwordInput);
                        SharedPreferences preferences = ProfileActivity.this
                                .getSharedPreferences(Session.PREF_NAME, 0);
                        String password = preferences.getString("password", null);
                        try {
                            if (encMd5(passwordInput).equals(password)) {
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                            } else {
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                            }
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }

                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                dialog.show();
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
    private class HapusProfileAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProfileActivity.this);
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
                SharedPreferences preferences = ProfileActivity.this
                        .getSharedPreferences(Session.PREF_NAME, 0);
                postParameters.add(new BasicNameValuePair("action", "delete"));
                postParameters.add(new BasicNameValuePair("id_u", preferences.getString("id_u", null)));

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
                    Toast.makeText(ProfileActivity.this, "Akun anda berhasil di hapus", Toast.LENGTH_LONG).show();
                    Session.logout(ProfileActivity.this);
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, "Hapus akun gagal", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(ProfileActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }
}
