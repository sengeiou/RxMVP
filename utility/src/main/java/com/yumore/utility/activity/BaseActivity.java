package com.yumore.utility.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.yumore.utility.utility.RxActivityTool;

/**
 * @author yumore
 */
public class BaseActivity extends AppCompatActivity {

    public BaseActivity baseActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this;
        RxActivityTool.addActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
