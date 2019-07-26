package com.yumore.common.adapter;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.yumore.common.common.utility.EmptyUtils;

import java.util.List;

/**
 * 不同页面的fragment的父类不一样
 *
 * @author Nathaniel
 * @date 18-6-29-下午1:43
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    /**
     * 不用String的原因是CharSequence的子类更多
     */
    private List<String> stringList;
    private FragmentManager fragmentManager;

    public FragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
    }

    public FragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.fragmentList = fragmentList;
    }

    public void setFragmentList(List<Fragment> fragmentList) {
//        if (!EmptyUtils.isObjectEmpty(fragmentList)) {
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            for (Fragment fragment : fragmentList) {
//                fragmentTransaction.remove(fragment);
//            }
//            fragmentTransaction.commit();
//            fragmentManager.executePendingTransactions();
//        }
        this.fragmentList = fragmentList;
        notifyDataSetChanged();
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList == null ? null : fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return stringList == null ? null : stringList.get(position);
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    /**
     * 这样导致死循环，没找到原因
     *
     * @param position position
     * @param title    title
     */
    public void setPageTitle(int position, @NonNull String title) {
        if (EmptyUtils.isObjectEmpty(stringList) || position > stringList.size()) {
            return;
        }
        stringList.set(position, title);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }
}
