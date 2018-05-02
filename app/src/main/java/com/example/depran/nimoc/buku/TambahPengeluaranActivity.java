package com.example.depran.nimoc.buku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.depran.nimoc.R;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;
import com.example.depran.nimoc.utils.Session;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TambahPengeluaranActivity extends AppCompatActivity {

    List<Divisi> listItems = new ArrayList<>();
    Spinner spinnerDivisi;
    ArrayAdapter<Divisi> spinnerAdapter;

    EditText nominalEditText, ketEditText;
    Button cancelBtn, tambahBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pengeluaran);

        spinnerDivisi = (Spinner) findViewById(R.id.spinnerDivisi);
        spinnerAdapter = new ArrayAdapter<Divisi>(TambahPengeluaranActivity.this, android.R.layout.simple_spinner_item, listItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDivisi.setAdapter(spinnerAdapter);
        new DivisiAsyncTask().execute();

        nominalEditText = (EditText) findViewById(R.id.nominal_pengeluaran);
        ketEditText = (EditText) findViewById(R.id.ket_pengeluaran);
        cancelBtn = (Button) findViewById(R.id.btnCancel);
        tambahBtn = (Button) findViewById(R.id.btnTambah);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tambahBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nominalEditText.getText().toString().isEmpty()){
                    nominalEditText.setError("Nominal tidak boleh kosong");
                }else if(ketEditText.getText().toString().isEmpty()){
                    ketEditText.setError("Keterangan tidak boleh kosong");
                }else if(ketEditText.getText().toString().length() < 5){
                    ketEditText.setError("Keterangan terlalu pendek");
                }else{
                    String nominal = nominalEditText.getText().toString().trim();
                    String ketPengeluaran = ketEditText.getText().toString().trim();
                    Divisi s = (Divisi) spinnerDivisi.getSelectedItem();
                    new  TambahPengeluaranKeuanganAsyncTask().execute(s.idDivisi.toString().trim(),nominal,ketPengeluaran);
                }
            }
        });
    }
    private class Divisi {
        String idDivisi, namaDivisi;

        public Divisi(String idDivisi, String namaDivisi) {
            this.idDivisi = idDivisi;
            this.namaDivisi = namaDivisi;
        }

        public String getIdDivisi() {
            return idDivisi;
        }

        public String getNamaDivisi() {
            return namaDivisi;
        }

        @Override
        public String toString() {
            return namaDivisi;
        }
    }

    //berfungsi untuk tambah buku ke database
    private class TambahPengeluaranKeuanganAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TambahPengeluaranActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String respon = "";
            try {
                String url = Constrants.URL_GET_RIWAYAT;
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                SharedPreferences preferences = TambahPengeluaranActivity.this
                        .getSharedPreferences(Session.PREF_NAME, 0);
                postParameters.add(new BasicNameValuePair("action", "pengeluaran"));
                postParameters.add(new BasicNameValuePair("owner", preferences.getString("id_u", null)));
                postParameters.add(new BasicNameValuePair("id_ck", preferences.getString("id_buku", null)));
                postParameters.add(new BasicNameValuePair("id_divisi", params[0]));
                postParameters.add(new BasicNameValuePair("pengeluaran_ke_dvs", params[1]));
                postParameters.add(new BasicNameValuePair("keterangan", params[2]));

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
                    Intent i = new Intent(TambahPengeluaranActivity.this, DashboardBukuActivity.class);
                    startActivity(i);
                    TambahPengeluaranActivity.this.finish();
                } else {
//                    Toast.makeText(TambahPengeluaranActivity.this, "Username atau Password anda salah", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(TambahPengeluaranActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }

    //berfungsi untuk mengambil data divisi ke database
    private class DivisiAsyncTask extends AsyncTask<String, String, String> {
        HashMap<Integer, String> mDivisis = new HashMap<Integer, String>();
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TambahPengeluaranActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String respon = "";
            try {
                SharedPreferences preferences = TambahPengeluaranActivity.this
                        .getSharedPreferences(Session.PREF_NAME, 0);
                String id = preferences.getString("id_buku", null);
                String url = Constrants.URL_GET_DIVISI + "?f_id_ck=" + id;
                respon = CustomHttpClient.executeHttpGet(url);

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
                JSONArray jsonArray = new JSONArray(result);
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String idBuku = object.getString("id_divisi");
                        String namaBuku = object.getString("nama_divisi");
                        listItems.add(new Divisi(idBuku,namaBuku));
                        spinnerAdapter.notifyDataSetChanged();
                    }
                }else{
                    //tidak ada divisi
                }
            } catch (Exception e) {
                Toast.makeText(TambahPengeluaranActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }
}
