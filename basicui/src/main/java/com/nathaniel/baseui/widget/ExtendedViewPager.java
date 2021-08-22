package com.nathaniel.baseui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * @author nathaniel
 */
public class ExtendedViewPager extends ViewPager {

    private boolean pagingEnabled;

    public ExtendedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        pagingEnabled = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!pagingEnabled) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!pagingEnabled) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    public void setPagingEnabled(boolean enabled) {
        pagingEnabled = enabled;
    }
}