package com.example.depran.nimoc.divisi;

import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.depran.nimoc.DivisiEmptyFragment;
import com.example.depran.nimoc.R;
import com.example.depran.nimoc.buku.DashboardBukuActivity;

public class DivisiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divisi);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnTambah);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        changeContent(DivisiEmptyFragment.newInstance(this));
    }
    public void changeContent(Fragment fragment){
        //app.Fragment
        getFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutDivisi, fragment)
                .commitAllowingStateLoss();
    }
}
