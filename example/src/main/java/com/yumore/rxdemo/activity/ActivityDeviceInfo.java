package com.yumore.rxdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yumore.rxdemo.R;
import com.yumore.rxdemo.R2;
import com.yumore.rxui.activity.ActivityBase;
import com.yumore.rxui.tool.RxDeviceTool;
import com.yumore.rxui.view.RxTitle;

/**
 * @author yumore
 */
public class ActivityDeviceInfo extends ActivityBase {

    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R2.id.btn_get_phone_info)
    Button mBtnGetPhoneInfo;
    @BindView(R2.id.tv_device_is_phone)
    TextView mTvDeviceIsPhone;
    @BindView(R2.id.tv_device_software_phone_type)
    TextView mTvDeviceSoftwarePhoneType;
    @BindView(R2.id.tv_device_density)
    TextView mTvDeviceDensity;
    @BindView(R2.id.tv_device_manu_facturer)
    TextView mTvDeviceManuFacturer;
    @BindView(R2.id.tv_device_width)
    TextView mTvDeviceWidth;
    @BindView(R2.id.tv_device_height)
    TextView mTvDeviceHeight;
    @BindView(R2.id.tv_device_version_name)
    TextView mTvDeviceVersionName;
    @BindView(R2.id.tv_device_version_code)
    TextView mTvDeviceVersionCode;
    @BindView(R2.id.tv_device_imei)
    TextView mTvDeviceImei;
    @BindView(R2.id.tv_device_imsi)
    TextView mTvDeviceImsi;
    @BindView(R2.id.tv_device_software_version)
    TextView mTvDeviceSoftwareVersion;
    @BindView(R2.id.tv_device_mac)
    TextView mTvDeviceMac;
    @BindView(R2.id.tv_device_software_mcc_mnc)
    TextView mTvDeviceSoftwareMccMnc;
    @BindView(R2.id.tv_device_software_mcc_mnc_name)
    TextView mTvDeviceSoftwareMccMncName;
    @BindView(R2.id.tv_device_software_sim_country_iso)
    TextView mTvDeviceSoftwareSimCountryIso;
    @BindView(R2.id.tv_device_sim_operator)
    TextView mTvDeviceSimOperator;
    @BindView(R2.id.tv_device_sim_serial_number)
    TextView mTvDeviceSimSerialNumber;
    @BindView(R2.id.tv_device_sim_state)
    TextView mTvDeviceSimState;
    @BindView(R2.id.tv_device_sim_operator_name)
    TextView mTvDeviceSimOperatorName;
    @BindView(R2.id.tv_device_subscriber_id)
    TextView mTvDeviceSubscriberId;
    @BindView(R2.id.tv_device_voice_mail_number)
    TextView mTvDeviceVoiceMailNumber;
    @BindView(R2.id.tv_device_adnroid_id)
    TextView mTvDeviceAdnroidId;
    @BindView(R2.id.tv_device_build_brand_model)
    TextView mTvDeviceBuildBrandModel;
    @BindView(R2.id.tv_device_build_manu_facturer)
    TextView mTvDeviceBuildManuFacturer;
    @BindView(R2.id.tv_device_build_brand)
    TextView mTvDeviceBuildBrand;
    @BindView(R2.id.tv_device_serial_number)
    TextView mTvDeviceSerialNumber;
    @BindView(R2.id.tv_device_iso)
    TextView mTvDeviceIso;
    @BindView(R2.id.tv_device_phone)
    TextView mTvDevicePhone;
    @BindView(R2.id.ll_info_root)
    LinearLayout mLlInfoRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);
    }

    private void getPhoneInfo() {
        if (RxDeviceTool.isPhone(mContext)) {
            mTvDeviceIsPhone.setText("是");
        } else {
            mTvDeviceIsPhone.setText("否");
        }

        mTvDeviceSoftwarePhoneType.setText(RxDeviceTool.getPhoneType(mContext) + "");
        mTvDeviceDensity.setText(RxDeviceTool.getScreenDensity(mContext) + "");
        mTvDeviceManuFacturer.setText(RxDeviceTool.getUniqueSerialNumber() + "");
        mTvDeviceWidth.setText(RxDeviceTool.getScreenWidth(mContext) + " ");
        mTvDeviceHeight.setText(RxDeviceTool.getScreenHeight(mContext) + " ");
        mTvDeviceVersionName.setText(RxDeviceTool.getAppVersionName(mContext) + "");
        mTvDeviceVersionCode.setText(RxDeviceTool.getAppVersionNo(mContext) + "");
        mTvDeviceImei.setText(RxDeviceTool.getDeviceIdIMEI(mContext) + "");
        mTvDeviceImsi.setText(RxDeviceTool.getIMSI(mContext) + "");
        mTvDeviceSoftwareVersion.setText(RxDeviceTool.getDeviceSoftwareVersion(mContext) + "");
        mTvDeviceMac.setText(RxDeviceTool.getMacAddress(mContext));
        mTvDeviceSoftwareMccMnc.setText(RxDeviceTool.getNetworkOperator(mContext) + "");
        mTvDeviceSoftwareMccMncName.setText(RxDeviceTool.getNetworkOperatorName(mContext) + "");
        mTvDeviceSoftwareSimCountryIso.setText(RxDeviceTool.getNetworkCountryIso(mContext) + "");
        mTvDeviceSimOperator.setText(RxDeviceTool.getSimOperator(mContext) + "");
        mTvDeviceSimSerialNumber.setText(RxDeviceTool.getSimSerialNumber(mContext) + "");
        mTvDeviceSimState.setText(RxDeviceTool.getSimState(mContext) + "");
        mTvDeviceSimOperatorName.setText(RxDeviceTool.getSimOperatorName(mContext) + "");
        mTvDeviceSubscriberId.setText(RxDeviceTool.getSubscriberId(mContext) + "");
        mTvDeviceVoiceMailNumber.setText(RxDeviceTool.getVoiceMailNumber(mContext) + "");
        mTvDeviceAdnroidId.setText(RxDeviceTool.getAndroidId(mContext) + "");
        mTvDeviceBuildBrandModel.setText(RxDeviceTool.getBuildBrandModel() + "");
        mTvDeviceBuildManuFacturer.setText(RxDeviceTool.getBuildMANUFACTURER() + "");
        mTvDeviceBuildBrand.setText(RxDeviceTool.getBuildBrand() + "");
        mTvDeviceSerialNumber.setText(RxDeviceTool.getSerialNumber() + "");
        mTvDeviceIso.setText(RxDeviceTool.getNetworkCountryIso(mContext) + "");
        mTvDevicePhone.setText(RxDeviceTool.getLine1Number(mContext) + "");
    }

    @OnClick(R2.id.btn_get_phone_info)
    public void onViewClicked() {
        mLlInfoRoot.setVisibility(View.VISIBLE);
        getPhoneInfo();
        mBtnGetPhoneInfo.setVisibility(View.GONE);
    }
}
