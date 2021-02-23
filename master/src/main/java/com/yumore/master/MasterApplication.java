package com.yumore.master;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yumore.common.basic.BaseApplication;
import com.yumore.common.helper.InitializeHelper;
import com.yumore.example.ExampleApplication;

/**
 * @author Nathaniel
 * @date 2018/7/1-14:06
 */
public class MasterApplication extends BaseApplication implements InitializeHelper {

    @Override
    public void initialize(Context context) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(application);
        ExampleApplication.getInstance().initialize(context);
    }
}
