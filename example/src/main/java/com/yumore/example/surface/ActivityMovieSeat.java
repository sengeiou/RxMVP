package com.yumore.example.surface;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.BaseActivity;
import com.yumore.utility.utility.RxBarTool;
import com.yumore.utility.widget.RxSeatMovie;
import com.yumore.utility.widget.RxTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yumore
 */
public class ActivityMovieSeat extends BaseActivity {

    @BindView(R2.id.seatView)
    RxSeatMovie mSeatView;
    @BindView(R2.id.activity_movie_seat)
    LinearLayout mActivityMovieSeat;
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        RxBarTool.setTransparentStatusBar(this);
        setContentView(R.layout.activity_movie_seat);
        ButterKnife.bind(this);
        initView();
    }


    protected void initView() {
        mRxTitle.setLeftFinish(baseActivity);

        mSeatView.setScreenName("3号厅荧幕");//设置屏幕名称
        mSeatView.setMaxSelected(8);//设置最多选中

        mSeatView.setSeatChecker(new RxSeatMovie.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {
                return !(column == 2 || column == 12);
            }

            @Override
            public boolean isSold(int row, int column) {
                return row == 6 && column == 6;
            }

            @Override
            public void checked(int row, int column) {

            }

            @Override
            public void unCheck(int row, int column) {

            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });
        mSeatView.setData(10, 15);
    }
}
