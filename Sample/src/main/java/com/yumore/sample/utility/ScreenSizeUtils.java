package com.yumore.sample.utility;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

/**
 * TODO 单例模式 获取屏幕宽高的帮助类
 */

public class ScreenSizeUtils {

    private static ScreenSizeUtils instance = null;
    private WindowManager manager;
    private DisplayMetrics dm;
    private int screenWidth, screenHeigth;
    private WeakReference<Context> contextWeakReference;//软引用context


    private ScreenSizeUtils(Context mContext) {
        contextWeakReference = new WeakReference<>(mContext);
        Context context = contextWeakReference.get();
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        screenHeigth = dm.heightPixels;

    }

    public static ScreenSizeUtils getInstance(Context mContext) {

        if (instance == null) {
            synchronized (ScreenSizeUtils.class) {
                if (instance == null)
                    instance = new ScreenSizeUtils(mContext);
            }
        }
        return instance;
    }

    //获取屏幕宽度
    public int getScreenWidth() {
        return screenWidth;
    }

    //获取屏幕高度
    public int getScreenHeight() {
        return screenHeigth;
    }
}
