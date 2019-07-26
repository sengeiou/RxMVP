package com.yumore.common.utility;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

/**
 * @author Nathaniel
 * @date 2019/3/26 - 11:51
 */
public class TabLayoutUtils {
    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip, int bottomDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }
        tabStrip.setAccessible(true);
        LinearLayout linearLayout = null;
        try {
            linearLayout = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
        int bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bottomDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            layoutParams.leftMargin = left;
            layoutParams.rightMargin = right;
            layoutParams.bottomMargin = bottom;
            child.setLayoutParams(layoutParams);
            child.invalidate();
        }
    }
}
