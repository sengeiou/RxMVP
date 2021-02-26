package com.yumore.example.surface;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.feature.tool.RxBarCode;
import com.yumore.feature.tool.RxQRCode;
import com.yumore.utility.activity.BaseActivity;
import com.yumore.utility.utility.RxBarTool;
import com.yumore.utility.widget.RxTitle;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author yumore
 */
public class ActivityCreateQRCode extends BaseActivity implements View.OnClickListener {

    private static final android.os.Handler HANDLER = new Handler(Looper.getMainLooper());
    private static Runnable mRunnable = null;
    private final int time_second = 0;
    private final int second = 60;
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R2.id.iv_linecode)
    ImageView mIvLinecode;
    @BindView(R2.id.iv_code)
    ImageView mIvCode;
    @BindView(R2.id.imageView1)
    ImageView mImageView1;
    @BindView(R2.id.textView2)
    TextView mTextView2;
    @BindView(R2.id.tv_time_second)
    TextView mTvTimeSecond;
    @BindView(R2.id.ll_refresh)
    LinearLayout mLlRefresh;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 60000) {
                initData();
            } else {
                super.handleMessage(msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(baseActivity);
        setContentView(R.layout.activity_create_qrcode);
        RxBarTool.setTransparentStatusBar(baseActivity);
        ButterKnife.bind(this);
        initView();
        initData();
        AuthCode(mTvTimeSecond, second);
    }

    private void AuthCode(final TextView view, final int timeSecond) {

        mRunnable = new Runnable() {
            int mSumNum = timeSecond;

            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                HANDLER.postDelayed(mRunnable, 1000);
                view.setText(String.format("%d", mSumNum));
                view.setEnabled(false);
                mSumNum--;
                if (mSumNum < 0) {
                    view.setText(String.format("0"));
                    view.setEnabled(true);
                    Message message = new Message();
                    message.what = 60000;
                    mHandler.sendMessage(message);
                    // 干掉这个定时器，下次减不会累加
                    HANDLER.removeCallbacks(mRunnable);
                    AuthCode(mTvTimeSecond, second);
                }
            }

        };
        HANDLER.postDelayed(mRunnable, 1000);
    }

    private void initView() {
        mRxTitle.setLeftFinish(baseActivity);
        mRxTitle.setTitle("动态生成码");
        mLlRefresh.setOnClickListener(this);
    }

    private void initData() {
        // TODO Auto-generated method stub
        RxQRCode.createQRCode("时间戳:" + System.currentTimeMillis(), 800, 800, mIvCode);
        mIvLinecode.setImageBitmap(RxBarCode.createBarCode("" + System.currentTimeMillis(), 1000, 300));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View arg0) {
        if (arg0.getId() == R.id.ll_refresh) {
            HANDLER.removeCallbacks(mRunnable);
            initData();
            mTvTimeSecond.setText(String.format("%d", second));
            AuthCode(mTvTimeSecond, second);
        }
    }
}
