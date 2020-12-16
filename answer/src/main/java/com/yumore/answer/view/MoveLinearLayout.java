package com.yumore.answer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by zhangyipeng on 16/7/5.
 */
public class MoveLinearLayout extends LinearLayout {


    private Scroller mScroller;

    public MoveLinearLayout(Context context) {
        this(context, null);
    }

    public MoveLinearLayout(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public MoveLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setScroller(Scroller scroller) {
        this.mScroller = scroller;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller == null) {
            return;
        }

        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
        }
        invalidate();//必须要调用

    }
}
