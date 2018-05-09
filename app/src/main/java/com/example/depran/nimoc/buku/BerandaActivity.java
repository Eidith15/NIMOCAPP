package com.example.depran.nimoc.buku;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.depran.nimoc.BerandaFragment;
import com.example.depran.nimoc.CatatanKeuanganFragment;
import com.example.depran.nimoc.R;
import com.example.depran.nimoc.arsip.ArsipFragment;
import com.example.depran.nimoc.arsip.CatatanArsipFragment;
import com.example.depran.nimoc.etc.AboutActivity;
import com.example.depran.nimoc.etc.BantuanActivity;
import com.example.depran.nimoc.etc.ProfileActivity;
import com.example.depran.nimoc.user.LoginActivity;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;
import com.example.depran.nimoc.utils.Session;

import org.json.JSONArray;

public class BerandaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView namaUser, emailUser;
    ImageView imageUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_beranda);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        namaUser = (TextView) headerView.findViewById(R.id.nama_user);
        emailUser = (TextView) headerView.findViewById(R.id.email_user);
        imageUser= (ImageView) headerView.findViewById(R.id.image_user);
        SharedPreferences preferences = BerandaActivity.this
                .getSharedPreferences(Session.PREF_NAME, 0);
        String username = preferences.getString("username", null);
        String email = preferences.getString("email", null);
        namaUser.setText(username);
        if (email.isEmpty()){
            emailUser.setText("Klik settings untuk melengkapi profile anda");
        }else{
            emailUser.setText(email);
        }
        new BukuAsyncTask().execute();
    }

    public void changeContent(Fragment fragment) {
        //app.Fragment
        getFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutBeranda, fragment)
                .commitAllowingStateLoss();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_beranda);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_buku) {
            return true;
        }

        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            new BukuAsyncTask().execute();
        } else if (id == R.id.nav_archive) {
            new ArsipAsyncTask().execute();
        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(BerandaActivity.this, ProfileActivity.class));
        } else if (id == R.id.nav_help) {
            startActivity(new Intent(BerandaActivity.this, BantuanActivity.class));
        } else if (id == R.id.nav_chat) {
            Intent i;
            String mailto = "mailto:ade.pranaya@mail.unpas.ac.id" +
                    "?cc=" + "" +
                    "&subject=" + Uri.encode("MASUKAN_NIMOC_FINANCE") +
                    "&body=" + Uri.encode("");

            i = new Intent(Intent.ACTION_SENDTO);
            i.setData(Uri.parse(mailto));

            try {
                startActivity(i);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(BerandaActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(BerandaActivity.this, AboutActivity.class));
        } else if (id == R.id.nav_logout) {
            konfirmasiKeluar();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_beranda);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void konfirmasiKeluar(){
        new MaterialDialog.Builder(this)
                .title(R.string.title_logout)
                .content(R.string.content_logout)
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Session.logout(BerandaActivity.this);
                        startActivity(new Intent(BerandaActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .show();
    }

    //berfungsi untuk mengambil data buku ke database
    private class BukuAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BerandaActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String respon = "";
            try {
                SharedPreferences preferences = BerandaActivity.this
                        .getSharedPreferences(Session.PREF_NAME, 0);
                String id = preferences.getString("id_u", null);
                String url = Constrants.URL_GET_BUKU+"&f_id_r="+id;
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
                    changeContent(CatatanKeuanganFragment.newInstance(BerandaActivity.this,jsonArray));
                }else{
                    changeContent(BerandaFragment.newInstance(BerandaActivity.this));
                }

            } catch (Exception e) {
                Toast.makeText(BerandaActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk","-> "+e.getMessage()) ;
            }
        }
    }
    //berfungsi untuk mengambil data buku ke database
    private class ArsipAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BerandaActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String respon = "";
            try {
                SharedPreferences preferences = BerandaActivity.this
                        .getSharedPreferences(Session.PREF_NAME, 0);
                String id = preferences.getString("id_u", null);
                String url = Constrants.URL_GET_ARSIP+"&f_id_r="+id;
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
                    changeContent(CatatanArsipFragment.newInstance(BerandaActivity.this,jsonArray));
                }else{
                    changeContent(ArsipFragment.newInstance(BerandaActivity.this));
                }

            } catch (Exception e) {
                Toast.makeText(BerandaActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk","-> "+e.getMessage()) ;
            }
        }
    }
}
