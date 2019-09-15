package com.yumore.rxdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yumore.rxdemo.R;
import com.yumore.rxdemo.R2;
import com.yumore.rxui.activity.ActivityBase;
import com.yumore.rxui.tool.RxVibrateTool;
import com.yumore.rxui.view.RxTitle;

/**
 * @author yumore
 */
public class ActivityVibrate extends ActivityBase {


    @BindView(R2.id.btn_vibrate_once)
    Button mBtnVibrateOnce;
    @BindView(R2.id.btn_vibrate_Complicated)
    Button mBtnVibrateComplicated;
    @BindView(R2.id.btn_vibrate_stop)
    Button mBtnVibrateStop;
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;

    private long[] temp = {100, 10, 100, 1000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrate);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);
    }

    @OnClick({R2.id.btn_vibrate_once,
            R2.id.btn_vibrate_Complicated,
            R2.id.btn_vibrate_stop})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_vibrate_once) {
            RxVibrateTool.vibrateOnce(this, 2000);
        } else if (id == R.id.btn_vibrate_Complicated) {
            RxVibrateTool.vibrateComplicated(this, temp, 0);
        } else if (id == R.id.btn_vibrate_stop) {
            RxVibrateTool.vibrateStop();
        }
    }
}
