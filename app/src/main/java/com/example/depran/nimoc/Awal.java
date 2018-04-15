package com.example.depran.nimoc;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Eidith on 15/04/2018.
 */

public class Awal  extends AppCompatActivity{

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.awal);

        viewPager = (ViewPager) findViewById(R.id.viewAwal);

        AwalViewPagerAdapter awalViewPagerAdapter = new AwalViewPagerAdapter(this);

        viewPager.setAdapter(awalViewPagerAdapter);
    }
}
