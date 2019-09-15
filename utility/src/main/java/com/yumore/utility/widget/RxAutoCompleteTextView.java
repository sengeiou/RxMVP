package com.yumore.utility.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author yumore
 * @date 2018/2/2
 * <p>
 * 解决没有输入内容时也能展示筛选内容的需求
 */

public class RxAutoCompleteTextView extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    public RxAutoCompleteTextView(Context context) {
        super(context);
    }

    public RxAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }
}
