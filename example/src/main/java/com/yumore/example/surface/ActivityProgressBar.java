package com.yumore.example.surface;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.BaseActivity;
import com.yumore.utility.utility.RxBarTool;
import com.yumore.utility.utility.RxDataTool;
import com.yumore.utility.widget.RxProgressBar;
import com.yumore.utility.widget.RxRoundProgress;
import com.yumore.utility.widget.RxTitle;
import com.yumore.utility.widget.roundprogressbar.RxIconRoundProgressBar;
import com.yumore.utility.widget.roundprogressbar.RxRoundProgressBar;
import com.yumore.utility.widget.roundprogressbar.RxTextRoundProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yumore
 */
public class ActivityProgressBar extends BaseActivity {


    private final int money1 = 10000;
    private final int mRxRoundPdMax = 100;
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R2.id.flikerbar)
    RxProgressBar mFlikerbar;
    @BindView(R2.id.round_flikerbar)
    RxProgressBar mRoundFlikerbar;
    @BindView(R2.id.rx_round_pd1)
    RxRoundProgressBar mRxRoundPd1;
    @BindView(R2.id.rx_round_pd2)
    RxRoundProgressBar mRxRoundPd2;
    @BindView(R2.id.rx_round_pd3)
    RxRoundProgressBar mRxRoundPd3;
    @BindView(R2.id.rx_round_pd4)
    RxRoundProgressBar mRxRoundPd4;
    @BindView(R2.id.rx_round_pd5)
    RxRoundProgressBar mRxRoundPd5;
    @BindView(R2.id.rx_round_pd6)
    RxRoundProgressBar mRxRoundPd6;
    @BindView(R2.id.rx_round_pd7)
    RxRoundProgressBar mRxRoundPd7;
    @BindView(R2.id.rx_round_pd8)
    RxIconRoundProgressBar mRxRoundPd8;
    @BindView(R2.id.rx_round_pd9)
    RxIconRoundProgressBar mRxRoundPd9;
    @BindView(R2.id.rx_round_pd10)
    RxIconRoundProgressBar mRxRoundPd10;
    @BindView(R2.id.rx_round_pd11)
    RxIconRoundProgressBar mRxRoundPd11;
    @BindView(R2.id.rx_round_pd12)
    RxIconRoundProgressBar mRxRoundPd12;
    @BindView(R2.id.rx_round_pd13)
    RxIconRoundProgressBar mRxRoundPd13;
    @BindView(R2.id.rx_round_pd14)
    RxIconRoundProgressBar mRxRoundPd14;
    @BindView(R2.id.rx_round_pd15)
    RxTextRoundProgressBar mRxRoundPd15;
    @BindView(R2.id.rx_round_pd16)
    RxTextRoundProgressBar mRxRoundPd16;
    @BindView(R2.id.rx_round_pd17)
    RxTextRoundProgressBar mRxRoundPd17;
    @BindView(R2.id.progress_one)
    RxIconRoundProgressBar mProgressOne;
    @BindView(R2.id.progress_two)
    RxRoundProgressBar mProgressTwo;
    @BindView(R2.id.progress_three)
    RxTextRoundProgressBar mProgressThree;
    @BindView(R2.id.textView)
    TextView mTextView;
    @BindView(R2.id.pb_line_of_credit)
    ProgressBar mPbLineOfCredit;
    @BindView(R2.id.textView5)
    TextView mTextView5;
    @BindView(R2.id.roundProgressBar1)
    RxRoundProgress mRxRoundProgress1;
    @BindView(R2.id.activity_round_progress_bar)
    LinearLayout mActivityRoundProgressBar;
    double money = 1000;
    Thread downLoadThread;
    Thread downLoadThread1;
    Thread downLoadThread2;
    Thread downLoadRxRoundPdThread;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mFlikerbar.setProgress(msg.arg1);
            mRoundFlikerbar.setProgress(msg.arg1);
            if (msg.arg1 == 100) {
                mFlikerbar.finishLoad();
                mRoundFlikerbar.finishLoad();
            }
        }
    };
    private double progress;
    private int progress1;
    private int mRxRoundProgress;
    Handler mRxRoundPdHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mRxRoundPd1.setProgress(mRxRoundProgress);
            mRxRoundPd2.setProgress(mRxRoundProgress);
            mRxRoundPd3.setProgress(mRxRoundProgress);
            mRxRoundPd4.setProgress(mRxRoundProgress);
            mRxRoundPd5.setProgress(mRxRoundProgress);
            mRxRoundPd6.setProgress(mRxRoundProgress);
            mRxRoundPd7.setProgress(mRxRoundProgress);
            mRxRoundPd8.setProgress(mRxRoundProgress);
            mRxRoundPd9.setProgress(mRxRoundProgress);
            mRxRoundPd10.setProgress(mRxRoundProgress);
            mRxRoundPd11.setProgress(mRxRoundProgress);
            mRxRoundPd12.setProgress(mRxRoundProgress);
            mRxRoundPd13.setProgress(mRxRoundProgress);
            mRxRoundPd14.setProgress(mRxRoundProgress);
            mRxRoundPd15.setProgress(mRxRoundProgress);
            mRxRoundPd16.setProgress(mRxRoundProgress);
            mRxRoundPd17.setProgress(mRxRoundProgress);
            mProgressOne.setProgress(mRxRoundProgress);
            mProgressTwo.setProgress(mRxRoundProgress);
            mProgressThree.setProgress(mRxRoundProgress);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        setContentView(R.layout.activity_progress_bar);
        ButterKnife.bind(this);
        initView();
        initRoundProgress();
        initLineProgress();
        initRxRoundPd();

        downLoad();
    }

    private void initView() {
        mRxTitle.setLeftFinish(baseActivity);
    }

    private int getMax(double currentProgress) {
        if (currentProgress < 100 && currentProgress > 0) {
            return 100;
        } else if (currentProgress >= 100 && currentProgress < 1000) {
            return 1000;
        } else if (currentProgress >= 1000 && currentProgress < 5000) {
            return 5000;
        } else if (currentProgress >= 5000 && currentProgress < 20000) {
            return 20000;
        } else if (currentProgress >= 20000 && currentProgress < 100000) {
            return 100000;
        } else if (currentProgress >= 100000) {
            return RxDataTool.stringToInt(currentProgress * 1.1 + "");
        } else {
            return RxDataTool.stringToInt(currentProgress + "");
        }
    }

    private void initRoundProgress() {
        // TODO Auto-generated method stub
        progress = 0;// ???????????????

        mRxRoundProgress1.setProgress(progress);
        mRxRoundProgress1.setMax(getMax(money));

        downLoadThread2 = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    while (!downLoadThread2.isInterrupted()) {
                        while (progress < mRxRoundProgress1.getMax()) {
                            progress += mRxRoundProgress1.getMax() * 0.01;
                            if (progress < mRxRoundProgress1.getMax()) {
                                mRxRoundProgress1.setProgress(progress);
                            }
                            Thread.sleep(8);
                        }
                        while (progress > 0) {
                            progress -= mRxRoundProgress1.getMax() * 0.01;
                            if (progress > 0) {
                                mRxRoundProgress1.setProgress(progress);
                            }
                            Thread.sleep(8);
                        }

                        if (money != 0) {
                            while (progress < money) {
                                progress += money * 0.01;
                                mRxRoundProgress1.setProgress(progress);
                                Thread.sleep(10);
                            }
                        }

                        mRxRoundProgress1.setProgress(money);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        downLoadThread2.start();
    }

    private void initLineProgress() {
        // TODO Auto-generated method stub
        progress1 = 0;// ???????????????

        mPbLineOfCredit.setProgress(progress1);
        mPbLineOfCredit.setMax(getMax(money1));

        downLoadThread1 = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    while (!downLoadThread1.isInterrupted()) {
                        while (progress1 < mPbLineOfCredit.getMax()) {
                            progress1 += mPbLineOfCredit.getMax() * 0.01;
                            if (progress1 < mPbLineOfCredit.getMax()) {
                                mPbLineOfCredit.setProgress(progress1);
                                //tv_current.setText(progress1+"");
                            }
                            Thread.sleep(8);
                        }
                        while (progress1 > 0) {
                            progress1 -= mPbLineOfCredit.getMax() * 0.01;
                            if (progress1 > 0) {
                                mPbLineOfCredit.setProgress(progress1);
                                //tv_current.setText(progress1+"");
                            }
                            Thread.sleep(8);
                        }

                        while (progress1 < money1) {
                            progress1 += money1 * 0.01;
                            mPbLineOfCredit.setProgress(progress1);
                            //tv_current.setText(progress1+"");
                            Thread.sleep(10);
                        }
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        downLoadThread1.start();
    }

    private void initRxRoundPd() {
        // TODO Auto-generated method stub
        mRxRoundProgress = 0;// ???????????????

        mRxRoundPd1.setMax(mRxRoundPdMax);
        mRxRoundPd2.setMax(mRxRoundPdMax);
        mRxRoundPd3.setMax(mRxRoundPdMax);
        mRxRoundPd4.setMax(mRxRoundPdMax);
        mRxRoundPd5.setMax(mRxRoundPdMax);
        mRxRoundPd6.setMax(mRxRoundPdMax);
        mRxRoundPd7.setMax(mRxRoundPdMax);
        mRxRoundPd8.setMax(mRxRoundPdMax);
        mRxRoundPd9.setMax(mRxRoundPdMax);
        mRxRoundPd10.setMax(mRxRoundPdMax);
        mRxRoundPd11.setMax(mRxRoundPdMax);
        mRxRoundPd12.setMax(mRxRoundPdMax);
        mRxRoundPd13.setMax(mRxRoundPdMax);
        mRxRoundPd14.setMax(mRxRoundPdMax);
        mRxRoundPd15.setMax(mRxRoundPdMax);
        mRxRoundPd16.setMax(mRxRoundPdMax);
        mRxRoundPd17.setMax(mRxRoundPdMax);
        mProgressOne.setMax(mRxRoundPdMax);
        mProgressTwo.setMax(mRxRoundPdMax);
        mProgressThree.setMax(mRxRoundPdMax);


        downLoadRxRoundPdThread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    while (!downLoadRxRoundPdThread.isInterrupted()) {
                        while (mRxRoundProgress < mRxRoundPd1.getMax()) {
                            mRxRoundProgress += mRxRoundPd1.getMax() * 0.01;
                            if (mRxRoundProgress < mRxRoundPd1.getMax()) {
                                Message message = new Message();
                                message.what = 101;
                                mRxRoundPdHandler.sendMessage(message);
                            }
                            Thread.sleep(8);
                        }
                        while (mRxRoundProgress > 0) {
                            mRxRoundProgress -= mRxRoundPd1.getMax() * 0.01;
                            if (mRxRoundProgress > 0) {
                                Message message = new Message();
                                message.what = 101;
                                mRxRoundPdHandler.sendMessage(message);
                            }
                            Thread.sleep(8);
                        }

                        while (mRxRoundProgress < mRxRoundPdMax) {
                            mRxRoundProgress += mRxRoundPdMax * 0.01;
                            Message message = new Message();
                            message.what = 101;
                            mRxRoundPdHandler.sendMessage(message);
                            Thread.sleep(10);
                        }
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        downLoadRxRoundPdThread.start();
    }

    //----------------------------------------------------------------------------------------------Flikerbar ?????????????????? start
    private void initFlikerProgressBar() {
        if (!mFlikerbar.isFinish()) {
            mFlikerbar.toggle();
            mRoundFlikerbar.toggle();

            if (mFlikerbar.isStop()) {
                downLoadThread.interrupt();
            } else {
                downLoad();
            }

        }
    }

    public void reLoad(View view) {

        downLoadThread.interrupt();
        // ????????????
        mFlikerbar.reset();
        mRoundFlikerbar.reset();

        downLoad();


    }
    //==============================================================================================Flikerbar ?????????????????? end

    private void downLoad() {
        downLoadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!downLoadThread.isInterrupted()) {
                        float progress = mFlikerbar.getProgress();
                        progress += 2;
                        Thread.sleep(200);
                        Message message = handler.obtainMessage();
                        message.arg1 = (int) progress;
                        handler.sendMessage(message);
                        if (progress == 100) {
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        downLoadThread.start();
    }

    @OnClick({R2.id.flikerbar, R2.id.round_flikerbar})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.flikerbar) {
            initFlikerProgressBar();
        } else if (id == R.id.round_flikerbar) {
            initFlikerProgressBar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        downLoadThread.interrupt();
        downLoadThread1.interrupt();
        downLoadThread2.interrupt();
        downLoadRxRoundPdThread.interrupt();
    }
}
