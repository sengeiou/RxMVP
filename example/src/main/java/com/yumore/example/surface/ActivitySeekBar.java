package com.yumore.example.surface;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.BaseActivity;
import com.yumore.utility.widget.RxSeekBar;
import com.yumore.utility.widget.RxTitle;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author yumore
 */
public class ActivitySeekBar extends BaseActivity {

    private final DecimalFormat df = new DecimalFormat("0.00");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_bar);
        ButterKnife.bind(this);

        mRxTitle.setLeftFinish(baseActivity);

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
