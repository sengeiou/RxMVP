package com.vondear.rxdemo.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import androidx.core.app.Fragment;
import androidx.core.app.FragmentPagerAdapter;
import androidx.core.view.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.vondear.rxdemo.R;
import com.vondear.rxdemo.fragment.FragmentLoadingDemo;
import com.vondear.rxdemo.fragment.FragmentLoadingWay;
import com.vondear.rxui.activity.ActivityBase;
import com.vondear.rxui.view.RxTitle;

/**
 * @author vondear
 */
public class ActivityLoading extends ActivityBase {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.viewpager)
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
