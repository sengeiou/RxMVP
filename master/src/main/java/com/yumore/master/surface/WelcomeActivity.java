package com.yumore.master.surface;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.yumore.common.utility.PreferencesUtils;
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
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(() -> {
            Class<?> clazz = PreferencesUtils.getInstance(getApplicationContext()).getTractionEnable() ? IntroduceActivity.class : TractionActivity.class;
            Intent intent = new Intent(getApplicationContext(), clazz);
            startActivity(intent);
            finish();
        }, 2000);
    }
}