package com.yumore.example.surface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.rxui.activity.ActivityBase;
import com.yumore.rxui.tool.RxBarTool;
import com.yumore.rxui.view.RxTitle;
import com.yumore.rxui.view.dialog.*;

/**
 * @author yumore
 */
public class ActivityDialog extends ActivityBase {

    @BindView(R2.id.button_tran)
    Button mButtonTran;
    @BindView(R2.id.button_DialogSure)
    Button mButtonDialogSure;
    @BindView(R2.id.button_DialogSureCancle)
    Button mButtonDialogSureCancle;
    @BindView(R2.id.button_DialogEditTextSureCancle)
    Button mButtonDialogEditTextSureCancle;
    @BindView(R2.id.button_DialogWheelYearMonthDay)
    Button mButtonDialogWheelYearMonthDay;
    @BindView(R2.id.button_DialogShapeLoading)
    Button mButtonDialogShapeLoading;
    @BindView(R2.id.button_DialogLoadingProgressAcfunVideo)
    Button mButtonDialogLoadingProgressAcfunVideo;
    @BindView(R2.id.activity_dialog)
    LinearLayout mActivityDialog;
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R2.id.button_DialogLoadingspinkit)
    Button mButtonDialogLoadingspinkit;
    @BindView(R2.id.button_DialogScaleView)
    Button mButtonDialogScaleView;
    private RxDialogWheelYearMonthDay mRxDialogWheelYearMonthDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        setContentView(R.layout.activity_dialog);
        RxBarTool.setTransparentStatusBar(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mRxTitle.setLeftFinish(mContext);
    }

    private void initWheelYearMonthDayDialog() {
        // ------------------------------------------------------------------选择日期开始
        mRxDialogWheelYearMonthDay = new RxDialogWheelYearMonthDay(this, 1994, 2018);
        mRxDialogWheelYearMonthDay.getSureView().setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (mRxDialogWheelYearMonthDay.getCheckBoxDay().isChecked()) {
                            mButtonDialogWheelYearMonthDay.setText(
                                    mRxDialogWheelYearMonthDay.getSelectorYear() + "年"
                                            + mRxDialogWheelYearMonthDay.getSelectorMonth() + "月"
                                            + mRxDialogWheelYearMonthDay.getSelectorDay() + "日");
                        } else {
                            mButtonDialogWheelYearMonthDay.setText(
                                    mRxDialogWheelYearMonthDay.getSelectorYear() + "年"
                                            + mRxDialogWheelYearMonthDay.getSelectorMonth() + "月");
                        }
                        mRxDialogWheelYearMonthDay.cancel();
                    }
                });
        mRxDialogWheelYearMonthDay.getCancleView().setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        mRxDialogWheelYearMonthDay.cancel();
                    }
                });
        // ------------------------------------------------------------------选择日期结束
    }

    @OnClick({
            R2.id.button_DialogScaleView,
            R2.id.button_DialogLoadingspinkit,
            R2.id.button_tran,
            R2.id.button_DialogSure,
            R2.id.button_DialogSureCancle,
            R2.id.button_DialogEditTextSureCancle,
            R2.id.button_DialogWheelYearMonthDay,
            R2.id.button_DialogShapeLoading,
            R2.id.button_DialogLoadingProgressAcfunVideo})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_tran) {
            RxDialog rxDialog = new RxDialog(mContext, R.style.tran_dialog);
            View view1 = LayoutInflater.from(mContext).inflate(R.layout.image, null);
            ImageView pageItem = view1.findViewById(R2.id.page_item);
            pageItem.setImageResource(R.drawable.coin);
            rxDialog.setContentView(view1);
            rxDialog.show();
        } else if (id == R.id.button_DialogSure) {
            //提示弹窗
            final RxDialogSure rxDialogSure = new RxDialogSure(mContext);
            rxDialogSure.getLogoView().setImageResource(R.drawable.logo);
            rxDialogSure.getSureView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rxDialogSure.cancel();
                }
            });
            rxDialogSure.show();
        } else if (id == R.id.button_DialogSureCancle) {
            //提示弹窗
            final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(mContext);
            rxDialogSureCancel.getTitleView().setBackgroundResource(R.drawable.logo);
            rxDialogSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rxDialogSureCancel.cancel();
                }
            });
            rxDialogSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rxDialogSureCancel.cancel();
                }
            });
            rxDialogSureCancel.show();
        } else if (id == R.id.button_DialogEditTextSureCancle) {
            //提示弹窗
            final RxDialogEditSureCancel rxDialogEditSureCancel = new RxDialogEditSureCancel(mContext);
            rxDialogEditSureCancel.getTitleView().setBackgroundResource(R.drawable.logo);
            rxDialogEditSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rxDialogEditSureCancel.cancel();
                }
            });
            rxDialogEditSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rxDialogEditSureCancel.cancel();
                }
            });
            rxDialogEditSureCancel.show();
        } else if (id == R.id.button_DialogWheelYearMonthDay) {
            if (mRxDialogWheelYearMonthDay == null) {
                initWheelYearMonthDayDialog();
            }
            mRxDialogWheelYearMonthDay.show();
        } else if (id == R.id.button_DialogShapeLoading) {
            RxDialogShapeLoading rxDialogShapeLoading = new RxDialogShapeLoading(this);
            rxDialogShapeLoading.show();
        } else if (id == R.id.button_DialogLoadingProgressAcfunVideo) {
            new RxDialogAcfunVideoLoading(this).show();
        } else if (id == R.id.button_DialogLoadingspinkit) {
            RxDialogLoading rxDialogLoading = new RxDialogLoading(mContext);
            rxDialogLoading.show();
            //rxDialogLoading.cancel(RxDialogLoading.RxCancelType.error,"");
        } else if (id == R.id.button_DialogScaleView) {
            RxDialogScaleView rxDialogScaleView = new RxDialogScaleView(mContext);
            rxDialogScaleView.setImage("squirrel.jpg", true);
            rxDialogScaleView.show();
        }
    }
}
