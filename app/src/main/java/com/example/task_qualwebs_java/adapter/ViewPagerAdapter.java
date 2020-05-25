package com.example.task_qualwebs_java.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.task_qualwebs_java.ui.fragment.LeftFragment;
import com.example.task_qualwebs_java.ui.fragment.RightFragment;
import com.example.task_qualwebs_java.ui.fragment.StartFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new LeftFragment();
            case 1:
                return new StartFragment();
            case 2:
                return new RightFragment();
            default:
                return new StartFragment();
        }
    }


}
