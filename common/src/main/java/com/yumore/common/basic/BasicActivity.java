package com.yumore.common.basic;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yumore.common.manager.ActivityManager;

/**
 * @author nathaniel
 * @version V1.0.0
 * @package com.yumore.common.basic
 * @datetime 12/17/20 - 3:07 PM
 */
public class BasicActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }
}
