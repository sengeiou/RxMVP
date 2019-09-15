package com.yumore.traction;


import androidx.viewpager.widget.ViewPager;

/**
 * A PageIndicator is responsible to show an visual indicator on the total views
 * number and the current visible view.
 */
public interface PageIndicator extends ViewPager.OnPageChangeListener {

    /**
     * Notify the indicator that the fragment list has changed.
     */
    void notifyDataSetChanged();

    /**
     * <p>
     * Set the current page of both the ViewPager and indicator.
     * </p>
     *
     * <p>
     * This <strong>must</strong> be used if you need to set the page before the
     * views are drawn on screen (e.g., default start page).
     * </p>
     *
     * @param item
     */
    void setCurrentItem(int item);

    /**
     * Set a page change listener which will receive forwarded events.
     *
     * @param listener
     */
    void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);

    void setPagingEnabled(boolean allow_change);

    /**
     * Bind the indicator to a ViewPager.
     *
     * @param pager
     */
    void setViewPager(ExtendedViewPager pager);

    /**
     * Bind the indicator to a ViewPager.
     *
     * @param pager
     * @param initialPosition
     */
    void setViewPager(ExtendedViewPager pager, int initialPosition);
}