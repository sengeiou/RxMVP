package com.yumore.camera.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.yumore.camera.R;
import com.yumore.camera.engine.camera.CameraParam;
import com.yumore.camera.fragment.CameraPreviewFragment;
import com.yumore.facedetect.engine.FaceTracker;
import com.yumore.uitls.fragment.MusicSelectFragment;
import com.yumore.uitls.utils.NotchUtils;
import com.yumore.uitls.utils.StatusBarUtils;

/**
 * 相机预览页面
 */
public class CameraActivity extends AppCompatActivity {

    private static final String FRAGMENT_CAMERA = "fragment_camera";
    private static final String FRAGMENT_MUSIC = "fragment_music";
    /**
     * Home按键监听服务
     */
    private final BroadcastReceiver mHomePressReceiver = new BroadcastReceiver() {
        private final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (TextUtils.isEmpty(reason)) {
                    return;
                }
                // 当点击了home键时需要停止预览，防止后台一直持有相机
                if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                    CameraPreviewFragment fragment = (CameraPreviewFragment) getSupportFragmentManager()
                            .findFragmentByTag(FRAGMENT_CAMERA);
                    if (fragment != null) {
                        fragment.cancelRecordIfNeeded();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            CameraPreviewFragment fragment = new CameraPreviewFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, FRAGMENT_CAMERA)
                    .addToBackStack(FRAGMENT_CAMERA)
                    .commit();
        }
        faceTrackerRequestNetwork();

        // 选择音乐监听器
        CameraParam.getInstance().musicSelectListener = () -> {
            MusicSelectFragment musicFragment = new MusicSelectFragment();
            musicFragment.addOnMusicSelectedListener((music) -> {
                getSupportFragmentManager().popBackStack();

                CameraPreviewFragment cameraFragment = (CameraPreviewFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_CAMERA);
                if (cameraFragment != null) {
                    cameraFragment.setMusicPath(music.getSongUrl());
                }
            });
            this.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, musicFragment)
                    .addToBackStack(FRAGMENT_MUSIC)
                    .commit();
        };

        // 判断是否存在刘海屏
        if (NotchUtils.hasNotchScreen(this)) {
            View view = findViewById(R.id.view_safety_area);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.height = StatusBarUtils.getStatusBarHeight(this);
            view.setLayoutParams(params);
        }
    }

    /**
     * 人脸检测SDK验证，可以替换成自己的SDK
     */
    private void faceTrackerRequestNetwork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FaceTracker.requestFaceNetwork(CameraActivity.this);
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleFullScreen();
        registerHomeReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterHomeReceiver();
    }

    @Override
    protected void onDestroy() {
        CameraParam.getInstance().musicSelectListener = null;
        super.onDestroy();
    }

    private void handleFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        // 是否全面屏
        if (NotchUtils.hasNotchScreen(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 1) {
            getSupportFragmentManager().popBackStack();
        } else if (backStackEntryCount == 1) {
            finish();
            overridePendingTransition(0, R.anim.anim_slide_down);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 注册服务
     */
    private void registerHomeReceiver() {
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomePressReceiver, homeFilter);
    }

    /**
     * 注销服务
     */
    private void unRegisterHomeReceiver() {
        unregisterReceiver(mHomePressReceiver);
    }
}
