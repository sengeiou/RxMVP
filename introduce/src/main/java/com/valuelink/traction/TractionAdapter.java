package com.valuelink.traction;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author nathaniel
 */
public class TractionAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;

    public TractionAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
        super(fragmentManager);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        // Integer.MAX_VALUE;
        return fragmentList.size();
    }
}
