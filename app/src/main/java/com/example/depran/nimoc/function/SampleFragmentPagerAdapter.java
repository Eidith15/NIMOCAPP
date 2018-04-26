package com.example.depran.nimoc.function;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by depran7 on 3/3/2018.
 */

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 5;
    private String tabTitles[] = new String[]{"DASHBOARD", "RIWAYAT", "DIVISI"};

    public SampleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
//            case 0:
//                return ProfileFragment.newInstance(position);
//            case 1:
//                return GalleryFragment.newInstance(position);
//            case 2:
//                return ContactFragment.newInstance(position);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
