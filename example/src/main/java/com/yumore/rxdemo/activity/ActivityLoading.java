package com.yumore.rxdemo.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.material.tabs.TabLayout;
import com.yumore.rxdemo.R;
import com.yumore.rxdemo.R2;
import com.yumore.rxdemo.fragment.FragmentLoadingDemo;
import com.yumore.rxdemo.fragment.FragmentLoadingWay;
import com.yumore.rxui.activity.ActivityBase;
import com.yumore.rxui.view.RxTitle;

/**
 * @author yumore
 */
public class ActivityLoading extends ActivityBase {

    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R2.id.tabs)
    TabLayout mTabs;
    @BindView(R2.id.viewpager)
    ViewPager mViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);
        mViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            String[] titles = new String[]{
                    "加载的方式", "加载的例子"
            };

            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return FragmentLoadingWay.newInstance();
                } else {
                    return FragmentLoadingDemo.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        mTabs.setupWithViewPager(mViewpager);
    }
}
