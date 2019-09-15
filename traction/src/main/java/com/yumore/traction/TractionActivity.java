package com.yumore.traction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yumore.provider.RouterConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nathaniel
 */
public class TractionActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, OnFragmentToActivity<Integer> {

    @BindView(R2.id.guide_viewpager)
    ExtendedViewPager extendedViewPager;
    @BindView(R2.id.enter_button)
    TextView textView;
    @BindView(R2.id.dot_layout)
    LinearLayout linearLayout;
    private List<Fragment> fragmentList = new ArrayList<>();
    private int[] videoRes = new int[]{R.raw.guide1, R.raw.guide2, R.raw.guide3};
    private LinearLayout.LayoutParams params1, params2;

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_traction);

        ButterKnife.bind(this);

        params1 = new LinearLayout.LayoutParams(dip2px(getApplicationContext(), 7), dip2px(getApplicationContext(), 7));
        params1.leftMargin = dip2px(getApplicationContext(), 15);
        params2 = new LinearLayout.LayoutParams(dip2px(getApplicationContext(), 10), dip2px(getApplicationContext(), 10));
        params2.leftMargin = dip2px(getApplicationContext(), 15);
        View dot;
        for (int i = 0; i < videoRes.length; i++) {
            dot = new View(this);
            if (i == 0) {
                dot.setLayoutParams(params2);
                dot.setBackgroundResource(R.drawable.dot_focus);
            } else {
                dot.setLayoutParams(params1);
                dot.setBackgroundResource(R.drawable.dot_unfocus);
            }
            linearLayout.addView(dot);
        }

        for (int i = 0; i < videoRes.length; i++) {
            TractionFragment fragment = new TractionFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("res", videoRes[i]);
            bundle.putInt("page", i);
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
        FragmentAdapter fragmentAdapter = new FragmentAdapter(fragmentList, getSupportFragmentManager());
        extendedViewPager.setOffscreenPageLimit(fragmentList.size());
        extendedViewPager.setAdapter(fragmentAdapter);
        extendedViewPager.addOnPageChangeListener(this);
    }

    @OnClick({
            R2.id.enter_button
    })
    public void onClick(View view) {
        if (view.getId() == R.id.enter_button) {
            ARouter.getInstance().build(RouterConstants.EXAMPLE_HOME).navigation();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View dot = linearLayout.getChildAt(i);
            if (i == position) {
                dot.setLayoutParams(params2);
                dot.setBackgroundResource(R.drawable.dot_focus);
            } else {
                dot.setLayoutParams(params1);
                dot.setBackgroundResource(R.drawable.dot_unfocus);
            }

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCallback(String action, Integer integer) {
        int i = extendedViewPager.getCurrentItem();
        if (integer == i) {
            integer += 1;
            extendedViewPager.setCurrentItem(integer, true);
        }
    }
}
