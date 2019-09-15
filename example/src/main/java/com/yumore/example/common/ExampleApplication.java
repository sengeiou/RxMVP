package com.yumore.example;

import android.app.Application;
import android.content.Context;
import com.yumore.rxui.tool.RxTool;

/**
 * @author vonde
 * @date 2016/12/23
 */

public class ExampleApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);
    }

}
