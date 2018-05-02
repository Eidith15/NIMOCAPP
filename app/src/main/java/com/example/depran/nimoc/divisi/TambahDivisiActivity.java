package com.example.depran.nimoc.divisi;

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
import com.example.depran.nimoc.buku.BerandaActivity;
import com.example.depran.nimoc.buku.DashboardBukuActivity;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;
import com.example.depran.nimoc.utils.Session;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class TambahDivisiActivity extends AppCompatActivity {
    EditText namaDivisiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_divisi);
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        namaDivisiText = (EditText) findViewById(R.id.namaDivisi);
        findViewById(R.id.btnTambah).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(namaDivisiText.getText().toString().isEmpty()){
                    namaDivisiText.setError("Nama divisi tidak boleh kosong");
                }else if(namaDivisiText.getText().toString().length() < 5){
                    namaDivisiText.setError("Nama divisi terlalu pendek");
                }else {
                    String nmBuku = namaDivisiText.getText().toString().trim();
                    new TambahDivisiAsyncTask().execute(nmBuku);
                }
            }
        });
    }

    //berfungsi untuk tambah buku ke database
    private class TambahDivisiAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TambahDivisiActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String respon = "";
            try {
                String url = Constrants.URL_GET_DIVISI;
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                SharedPreferences preferences = TambahDivisiActivity.this
                        .getSharedPreferences(Session.PREF_NAME, 0);
                postParameters.add(new BasicNameValuePair("f_id_ck", preferences.getString("id_buku", null)));
                postParameters.add(new BasicNameValuePair("nama_divisi", params[0]));

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
                    Intent i = new Intent(TambahDivisiActivity.this, DashboardBukuActivity.class);
                    startActivity(i);
                    TambahDivisiActivity.this.finish();
                } else {
                    Toast.makeText(TambahDivisiActivity.this, "Tambah divisi Gagal pastikan koneksi anda terhubung", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(TambahDivisiActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }
}
