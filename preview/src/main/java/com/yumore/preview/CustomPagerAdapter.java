package com.yumore.preview;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * @author Nathaniel
 * @date 2019/9/22 - 14:38
 */
public class CustomPagerAdapter extends PagerAdapter {

    private final List<View> viewList;

    public CustomPagerAdapter(List<View> viewList) {
        this.viewList = viewList;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
