package com.example.depran.nimoc.buku;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.depran.nimoc.DashboardBukuFragment;
import com.example.depran.nimoc.DivisiEmptyFragment;
import com.example.depran.nimoc.R;
import com.example.depran.nimoc.RiwayatFragment;

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
                changeContent(DivisiEmptyFragment.newInstance(DashboardBukuActivity.this));
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
}
