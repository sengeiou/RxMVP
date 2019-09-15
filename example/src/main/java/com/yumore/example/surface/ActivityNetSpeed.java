package com.yumore.example.surface;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.ActivityBase;
import com.yumore.utility.widget.RxNetSpeedView;
import com.yumore.utility.widget.RxTitle;

/**
 * @author yumore
 */
public class ActivityNetSpeed extends ActivityBase {

    @BindView(R2.id.button2)
    Button mButton2;
    @BindView(R2.id.button3)
    Button mButton3;
    @BindView(R2.id.activity_net_speed)
    RelativeLayout mActivityNetSpeed;
    @BindView(R2.id.rx_net_speed_view)
    RxNetSpeedView mRxNetSpeedView;
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_speed);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);
    }

    @OnClick({R2.id.button2, R2.id.button3})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button2) {
            mRxNetSpeedView.setMulti(false);
        } else if (id == R.id.button3) {
            mRxNetSpeedView.setMulti(true);
        }
    }
}
