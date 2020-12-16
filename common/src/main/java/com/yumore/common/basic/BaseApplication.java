package com.yumore.common.basic;

import android.app.Application;
import android.content.Context;

import com.yumore.common.helper.InitializeHelper;

/**
 * @author Nathaniel
 * @date 18-5-9-上午11:35
 */
public abstract class BaseApplication extends Application implements InitializeHelper {
    protected Application application;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initialize(application);
    }
}
