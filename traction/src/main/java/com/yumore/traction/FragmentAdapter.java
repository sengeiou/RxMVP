package com.yumore.traction;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * fragmentAdapter
 * @author Nathaniel
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    private FragmentManager fragmentManager;

    public FragmentAdapter(List<Fragment> fragmentList, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.fragmentList = fragmentList;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return fragmentList != null && !fragmentList.isEmpty() ? fragmentList.size() : 0;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
}
