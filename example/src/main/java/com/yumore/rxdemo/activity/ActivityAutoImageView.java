package com.yumore.rxdemo.activity;

import android.os.Bundle;
import com.yumore.rxdemo.R;
import com.yumore.rxui.activity.ActivityBase;
import com.yumore.rxui.tool.RxBarTool;

/**
 * @author yumore
 */
public class ActivityAutoImageView extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        RxBarTool.setTransparentStatusBar(this);
        setContentView(R.layout.activity_auto_image_view);
    }
}
