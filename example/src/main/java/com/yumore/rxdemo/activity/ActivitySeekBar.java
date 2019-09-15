package com.yumore.rxdemo.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.yumore.rxdemo.R;
import com.yumore.rxdemo.R2;
import com.yumore.rxui.activity.ActivityBase;
import com.yumore.rxui.view.RxSeekBar;
import com.yumore.rxui.view.RxTitle;

import java.text.DecimalFormat;


/**
 * @author yumore
 */
public class ActivitySeekBar extends ActivityBase {

    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R2.id.seekbar1)
    RxSeekBar mSeekbar1;
    @BindView(R2.id.progress2_tv)
    TextView mProgress2Tv;
    @BindView(R2.id.seekbar2)
    RxSeekBar mSeekbar2;
    @BindView(R2.id.seekbar3)
    RxSeekBar mSeekbar3;
    @BindView(R2.id.seekbar4)
    RxSeekBar mSeekbar4;
    @BindView(R2.id.activity_main)
    LinearLayout mActivityMain;

    private DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_bar);
        ButterKnife.bind(this);

        mRxTitle.setLeftFinish(mContext);

        mSeekbar1.setValue(10);
        mSeekbar2.setValue(-0.5f, 0.8f);

        mSeekbar1.setOnRangeChangedListener(new RxSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RxSeekBar view, float min, float max, boolean isFromUser) {
                mSeekbar1.setProgressDescription((int) min + "%");
            }
        });

        mSeekbar2.setOnRangeChangedListener(new RxSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RxSeekBar view, float min, float max, boolean isFromUser) {
                if (isFromUser) {
                    mProgress2Tv.setText(min + "-" + max);
                    mSeekbar2.setLeftProgressDescription(df.format(min));
                    mSeekbar2.setRightProgressDescription(df.format(max));
                }
            }
        });

    }
}
