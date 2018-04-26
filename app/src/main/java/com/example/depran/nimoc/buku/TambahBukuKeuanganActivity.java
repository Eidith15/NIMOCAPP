package com.example.depran.nimoc.buku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.depran.nimoc.R;
import com.example.depran.nimoc.opening.SplashScreenActivity;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;
import com.example.depran.nimoc.utils.Session;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class TambahBukuKeuanganActivity extends AppCompatActivity {

    EditText namaBukuText, keteranganBukuText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_buku_keuangan);

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        namaBukuText = (EditText) findViewById(R.id.ttextBuku);
        keteranganBukuText = (EditText) findViewById(R.id.ttextKet);
        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nmBuku = namaBukuText.getText().toString().trim();
                String ketBuku = namaBukuText.getText().toString().trim();
                new TambahBukuKeuanganAsyncTask().execute(nmBuku, ketBuku);
            }
        });
    }

    //berfungsi untuk tambah buku ke database
    private class TambahBukuKeuanganAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TambahBukuKeuanganActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String respon = "";
            try {
                String url = Constrants.URL_BUKU;
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                SharedPreferences preferences = TambahBukuKeuanganActivity.this
                        .getSharedPreferences(Session.PREF_NAME, 0);
                postParameters.add(new BasicNameValuePair("f_id_r", preferences.getString("id_u", null)));
                postParameters.add(new BasicNameValuePair("nama_buku", params[0]));
                postParameters.add(new BasicNameValuePair("keterangan", params[1]));

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
                    Intent i = new Intent(TambahBukuKeuanganActivity.this, BerandaActivity.class);
                    startActivity(i);
                    TambahBukuKeuanganActivity.this.finish();
                } else {
//                    Toast.makeText(TambahBukuKeuanganActivity.this, "Username atau Password anda salah", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(TambahBukuKeuanganActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }
}
