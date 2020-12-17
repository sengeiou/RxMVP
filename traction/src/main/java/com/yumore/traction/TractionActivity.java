package com.yumore.traction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yumore.provider.ISampleProvider;
import com.yumore.provider.RouterConstants;
import com.yumore.traction.databinding.ActivityTractionBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nathaniel
 */
@Route(path = RouterConstants.TRACTION_HOME)
public class TractionActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, OnFragmentToActivity<Integer>, View.OnClickListener {
    private final int[] videoRes = new int[]{
            R.raw.guide1,
            R.raw.guide2,
            R.raw.guide3
    };
    private ActivityTractionBinding tractionBinding;
    private LinearLayout.LayoutParams focusedParams, unfocusedParams;

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        tractionBinding = ActivityTractionBinding.inflate(getLayoutInflater());
        setContentView(tractionBinding.getRoot());

        initialize();
    }

    private void initialize() {
        List<Fragment> fragmentList = new ArrayList<>();
        focusedParams = new LinearLayout.LayoutParams(dip2px(getApplicationContext(), 7), dip2px(getApplicationContext(), 7));
        focusedParams.leftMargin = dip2px(getApplicationContext(), 15);
        unfocusedParams = new LinearLayout.LayoutParams(dip2px(getApplicationContext(), 10), dip2px(getApplicationContext(), 10));
        unfocusedParams.leftMargin = dip2px(getApplicationContext(), 15);
        View dotView;
        for (int i = 0; i < videoRes.length; i++) {
            dotView = new View(this);
            if (i == 0) {
                dotView.setLayoutParams(unfocusedParams);
                dotView.setBackgroundResource(R.drawable.tranction_shape_circle_magenta_solid);
            } else {
                dotView.setLayoutParams(focusedParams);
                dotView.setBackgroundResource(R.drawable.tranction_shape_circle_gray_solid);
            }
            tractionBinding.indicatorLayout.addView(dotView);
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
        tractionBinding.tractionViewpager.setOffscreenPageLimit(fragmentList.size());
        tractionBinding.tractionViewpager.setAdapter(fragmentAdapter);
        tractionBinding.tractionViewpager.addOnPageChangeListener(this);
        tractionBinding.tractionLayout.bringToFront();
        tractionBinding.tractionEnter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.traction_enter) {
            ISampleProvider sampleProvider = ARouter.getInstance().navigation(ISampleProvider.class);
            sampleProvider.setTractionEnable(true);
            ARouter.getInstance().build(RouterConstants.EXAMPLE_HOME).navigation();
            finish();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < tractionBinding.indicatorLayout.getChildCount(); i++) {
            View dotView = tractionBinding.indicatorLayout.getChildAt(i);
            if (i == position) {
                dotView.setLayoutParams(unfocusedParams);
                dotView.setBackgroundResource(R.drawable.tranction_shape_circle_magenta_solid);
            } else {
                dotView.setLayoutParams(focusedParams);
                dotView.setBackgroundResource(R.drawable.tranction_shape_circle_gray_solid);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCallback(String action, Integer integer) {
        int position = tractionBinding.tractionViewpager.getCurrentItem();
        if (integer == position) {
            integer++;
            tractionBinding.tractionViewpager.setCurrentItem(integer, true);
        }
    }
}
