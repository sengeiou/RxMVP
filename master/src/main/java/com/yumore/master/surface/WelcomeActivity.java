package com.yumore.master.surface;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.yumore.common.basic.BasicActivity;
import com.yumore.common.utility.PreferencesUtils;
import com.yumore.example.surface.ExampleActivity;
import com.yumore.introduce.IntroduceActivity;
import com.yumore.master.R;
import com.yumore.traction.TractionActivity;

/**
 * RootActivity
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/8 - 14:57
 */
public class WelcomeActivity extends BasicActivity {
    private static final int DELAY_MILLS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        /*
         * TODO: 解决new Handler() 无参过时
         */
        new Handler(getMainLooper()).postDelayed(() -> {
            Class<?> clazz = PreferencesUtils.getInstance(getApplicationContext()).getTractionEnable() ?
                    PreferencesUtils.getInstance(getApplicationContext()).getTractionEnable() ?
                            ExampleActivity.class : TractionActivity.class : IntroduceActivity.class;
            Intent intent = new Intent(getApplicationContext(), clazz);
            startActivity(intent);
            finish();
        }, DELAY_MILLS);
    }
}