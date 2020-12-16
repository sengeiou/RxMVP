package com.yumore.example.surface;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.BaseActivity;
import com.yumore.utility.widget.RxTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yumore
 */
public class LoadingActivity extends BaseActivity {

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
        mRxTitle.setLeftFinish(baseActivity);
        mViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            final String[] titles = new String[]{
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
