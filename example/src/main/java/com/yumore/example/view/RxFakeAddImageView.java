package com.yumore.example.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;

/**
 * @author yumore
 */
public class RxFakeAddImageView extends androidx.appcompat.widget.AppCompatImageView {
    private PointF mPointF;

    public RxFakeAddImageView(Context context) {
        super(context);
    }

    public RxFakeAddImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxFakeAddImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMPointF(PointF pointF) {
        setX(pointF.x);
        setY(pointF.y);
    }

    public PointF getmPointF() {
        return mPointF;
    }

    public void setmPointF(PointF mPointF) {
        setX(mPointF.x);
        setY(mPointF.y);
    }
}
