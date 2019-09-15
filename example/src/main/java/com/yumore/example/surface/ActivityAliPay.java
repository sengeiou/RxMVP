package com.yumore.example.surface;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.example.common.SelfInfo;
import com.yumore.rxfeature.module.alipay.AliPayModel;
import com.yumore.rxfeature.module.alipay.AliPayTool;
import com.yumore.utility.activity.ActivityBase;
import com.yumore.utility.callback.OnSuccessAndErrorListener;
import com.yumore.utility.utility.RxTimeTool;
import com.yumore.utility.widget.RxToast;

public class ActivityAliPay extends ActivityBase {

    @BindView(R2.id.iv_alipay)
    ImageView mIvAlipay;
    @BindView(R2.id.payV2)
    Button mPayV2;
    @BindView(R2.id.authV2)
    Button mAuthV2;
    @BindView(R2.id.h5pay)
    Button mH5pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ali_pay);
        ButterKnife.bind(this);
    }

    @OnClick({R2.id.payV2, R2.id.authV2, R2.id.h5pay})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.payV2) {
            //需要填写APPID 与 私钥
            AliPayTool.aliPay(mContext, SelfInfo.ALIPAY_APPID, true, SelfInfo.ALIPAY_RSA2_PRIVATE, new AliPayModel(RxTimeTool.date2String(RxTimeTool.getCurTimeDate()), "0.01", "爱心", "一份爱心"), new OnSuccessAndErrorListener() {
                @Override
                public void onSuccess(String s) {
                    RxToast.success("支付成功");
                }

                @Override
                public void onError(String s) {
                    RxToast.error("支付失败" + s);
                }
            });
        } else if (id == R.id.authV2) {
        } else if (id == R.id.h5pay) {
        }
    }
}
