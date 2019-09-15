package com.yumore.traction;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

public class ExtendedViewPager extends ViewPager {

    private boolean mPagingEnabled = true;

    public ExtendedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPagingEnabled = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mPagingEnabled) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mPagingEnabled) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    public void setPagingEnabled(boolean enabled) {
        mPagingEnabled = enabled;
    }
}