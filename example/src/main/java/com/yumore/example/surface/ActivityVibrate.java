package com.yumore.example.surface;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.BaseActivity;
import com.yumore.utility.utility.RxVibrateTool;
import com.yumore.utility.widget.RxTitle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yumore
 */
public class ActivityVibrate extends BaseActivity {


    private final long[] temp = {100, 10, 100, 1000};
    @BindView(R2.id.btn_vibrate_once)
    Button mBtnVibrateOnce;
    @BindView(R2.id.btn_vibrate_Complicated)
    Button mBtnVibrateComplicated;
    @BindView(R2.id.btn_vibrate_stop)
    Button mBtnVibrateStop;
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrate);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(baseActivity);
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
