package com.yumore.example.surface;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.ActivityBase;
import com.yumore.utility.utility.RxActivityTool;
import com.yumore.utility.widget.RxTitle;

/**
 * @author yumore
 */
public class ActivitySeat extends ActivityBase {

    @BindView(R2.id.btn_movie)
    Button mBtnMovie;
    @BindView(R2.id.btn_flight)
    Button mBtnFlight;
    @BindView(R2.id.activity_seat)
    LinearLayout mActivitySeat;
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);
    }

    @OnClick({R2.id.btn_movie, R2.id.btn_flight})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_movie) {
            RxActivityTool.skipActivity(ActivitySeat.this, ActivityMovieSeat.class);
        } else if (id == R.id.btn_flight) {
            RxActivityTool.skipActivity(ActivitySeat.this, ActivityFlightSeat.class);
        }
    }
}