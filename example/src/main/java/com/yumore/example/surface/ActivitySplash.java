package com.yumore.example.surface;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yumore.common.manager.ThreadManager;
import com.yumore.example.R;
import com.yumore.utility.activity.BaseActivity;
import com.yumore.utility.utility.RxDeviceTool;
import com.yumore.utility.widget.RxToast;
import com.yumore.utility.widget.dialog.RxDialogSureCancel;


/**
 * @author yumore
 */
public class ActivitySplash extends BaseActivity {

    ProgressBar pg;
    boolean update = false;
    private TextView tv_splash_version;
    private TextView tv_update_info;
    private Context context;
    @SuppressWarnings("HandlerLeak")
    private final Handler checkHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!update) {
                RxToast.showToast(context, "正在检查版本更新...", 500);
                String temp = getResources().getString(R.string.newest_apk_down);
                String timeTip = String.format(temp, RxDeviceTool.getAppVersionName(context));
                ShowDialog(timeTip, "your_apk_down_url");
            } else {
                RxToast.showToast(context, "当前为最新版本，无需更新!", 500);
                pg.setVisibility(View.GONE);
            }
        }
    };
    private String appVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        initView();
        checkUpdate();

    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        pg = findViewById(R.id.pg);
        tv_update_info = findViewById(R.id.tv_update_info);
        tv_splash_version = findViewById(R.id.tv_splash_version);
        appVersionName = RxDeviceTool.getAppVersionName(context);
        tv_splash_version.setText("版本号 " + appVersionName);
    }

    public void buttonClick(View v) {
        RxToast.showToast(context, "正在进入主界面", 500);
        toMain();
    }

    public void toMain() {
        Intent intent = new Intent();
        intent.setClass(context, ExampleActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 检查是否有新版本，如果有就升级
     */
    private void checkUpdate() {
        ThreadManager.getThreadPollProxy().execute(() -> {
            Message msg = checkHandler.obtainMessage();
            checkHandler.sendMessage(msg);
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            update = true;
            checkHandler.sendMessage(new Message());
        });
    }

    private void ShowDialog(String strAppVersionName, String apk_down_url) {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(context);
        rxDialogSureCancel.getContentView().setText(strAppVersionName);
        rxDialogSureCancel.getSureView().setOnClickListener(v -> {
            //getFile(apk_down_url, RxFileTool.getDiskFileDir(context) + File.separator + "update", str + ".apk");
            // TODO: 第一步 在此处 使用 你的网络框架下载 新的Apk文件 可参照下面的例子 使用的是 okGo网络框架
            // TODO: 第二步 可使用 RxAppTool.InstallAPK(context,file.getAbsolutePath()); 方法进行 最新版本Apk文件的安装
            rxDialogSureCancel.cancel();
        });
        rxDialogSureCancel.getCancelView().setOnClickListener(v -> {
            RxToast.showToast(context, "已取消最新版本的下载", 500);
            rxDialogSureCancel.cancel();
        });
        rxDialogSureCancel.show();
    }
}
