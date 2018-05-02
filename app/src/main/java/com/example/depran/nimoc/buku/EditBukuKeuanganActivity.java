package com.example.depran.nimoc.buku;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.depran.nimoc.R;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;
import com.example.depran.nimoc.utils.Session;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditBukuKeuanganActivity extends AppCompatActivity {

    Button cancelButton, editButton;
    EditText namaBukuEditText, ketEditText;
    TextView idBukuTextView;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_buku_keuangan);

        // Get data sent from calling activity.
        Bundle extras = getIntent().getExtras();
        String idBuku = extras.getString("id_buku");
        position = extras.getInt("position");
        final String namaBuku = extras.getString("nama_buku", "");
        String ketBuku;
        if (extras.getString("ket_buku").isEmpty() || extras.getString("ket_buku").equalsIgnoreCase("null")) {
            ketBuku = "";
        }else{
            ketBuku = extras.getString("ket_buku", "");

        }

        namaBukuEditText = (EditText) findViewById(R.id.etextBuku);
        ketEditText = (EditText) findViewById(R.id.etextKet);
        idBukuTextView = (TextView) findViewById(R.id.id_buku);

        namaBukuEditText.setText(namaBuku);
        ketEditText.setText(ketBuku);
        idBukuTextView.setText(idBuku);

        cancelButton = (Button) findViewById(R.id.btnCancel);
        editButton = (Button) findViewById(R.id.btnEdit);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(namaBukuEditText.getText().toString().isEmpty()){
                    namaBukuEditText.setError("Nama buku tidak boleh kosong");
                }else if(ketEditText.getText().toString().isEmpty()){
                    ketEditText.setError("Keterangan tidak boleh kosong");
                }else if(namaBukuEditText.getText().toString().equals(namaBuku)){
                    namaBukuEditText.setError("Nama buku tidak boleh sama dengan nama sebelumnya");
                }else{
                    String namaBuku = namaBukuEditText.getText().toString().trim();
                    String ketBuku = ketEditText.getText().toString().trim();
                    String idBuku = idBukuTextView.getText().toString().trim();

                    new EditBukuAsyncTask().execute(idBuku, namaBuku, ketBuku);
                }
            }
        });

    }


    public void returnReply() {
        String namaBuku = namaBukuEditText.getText().toString().trim();
        String ketBuku = ketEditText.getText().toString().trim();
        String idBuku = idBukuTextView.getText().toString().trim();

        Intent replyIntent = new Intent();
        replyIntent.putExtra("com.example.depran.nimoc.position", position);
        replyIntent.putExtra("com.example.depran.nimoc.nama_buku", namaBuku);
        replyIntent.putExtra("com.example.depran.nimoc.ket_buku", ketBuku);
        replyIntent.putExtra("com.example.depran.nimoc.id_buku", idBuku);
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, replyIntent);
        } else {
            getParent().setResult(Activity.RESULT_OK, replyIntent);
        }
        finish();
    }

    //berfungsi untuk edit buku ke database
    private class EditBukuAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditBukuKeuanganActivity.this);
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
                SharedPreferences preferences = EditBukuKeuanganActivity.this
                        .getSharedPreferences(Session.PREF_NAME, 0);
                postParameters.add(new BasicNameValuePair("action", "update"));
                postParameters.add(new BasicNameValuePair("id_buku", params[0]));
                postParameters.add(new BasicNameValuePair("f_id_r", preferences.getString("id_u", null)));
                postParameters.add(new BasicNameValuePair("nama_buku", params[1]));
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
                    returnReply();
                    Toast.makeText(EditBukuKeuanganActivity.this, "Buku anda telah berhasil di ubah", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EditBukuKeuanganActivity.this, "Edit Buku gagal", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(EditBukuKeuanganActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }
}
