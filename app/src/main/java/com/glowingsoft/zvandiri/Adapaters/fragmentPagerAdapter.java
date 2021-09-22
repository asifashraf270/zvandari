package com.glowingsoft.zvandiri.Adapaters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.glowingsoft.zvandiri.Fragments.AudioFragment;
import com.glowingsoft.zvandiri.Fragments.VideosFragment;

public class fragmentPagerAdapter extends FragmentPagerAdapter {

    public fragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new VideosFragment();
            case 1:
                return new AudioFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Videos";
            case 1:
                return "Audios";
        }
        return super.getPageTitle(position);
    }
}
