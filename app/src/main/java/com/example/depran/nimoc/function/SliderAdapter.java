package com.example.depran.nimoc.function;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.depran.nimoc.R;

/**
 * Created by Eidith on 22/04/2018.
 */

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    //arrays
    public int[] slide_images = {
            R.drawable.awal1icon,
            R.drawable.awal2icon,
            R.drawable.awal3icon
    };

    public String[] slide_headings = {
            "What Is NiMoC ?",
            "Easy Organizing",
            "Easy and Efficient"
    };

    public String[] slide_content = {
            "Nimoc merupakan aplikasi keuangan untuk memudahkan setiap organisasi didalam mengatur catatan keuangan",
            "Memudahkan organisasi untuk mengatur catatan keuangan dari setiap divisi yang ada pada organisasinya",
            "Dengan menggunakan aplikasi keuangan daru nimoc ini memudahkan setiap organisasi dan tidak membuat sulit untuk membuat laporan keuangan"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view,Object o) {
        return view == (RelativeLayout) o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slideImage);
        TextView slideHeading = (TextView) view.findViewById(R.id.textHeader);
        TextView slideContent = (TextView) view.findViewById(R.id.textcontent);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideContent.setText(slide_content[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((RelativeLayout)object);
    }
}
