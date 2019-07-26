package com.vondear.rxui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.FragmentActivity;
import com.vondear.rxui.tool.RxActivityTool;

/**
 * @author vondear
 */
public class ActivityBase extends FragmentActivity {

    public ActivityBase mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        RxActivityTool.addActivity(this);
//        DragAndDropPermissionsCompat
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
