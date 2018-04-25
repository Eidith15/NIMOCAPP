package com.example.depran.nimoc.opening;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.depran.nimoc.R;
import com.example.depran.nimoc.function.SliderAdapter;
import com.example.depran.nimoc.user.LoginActivity;
import com.example.depran.nimoc.utils.Session;


public class FirstSliderActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mdotLayout;

    private Button mNextbtn;
    private Button mBackbtn;
    private TextView[] mDots;

    private int mCurrentPage;

    private SliderAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        //hide status bar
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= 21) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow(); // in Activity's onCreate() for instance
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mdotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        mNextbtn = (Button) findViewById(R.id.btnNext);
        mBackbtn = (Button) findViewById(R.id.btnPrev);

        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        //onClickListener
        mNextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage+1);
            }
        });

        mBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage-1);
            }
        });
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        mdotLayout.removeAllViews();

        for (int i = 0;i < mDots.length;i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.white));

            mdotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.blackGrey));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int i, float v, int i1){

        }
        @Override
        public void onPageSelected(int i){

            addDotsIndicator(i);
            mCurrentPage = i;

            if(i == 0){
                mNextbtn.setEnabled(true);
                mBackbtn.setEnabled(false);
                mBackbtn.setVisibility(View.INVISIBLE);

                mNextbtn.setText("Next");
                mNextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSlideViewPager.setCurrentItem(mCurrentPage+1);
                    }
                });
                mBackbtn.setText("");
            }else if(i == mDots.length -1){
                mNextbtn.setEnabled(true);
                mBackbtn.setEnabled(true);
                mBackbtn.setVisibility(View.VISIBLE);

                mNextbtn.setText("Finish");
                mBackbtn.setText("Back");

                mNextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(FirstSliderActivity.this, LoginActivity.class);
                        Session.createAwalSession(FirstSliderActivity.this, false);
                        startActivity(intent);
                        finish();
                    }
                });
            }else{
                mNextbtn.setEnabled(true);
                mBackbtn.setEnabled(true);
                mBackbtn.setVisibility(View.VISIBLE);

                mNextbtn.setText("Next");
                mNextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSlideViewPager.setCurrentItem(mCurrentPage+1);
                    }
                });
                mBackbtn.setText("Back");

            }

        }
        public void onPageScrollStateChanged(int i){

        }
    };
}
