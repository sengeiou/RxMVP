package com.yumore.master.common;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yumore.common.basic.BaseApplication;
import com.yumore.common.helper.InitializeHelper;
import com.yumore.master.BuildConfig;

/**
 * @author Nathaniel
 * @date 2018/7/1-14:06
 */
public class SampleApplication extends BaseApplication implements InitializeHelper {

    @Override
    public void initialize(Context context) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(application);
    }
}
