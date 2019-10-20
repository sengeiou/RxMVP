package com.yumore.gallery.utility;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 用于监听导航栏的显示和隐藏
 * 主要用于适配华为EMUI系统上虚拟导航栏可随时收起和展开的情况
 *
 * @author Nathaniel
 */
public class NavigationBarChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {
    /**
     * 监听竖屏模式导航栏的显示和隐藏
     */
    public static final int ORIENTATION_VERTICAL = 1;
    /**
     * 监听横屏模式导航栏的显示和隐藏
     */
    public static final int ORIENTATION_HORIZONTAL = 2;

    private Rect rect;
    private View rootView;
    private boolean showNavigationBar = false;
    private int orientation;
    private OnSoftInputStateChangeListener onSoftInputStateChangeListener;

    public NavigationBarChangeListener(View rootView, int orientation) {
        this.rootView = rootView;
        this.orientation = orientation;
        rect = new Rect();
    }

    public static NavigationBarChangeListener with(View rootView) {
        return with(rootView, ORIENTATION_VERTICAL);
    }

    public static NavigationBarChangeListener with(AppCompatActivity activity) {
        return with(activity.findViewById(android.R.id.content), ORIENTATION_VERTICAL);
    }

    public static NavigationBarChangeListener with(View rootView, int orientation) {
        NavigationBarChangeListener changeListener = new NavigationBarChangeListener(rootView, orientation);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(changeListener);
        return changeListener;
    }

    public static NavigationBarChangeListener with(AppCompatActivity activity, int orientation) {
        return with(activity.findViewById(android.R.id.content), orientation);
    }

    @Override
    public void onGlobalLayout() {
        rect.setEmpty();
        rootView.getWindowVisibleDisplayFrame(rect);
        int heightDiff = 0;
        if (orientation == ORIENTATION_VERTICAL) {
            heightDiff = rootView.getHeight() - (rect.bottom - rect.top);
        } else if (orientation == ORIENTATION_HORIZONTAL) {
            heightDiff = rootView.getWidth() - (rect.right - rect.left);
        }
        int navigationBarHeight = ScreenUtils.hasVirtualNavigationBar(rootView.getContext()) ? ScreenUtils.getNavigationBarHeight(rootView.getContext()) : 0;
        if (heightDiff >= navigationBarHeight && heightDiff < navigationBarHeight * 2) {
            if (!showNavigationBar && onSoftInputStateChangeListener != null) {
                onSoftInputStateChangeListener.onNavigationBarShow(orientation, heightDiff);
            }
            showNavigationBar = true;
        } else {
            if (showNavigationBar && onSoftInputStateChangeListener != null) {
                onSoftInputStateChangeListener.onNavigationBarHide(orientation);
            }
            showNavigationBar = false;
        }
    }

    public void setOnSoftInputStateChangeListener(OnSoftInputStateChangeListener onSoftInputStateChangeListener) {
        this.onSoftInputStateChangeListener = onSoftInputStateChangeListener;
    }

    public interface OnSoftInputStateChangeListener {
        /**
         * on navigation bar show
         *
         * @param orientation orientation
         * @param height      height
         */
        void onNavigationBarShow(int orientation, int height);

        /**
         * on navigation bar hide
         *
         * @param orientation orientation
         */
        void onNavigationBarHide(int orientation);
    }
}
