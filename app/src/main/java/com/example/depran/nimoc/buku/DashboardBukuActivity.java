package com.example.depran.nimoc.buku;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.depran.nimoc.DashboardBukuFragment;
import com.example.depran.nimoc.R;
import com.example.depran.nimoc.RiwayatFragment;
import com.example.depran.nimoc.arsip.ArsipFragment;
import com.example.depran.nimoc.etc.BantuanActivity;
import com.example.depran.nimoc.etc.ProfileActivity;
import com.example.depran.nimoc.user.LoginActivity;
import com.example.depran.nimoc.utils.Session;

public class DashboardBukuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button dashboardBukuBtn, riwayatBtn, divisiBtn;
    TextView namaUser, emailUser;
    ImageView imageUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_buku);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        SharedPreferences preferences = DashboardBukuActivity.this
                .getSharedPreferences(Session.PREF_NAME, 0);
        String username = preferences.getString("username", null);
        namaUser.setText(username);

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
                changeContent(DashboardBukuFragment.newInstance(DashboardBukuActivity.this));
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_buku, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            finish();
        } else if (id == R.id.nav_archive) {
//            startActivity(new Intent(DashboardBukuActivity.this, ArsipFragment.class));
        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(DashboardBukuActivity.this, ProfileActivity.class));
        } else if (id == R.id.nav_help) {
            startActivity(new Intent(DashboardBukuActivity.this, BantuanActivity.class));
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
                Toast.makeText(DashboardBukuActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_about) {
//            startActivity(new Intent(DashboardBukuActivity.this, Abou.class));
        } else if (id == R.id.nav_logout) {
                konfirmasiKeluar();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
                        Session.logout(DashboardBukuActivity.this);
                        startActivity(new Intent(DashboardBukuActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .show();
    }
}
