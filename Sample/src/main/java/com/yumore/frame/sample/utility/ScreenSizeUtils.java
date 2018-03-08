package com.yumore.frame.sample.utility;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

/**
 * TODO 单例模式 获取屏幕宽高的帮助类
 */

public class ScreenSizeUtils {

    private static ScreenSizeUtils instance = null;
    private int screenWidth, screenHeight;


    private ScreenSizeUtils(Context mcontext) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(mcontext);
        Context context = contextWeakReference.get();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

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

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}
