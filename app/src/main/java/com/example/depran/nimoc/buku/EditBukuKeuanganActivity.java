package com.example.depran.nimoc.buku;

import android.app.ProgressDialog;
import android.content.Intent;
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
import org.json.JSONObject;

import java.util.ArrayList;

public class EditBukuKeuanganActivity extends AppCompatActivity {

    Button cancelButton, editButton;
    EditText namaBukuEditText, ketEditText;
    TextView idBukuTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_buku_keuangan);

        // Get data sent from calling activity.
        Bundle extras = getIntent().getExtras();
        String idBuku = extras.getString("id_buku");
        String namaBuku = extras.getString("nama_buku", "");
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

            }
        });

    }

    /**
     * Click handler for the Save button.
     * Creates a new intent for the reply, adds the reply message to it as an extra,
     * sets the intent result, and closes the activity.
     *
     * @param view The view that was clicked.
     */
    public void returnReply(View view) {
//        String word = ((EditText) findViewById(R.id.edit_word)).getText().toString();
//        String desc= ((EditText) findViewById(R.id.edit_desc)).getText().toString();
//
//        Intent replyIntent = new Intent();
//        replyIntent.putExtra(EXTRA_REPLY, word);
//        replyIntent.putExtra(EXTRA_REPLY1, desc);
//        replyIntent.putExtra(WordListAdapter.EXTRA_ID, mId);
//        setResult(RESULT_OK, replyIntent);
//        finish();
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
                ArrayList<NameValuePair> putParameters = new ArrayList<NameValuePair>();
//                putParameters.add(new BasicNameValuePair("username", params[0]));
//                putParameters.add(new BasicNameValuePair("password", params[1]));

                respon = CustomHttpClient.executeHttpPut(url, putParameters);

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

                    String idUser = object.getJSONObject("data").getString("id_u");
                    String username = object.getJSONObject("data").getString("username");
                    String password = object.getJSONObject("data").getString("password");

                    //pengisian data idUser, username, password ke Session...
                    Session.createLoginSession(EditBukuKeuanganActivity.this, idUser, username, password);

                    //Toast.makeText(Login2Activity.this, "Pindah ke BerandaActivity",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(EditBukuKeuanganActivity.this, BerandaActivity.class));
                    EditBukuKeuanganActivity.this.finish();

                } else {
                    Toast.makeText(EditBukuKeuanganActivity.this, "Username atau Password anda salah", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(EditBukuKeuanganActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }
}
