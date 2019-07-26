package com.vondear.rxdemo.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.vondear.rxdemo.R;
import com.vondear.rxui.activity.ActivityBase;
import com.vondear.rxui.tool.RxActivityTool;
import com.vondear.rxui.tool.RxBarTool;
import com.vondear.rxui.view.RxShoppingView;
import com.vondear.rxui.view.RxTitle;

/**
 * @author vondear
 */
public class ActivityShoppingView extends ActivityBase {

    @BindView(R.id.sv_1)
    RxShoppingView mSv1;
    @BindView(R.id.sv_2)
    RxShoppingView mSv2;
    @BindView(R.id.btn_take_out)
    Button mBtnTakeOut;
    @BindView(R.id.activity_shopping_view)
    LinearLayout mActivityShoppingView;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        setContentView(R.layout.activity_shopping_view);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);

    }

    @OnClick(R.id.btn_take_out)
    public void onClick() {
        RxActivityTool.skipActivity(mContext, ActivityELMe.class);
    }
}
