package com.example.depran.nimoc.etc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.depran.nimoc.R;

public class BantuanDetailActivity extends AppCompatActivity {

    TextView deskripsiTextView,fungsiTextView,caraPakaiTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan_detail);

        Intent intent = getIntent();
        String label = intent.getStringExtra("label");
        setTitle(label);
        String deskripsi = intent.getStringExtra("deskripsi");
        String fungsi = intent.getStringExtra("fungsi");
        String caraPakai = intent.getStringExtra("cara_pakai");

        deskripsiTextView = (TextView) findViewById(R.id.deskripsi);
        fungsiTextView= (TextView) findViewById(R.id.fungsi);
        caraPakaiTextView = (TextView) findViewById(R.id.cara_pakai);

        deskripsiTextView.setText(deskripsi);
        fungsiTextView.setText(fungsi);
        caraPakaiTextView.setText(caraPakai);
    }
}
