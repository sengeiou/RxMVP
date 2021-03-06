package com.yumore.preview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： 巴掌 on 16/8/23 22:00
 * Github: https://github.com/JeasonWong
 */
public class PreviewIndicator extends LinearLayout {

    //指示器个数
    private final int INDICATOR_COUNT = 3;
    private final List<ImageView> mImageList = new ArrayList<>();

    public PreviewIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreviewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        for (int i = 0; i < INDICATOR_COUNT; i++) {
            ImageView imageView = new ImageView(getContext());
            if (i == 0) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.preivew_shape_circle_selected));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.preview_shape_circle_unselected));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(DensityUtils.dp2px(getContext(), 10), 0, DensityUtils.dp2px(getContext(), 10), 0);
            addView(imageView, params);
            mImageList.add(imageView);
        }
    }

    public void setSelected(int position) {
        for (int i = 0; i < mImageList.size(); i++) {
            ImageView imageView = mImageList.get(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.preivew_shape_circle_selected));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.preview_shape_circle_unselected));
            }
        }
    }

}
