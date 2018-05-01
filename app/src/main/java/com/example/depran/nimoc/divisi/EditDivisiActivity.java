package com.example.depran.nimoc.divisi;

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

public class EditDivisiActivity extends AppCompatActivity {

    Button cancelButton, editButton;
    EditText namaDivisiEditText;
    TextView idDivisiTextView;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_divisi);

        // Get data sent from calling activity.
        Bundle extras = getIntent().getExtras();
        String idDivisi = extras.getString("id_divisi");
        String namaDivisi = extras.getString("nama_divisi");
        position = extras.getInt("position");
        if (namaDivisi.isEmpty() || namaDivisi.equalsIgnoreCase("null")) {
            namaDivisi = "";
        }else{
            namaDivisi = extras.getString("nama_divisi", "");

        }

        namaDivisiEditText = (EditText) findViewById(R.id.nama_divisi);
        idDivisiTextView = (TextView) findViewById(R.id.id_divisi);

        namaDivisiEditText.setText(namaDivisi);
        idDivisiTextView.setText(idDivisi);
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
                String namaDivisi = namaDivisiEditText.getText().toString().trim();
                String idDivisi = idDivisiTextView.getText().toString().trim();

                new EditDivisiAsyncTask().execute(idDivisi, namaDivisi);

            }
        });
    }

    public void returnReply() {
        String namaDivisi = namaDivisiEditText.getText().toString().trim();
        String idDivisi = idDivisiTextView.getText().toString().trim();

        Intent replyIntent = new Intent();
        replyIntent.putExtra("com.example.depran.nimoc.position", position);
        replyIntent.putExtra("com.example.depran.nimoc.nama_divisi", namaDivisi);
        replyIntent.putExtra("com.example.depran.nimoc.id_divisi", idDivisi);
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, replyIntent);
        } else {
            getParent().setResult(Activity.RESULT_OK, replyIntent);
        }
        finish();
    }

    //berfungsi untuk edit buku ke database
    private class EditDivisiAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditDivisiActivity.this);
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
                SharedPreferences preferences = EditDivisiActivity.this
                        .getSharedPreferences(Session.PREF_NAME, 0);
                postParameters.add(new BasicNameValuePair("action", "update"));
                postParameters.add(new BasicNameValuePair("id_divisi", params[0]));
                postParameters.add(new BasicNameValuePair("f_id_ck", preferences.getString("id_buku", null)));
                postParameters.add(new BasicNameValuePair("nama_buku", params[1]));

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
                    Toast.makeText(EditDivisiActivity.this, "Divisi anda telah berhasil di ubah", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EditDivisiActivity.this, "Edit Divisi gagal", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(EditDivisiActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }
}
