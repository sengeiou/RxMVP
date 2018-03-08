package com.yumore.frame.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.yumore.frame.R;

/**
 * WelcomeActivity
 *
 * @author Nathaniel
 *         nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/8 - 14:57
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, TestActivity.class));
            }
        }, 2000);
    }
}
