package com.yumore.feature.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.widget.NestedScrollView;

import com.yumore.feature.R;
import com.yumore.feature.tool.RxBarCode;
import com.yumore.feature.tool.RxQRCode;
import com.yumore.utility.activity.BaseActivity;
import com.yumore.utility.utility.RxActivityTool;
import com.yumore.utility.utility.RxBarTool;
import com.yumore.utility.utility.RxDataTool;
import com.yumore.utility.utility.RxImageTool;
import com.yumore.utility.utility.RxSPTool;
import com.yumore.utility.widget.RxTitle;
import com.yumore.utility.widget.RxToast;
import com.yumore.utility.widget.ticker.RxTickerUtils;
import com.yumore.utility.widget.ticker.RxTickerView;

import static com.yumore.utility.utility.RxConstants.SP_MADE_CODE;
import static com.yumore.utility.utility.RxConstants.SP_SCAN_CODE;

/**
 * @author yumore
 */
public class ActivityCodeTool extends BaseActivity {

    private static final char[] NUMBER_LIST = RxTickerUtils.getDefaultNumberList();
    private RxTitle mRxTitle;
    private EditText mEtQrCode;
    private ImageView mIvCreateQrCode;
    private ImageView mIvQrCode;
    private LinearLayout mActivityCodeTool;
    private LinearLayout mLlCode;
    private LinearLayout mLlQrRoot;
    private EditText mEtBarCode;
    private ImageView mIvCreateBarCode;
    private ImageView mIvBarCode;
    private LinearLayout mLlBarCode;
    private LinearLayout mLlBarRoot;
    private LinearLayout mLlScaner;
    private LinearLayout mLlQr;
    private LinearLayout mLlBar;
    private RxTickerView mRxTickerViewMade;
    private RxTickerView mRxTickerViewScan;
    private NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        RxBarTool.setTransparentStatusBar(this);
        setContentView(R.layout.activity_code_tool);
        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateScanCodeCount();
    }

    protected void initView() {
        mRxTitle = findViewById(R.id.rx_title);
        mEtQrCode = findViewById(R.id.et_qr_code);
        mIvCreateQrCode = findViewById(R.id.iv_create_qr_code);
        mIvQrCode = findViewById(R.id.iv_qr_code);
        mActivityCodeTool = findViewById(R.id.activity_code_tool);
        mLlCode = findViewById(R.id.ll_code);
        mLlQrRoot = findViewById(R.id.ll_qr_root);

        nestedScrollView = findViewById(R.id.nestedScrollView);

        mEtBarCode = findViewById(R.id.et_bar_code);
        mIvCreateBarCode = findViewById(R.id.iv_create_bar_code);
        mIvBarCode = findViewById(R.id.iv_bar_code);
        mLlBarCode = findViewById(R.id.ll_bar_code);
        mLlBarRoot = findViewById(R.id.ll_bar_root);
        mLlScaner = findViewById(R.id.ll_scaner);
        mLlQr = findViewById(R.id.ll_qr);
        mLlBar = findViewById(R.id.ll_bar);

        mRxTickerViewScan = findViewById(R.id.ticker_scan_count);
        mRxTickerViewScan.setCharacterList(NUMBER_LIST);

        mRxTickerViewMade = findViewById(R.id.ticker_made_count);
        mRxTickerViewMade.setCharacterList(NUMBER_LIST);
        updateMadeCodeCount();
    }

    private void updateScanCodeCount() {
        mRxTickerViewScan.setText(RxDataTool.stringToInt(RxSPTool.getContent(baseActivity, SP_SCAN_CODE)) + "", true);
    }

    private void updateMadeCodeCount() {
        mRxTickerViewMade.setText(RxDataTool.stringToInt(RxSPTool.getContent(baseActivity, SP_MADE_CODE)) + "", true);
    }

    private void initEvent() {
        mRxTitle.setLeftIconVisibility(true);
        mRxTitle.setTitleColor(Color.WHITE);
        mRxTitle.setTitleSize(RxImageTool.dp2px(20));
        mRxTitle.setLeftFinish(baseActivity);

        mRxTickerViewScan.setAnimationDuration(500);

        mIvCreateQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mEtQrCode.getText().toString();
                if (RxDataTool.isNullString(str)) {
                    RxToast.error("????????????????????????????????????");
                } else {
                    mLlCode.setVisibility(View.VISIBLE);

                    //????????????????????????  ???????????????
                    RxQRCode.builder(str).
                            backColor(0xFFFFFFFF).
                            codeColor(0xFF000000).
                            codeSide(600).
                            into(mIvQrCode);

                    //???????????????????????? ?????????????????????800 ??????????????? ??????????????????
                    // RxQRCode.createQRCode(str,mIvQrCode);

                    mIvQrCode.setVisibility(View.VISIBLE);

                    RxToast.success("??????????????????!");

                    RxSPTool.putContent(baseActivity, SP_MADE_CODE, RxDataTool.stringToInt(RxSPTool.getContent(baseActivity, SP_MADE_CODE)) + 1 + "");

                    updateMadeCodeCount();

                    nestedScrollView.computeScroll();
                }
            }
        });

        mIvCreateBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = mEtBarCode.getText().toString();
                if (RxDataTool.isNullString(str1)) {
                    RxToast.error("????????????????????????????????????");
                } else {
                    mLlBarCode.setVisibility(View.VISIBLE);

                    //????????????????????????  ???????????????
                    RxBarCode.builder(str1).
                            backColor(0x00000000).
                            codeColor(0xFF000000).
                            codeWidth(1000).
                            codeHeight(300).
                            into(mIvBarCode);

                    //????????????????????????  ????????????1000 ??????300 ??????????????? ??????????????????
                    //mIvBarCode.setImageBitmap(RxBarCode.createBarCode(str1, 1000, 300));

                    mIvBarCode.setVisibility(View.VISIBLE);

                    RxToast.success("??????????????????!");

                    RxSPTool.putContent(baseActivity, SP_MADE_CODE, RxDataTool.stringToInt(RxSPTool.getContent(baseActivity, SP_MADE_CODE)) + 1 + "");

                    updateMadeCodeCount();
                }
            }
        });

        mLlScaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.skipActivity(baseActivity, ActivityScanerCode.class);
            }
        });

        mLlQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlQrRoot.setVisibility(View.VISIBLE);
                mLlBarRoot.setVisibility(View.GONE);
            }
        });

        mLlBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlBarRoot.setVisibility(View.VISIBLE);
                mLlQrRoot.setVisibility(View.GONE);
            }
        });
    }
}
