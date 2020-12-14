package com.valuelink.traction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.valuelink.provider.constrant.RouterConstants;
import com.valuelink.provider.provider.IMasterProvider;
import com.valuelink.provider.provider.IProfileProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * @author nathaniel
 */
public class TractionActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private static final int[] IMAGE_CN_RESOURCES = {
            R.drawable.icon_indicator_cn_01,
            R.drawable.icon_indicator_cn_02,
            R.drawable.icon_indicator_cn_03
    };
    private static final int[] IMAGE_EN_RESOURCES = {
            R.drawable.icon_indicator_en_01,
            R.drawable.icon_indicator_en_02,
            R.drawable.icon_indicator_en_03
    };
    private static final int HANDLER_MESSAGE = 1, DELAY_MILLIS = 30 * 1000;
    private List<Fragment> fragmentList;
    private TextView textView;
    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams normalParams, focusParams;
    private Handler handler;
    private int currentPosition;
    private ViewPager viewPager;
    private int[] imageResources;

    public boolean isChinese() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language.endsWith("zh");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_traction);
        viewPager = findViewById(R.id.traction_viewPager_vp);
        linearLayout = findViewById(R.id.traction_dots_layout);
        fragmentList = new ArrayList<>();
        imageResources = isChinese() ? IMAGE_CN_RESOURCES : IMAGE_EN_RESOURCES;
        for (int imageResource : imageResources) {
            fragmentList.add(TractionFragment.newInstance(imageResource));
        }
        TractionAdapter tractionAdapter = new TractionAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(tractionAdapter);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        normalParams = new LinearLayout.LayoutParams(dip2px(getApplicationContext(), 8), dip2px(getApplicationContext(), 8));
        normalParams.leftMargin = dip2px(getApplicationContext(), 15);
        focusParams = new LinearLayout.LayoutParams(dip2px(getApplicationContext(), 48), dip2px(getApplicationContext(), 8));
        focusParams.leftMargin = dip2px(getApplicationContext(), 15);
        View dotView;
        for (int i = 0; i < imageResources.length; i++) {
            dotView = new View(this);
            if (i == 0) {
                dotView.setLayoutParams(focusParams);
                dotView.setBackgroundResource(R.drawable.icon_dot_focus);
            } else {
                dotView.setLayoutParams(normalParams);
                dotView.setBackgroundResource(R.drawable.icon_dot_normal);
            }
            linearLayout.addView(dotView);
        }
        linearLayout.setGravity(Gravity.CENTER);
        viewPager.addOnPageChangeListener(this);
        textView = findViewById(R.id.enter_button_tv);
        textView.setOnClickListener(this);
        initHandler();
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE, DELAY_MILLIS);
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message message) {
                if (message.what == 1) {
                    if (currentPosition == fragmentList.size() - 1) {
                        currentPosition = 0;
                        viewPager.setCurrentItem(0, false);
                    } else {
                        currentPosition++;
                        viewPager.setCurrentItem(currentPosition, true);
                    }
                    handler.sendEmptyMessageDelayed(HANDLER_MESSAGE, DELAY_MILLIS);
                } else {
                    super.handleMessage(message);
                }
            }
        };
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View dotView = linearLayout.getChildAt(i);
            if (i == position) {
                dotView.setLayoutParams(focusParams);
                dotView.setBackgroundResource(R.drawable.icon_dot_focus);
            } else {
                dotView.setLayoutParams(normalParams);
                dotView.setBackgroundResource(R.drawable.icon_dot_normal);
            }

        }
        textView.setVisibility(position == imageResources.length - 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onClick(View view) {
        IProfileProvider profileProvider = ARouter.getInstance().navigation(IProfileProvider.class);
        IMasterProvider masterProvider = ARouter.getInstance().navigation(IMasterProvider.class);
        masterProvider.setVersionCode();
        ARouter.getInstance()
                .build(profileProvider.isLogged() ? RouterConstants.ROUTER_NAVIGATE_NAVIGATE : RouterConstants.ROUTER_PROFILE_LOGIN)
                .navigation();
        finish();
    }
}
