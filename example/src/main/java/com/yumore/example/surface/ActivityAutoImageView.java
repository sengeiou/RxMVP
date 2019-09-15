package com.yumore.example.surface;

import android.os.Bundle;
import com.yumore.example.R;
import com.yumore.utility.activity.ActivityBase;
import com.yumore.utility.utility.RxBarTool;

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
