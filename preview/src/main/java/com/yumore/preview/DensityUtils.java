package com.yumore.preview;

import android.content.Context;

/**
 * 作者： 巴掌 on 16/8/23 21:58
 * Github: https://github.com/JeasonWong
 */
public class DensityUtils {

    private DensityUtils() {
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
