package com.yumore.sample.common;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yumore.frame.helper.InitializeHelper;
import com.yumore.sample.BuildConfig;

/**
 * @author Nathaniel
 * @date 2018/7/1-14:06
 */
public class SampleApplication extends Application implements InitializeHelper {

    private Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initialize(application);
    }

    @Override
    public void initialize(Context context) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(application);
    }
}
