package com.example.labtestapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.labtestapplication.fragment.Explore;
import com.example.labtestapplication.fragment.HomePage;
import com.example.labtestapplication.fragment.LabTests;
import com.example.labtestapplication.fragment.Profile;

public class MyFragmentAdapter extends FragmentStatePagerAdapter {

    int count;

    public MyFragmentAdapter(FragmentManager fm, int count) {
        super(fm);
        this.count = count;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: HomePage home = new HomePage();
                return home;
            case 1: LabTests labTests = new LabTests();
                return labTests;
            case 2: Explore explore = new Explore();
                return explore;
            case 3: Profile profile = new Profile();
                return profile;
        }
        return null;
    }

    @Override
    public int getCount() {
        return count;
    }
}

