package com.example.task_qualwebs_java.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.task_qualwebs_java.ui.fragment.FaboritiesFragment;
import com.example.task_qualwebs_java.ui.fragment.LeftFragment;
import com.example.task_qualwebs_java.ui.fragment.MenuFragment;
import com.example.task_qualwebs_java.ui.fragment.RecentFragment;
import com.example.task_qualwebs_java.ui.fragment.RightFragment;
import com.example.task_qualwebs_java.ui.fragment.StartFragment;

public class MenuPagerAdapter extends FragmentPagerAdapter {
    public MenuPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MenuFragment();
            case 1:
                return new RecentFragment();
            case 2:
                return new FaboritiesFragment();
            default:
                return new StartFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
