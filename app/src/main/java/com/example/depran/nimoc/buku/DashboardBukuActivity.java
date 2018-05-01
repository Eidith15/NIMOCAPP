package com.example.depran.nimoc.buku;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.depran.nimoc.BerandaFragment;
import com.example.depran.nimoc.CatatanKeuanganFragment;
import com.example.depran.nimoc.DashboardBukuFragment;
import com.example.depran.nimoc.DivisiEmptyFragment;
import com.example.depran.nimoc.DivisiFragment;
import com.example.depran.nimoc.R;
import com.example.depran.nimoc.RiwayatFragment;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;
import com.example.depran.nimoc.utils.Session;

import org.json.JSONArray;

import java.text.NumberFormat;
import java.util.Locale;

public class DashboardBukuActivity extends AppCompatActivity {

    Button dashboardBukuBtn, riwayatBtn, divisiBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_buku);

        changeContent(DashboardBukuFragment.newInstance(this));
        dashboardBukuBtn = (Button) findViewById(R.id.dashboard_buku_btn);
        riwayatBtn = (Button) findViewById(R.id.dashboard_riwayat_btn);
        divisiBtn = (Button) findViewById(R.id.dashboard_divisi_btn);

        dashboardBukuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeContent(DashboardBukuFragment.newInstance(DashboardBukuActivity.this));
            }
        });
        riwayatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeContent(RiwayatFragment.newInstance(DashboardBukuActivity.this));
            }
        });
        divisiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DivisiAsyncTask().execute();
            }
        });

    }

    public void changeContent(Fragment fragment){
        //app.Fragment
        getFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutBuku, fragment)
                .commitAllowingStateLoss();

        //v4
//        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frameLayoutBuku, fragment);
//        fragmentTransaction.commit();
    }



    //berfungsi untuk mengambil data buku ke database
    private class DivisiAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DashboardBukuActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String respon = "";
            try {
                SharedPreferences preferences = DashboardBukuActivity.this
                        .getSharedPreferences(Session.PREF_NAME, 0);
                String id = preferences.getString("id_buku", null);
                String url = Constrants.URL_GET_DIVISI+"?f_id_ck="+id;
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
            Log.e("Respon"," -> "+result) ;
            try {
                JSONArray jsonArray = new JSONArray(result);
                if (jsonArray.length()>0){
                    changeContent(DivisiFragment.newInstance(DashboardBukuActivity.this,jsonArray));
                }else{
                    changeContent(DivisiEmptyFragment.newInstance(DashboardBukuActivity.this));
                }

            } catch (Exception e) {
                Toast.makeText(DashboardBukuActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk","-> "+e.getMessage()) ;
            }
        }
    }
}
