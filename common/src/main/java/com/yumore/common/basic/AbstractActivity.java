package com.yumore.common.basic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.yumore.common.BuildConfig;
import com.yumore.common.R;
import com.yumore.common.callback.OnNetworkListener;
import com.yumore.common.manager.ActivityManager;
import com.yumore.common.mvp.AbstractPresenter;
import com.yumore.common.mvp.PresenterDispatch;
import com.yumore.common.mvp.PresenterProvider;
import com.yumore.common.receiver.NetworkReceiver;
import com.yumore.common.utility.EmptyUtils;
import com.yumore.common.utility.LoggerUtils;
import com.yumore.common.utility.NetworkUtils;
import com.yumore.common.utility.StatusBarUtils;
import com.yumore.common.widget.CustomSnackBar;
import com.yumore.gallery.widget.SystemBarTintManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseActivity
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/8 - 15:11
 * * 如果需要使用沉浸式状态栏 支持链式调用
 * 集体的设置参考
 */
public abstract class AbstractActivity<P extends BaseContract> extends AppCompatActivity implements BaseViewer, OnNetworkListener {
    private static final String TAG = AbstractActivity.class.getSimpleName();
    protected static int SNACK_MESSAGE_SUCCESS = 0;
    protected static int SNACK_MESSAGE_FAILED = 1;
    protected static int SNACK_MESSAGE_WARNING = 2;
    protected P presenter;
    protected boolean withoutNext;
    private Unbinder unbinder;
    private AlertDialog alertDialog;
    private PresenterProvider presenterProvider;
    private PresenterDispatch presenterDispatch;
    private Toast toast;
    private NetworkReceiver networkReceiver;
    private boolean firstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 初始化之前要做的事情
        super.onCreate(savedInstanceState);
        beforeInit(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());

        presenterProvider = PresenterProvider.inject(this);
        presenterDispatch = new PresenterDispatch(presenterProvider);
        presenterDispatch.attachView(this);
        presenterDispatch.onCreatePresenter(savedInstanceState);

        registerNetworkListener();
        initialize();
        ActivityManager.getAppInstance().addActivity(getActivity());
    }

    private void registerNetworkListener() {
        networkReceiver = new NetworkReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(networkReceiver, filter);
    }

    @Override
    public void setContentView(int layoutId) {
        super.setContentView(layoutId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(getStatusBarColor());
        StatusBarUtils.setStatusBarMode(getActivity(), getStatusBarMode(), getStatusBarColor());
    }

    /**
     * 设置状态栏颜色
     *
     * @return 颜色
     */
    private int getStatusBarColor() {
        LoggerUtils.logger(TAG, "platformType = " + getPlatformType());
        switch (getPlatformType()) {
            case 0:
                return R.color.common_color_green_dark;
            case 1:
                return R.color.common_color_blue_dark;
            case 2:
                return R.color.common_color_white;
            case 3:
                return R.color.common_color_blue_sky;
            case 4:
                return R.color.common_color_purple_dark;
            case 999:
                return R.color.common_color_black_dark;
            default:
                return R.color.common_color_grey_dark;
        }
    }

    protected int getPlatformType() {
        return -1;
    }

    protected boolean getStatusBarMode() {
        return getPlatformType() == 2 || getPlatformType() == 999;
    }

    @Override
    public void beforeInit(Bundle savedInstanceState) {
        /*
         * TODO Only fullscreen activities can request orientation终极解决方法
         * 必须在super.onCreate(Bundle savedInstanceState)前调用，否则不起效果
         */
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
            Log.e(TAG, "onCreate fixOrientation when Oreo, result = " + result);
        }
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            @SuppressLint("PrivateApi")
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray typedArray = obtainStyledAttributes(styleableRes);
            Method method = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            method.setAccessible(true);
            isTranslucentOrFloating = (boolean) method.invoke(null, typedArray);
            method.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            Log.e(TAG, "avoid calling setRequestedOrientation when Oreo.");
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    private boolean fixOrientation() {
        try {
            Field field = AppCompatActivity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo activityInfo = (ActivityInfo) field.get(this);
            activityInfo.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenterDispatch.onSaveInstanceState(outState);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initialize() {
        //将当前activity添加进入管理栈
        presenter = initPresenter();
        if (null != presenter) {
            presenter.attachView(this);
        }
        initView();
        loadData();
        bindView();
    }

    @Override
    public void initView() {
        if (null == unbinder) {
            unbinder = ButterKnife.bind(this);
        }
    }

    protected AbstractPresenter getPresenter() {
        return presenterProvider.getPresenter(0);
    }

    public PresenterProvider getPresenterProvider() {
        return presenterProvider;
    }

    /**
     * 在子类中初始化对应的presenter
     *
     * @return 相应的presenter
     */
    protected abstract P initPresenter();


    @Override
    public void dismissLoading() {
        if (null != alertDialog) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    @Override
    public void showLoading(String message) {
        dismissLoading();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.common_loading_dialog, null);
        TextView textView = view.findViewById(R.id.loading_dialog_message);
        if (!TextUtils.isEmpty(message)) {
            textView.setText(message);
        }
        alertDialog = new AlertDialog.Builder(getContext(), R.style.CustomDialog)
                .setCancelable(false)
                .setView(view)
                .create();
        alertDialog.show();
        Window dialogWindow = alertDialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.alpha = 0.75f;
            dialogWindow.setAttributes(layoutParams);
        }
    }

    @Override
    public void showMessage(@NonNull String message) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.common_toast_layout, null);
        if (EmptyUtils.isEmpty(toast)) {
            toast = new Toast(getContext());
        }
        TextView textView = view.findViewById(R.id.common_toast_message);
        textView.setText(message);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    protected void showMessage(int resId) {
        String message = getContext().getResources().getString(resId);
        showMessage(message);
    }

    @Override
    public Context getContext() {
        return this;
    }

    protected AppCompatActivity getActivity() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstLoad = false;
    }

    public boolean isFirstLoad() {
        return firstLoad;
    }

    @Override
    public void withoutMore(boolean withoutMore) {
        this.withoutNext = withoutMore;
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        if (resources != null && resources.getConfiguration().fontScale != 1.0f) {
            android.content.res.Configuration configuration = resources.getConfiguration();
            configuration.fontScale = 1.0f;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resources;
    }

    @SuppressWarnings("unused")
    protected void showSnackBar(@NonNull View view, @NonNull String message) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    protected void showSnackBar(@NonNull String message, int level) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        // 注意getRootView() 如果不加这个会被状态栏遮挡
        CustomSnackBar customSnackBar = CustomSnackBar.make(findViewById(android.R.id.content).getRootView(), message, CustomSnackBar.LENGTH_LONG);
        View snackBarView = customSnackBar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.common_color_page));
        TextView textView = snackBarView.findViewById(R.id.snackbar_text);
        int textColor = level == SNACK_MESSAGE_FAILED ? Color.RED : level == SNACK_MESSAGE_WARNING ? Color.YELLOW : Color.BLACK;
        textView.setTextColor(textColor);
        textView.setGravity(Gravity.CENTER);
        customSnackBar.show();
    }

    @Override
    protected void onDestroy() {
        //将当前activity移除管理栈
        if (null != presenter) {
            // 在presenter中解绑释放view
            presenter.detachView();
            presenter = null;
        }
        if (null != unbinder) {
            unbinder.unbind();
            unbinder = null;
        }
        presenterDispatch.detachView();
        unregisterReceiver(networkReceiver);
        super.onDestroy();
    }

    @Override
    public void onStatusChanged(int connectedType) {
        int networkSpeedBytes = NetworkUtils.getNetworkSpeedBytes();
        switch (connectedType) {
            case NetworkUtils.NETWORK_TYPE_WIFI:
                if (BuildConfig.DEBUG) {
//                    if (networkSpeedBytes < 1024) {
//                        showSnackBar("您已切换到WiFi环境，但网速就堪忧啊", SNACK_MESSAGE_WARNING);
//                    } else {
//                        showSnackBar("您已切换到WiFi环境，网速杠杠滴", SNACK_MESSAGE_SUCCESS);
//                    }
                }
                break;

            case NetworkUtils.NETWORK_TYPE_2G:
            case NetworkUtils.NETWORK_TYPE_3G:
            case NetworkUtils.NETWORK_TYPE_4G:
                if (BuildConfig.DEBUG) {
//                    if (networkSpeedBytes < 1024) {
//                        showSnackBar("您的网络状态拥堵比北京早高峰还糟糕", SNACK_MESSAGE_WARNING);
//                    } else {
//                        showSnackBar("您的网络状态比较流畅，尽情享用", SNACK_MESSAGE_SUCCESS);
//                    }
                }
                break;
            default:
                if (BuildConfig.DEBUG) {
//                    showSnackBar("都什么年代了，怕是到了原始社会了", SNACK_MESSAGE_FAILED);
                }
                break;
        }
    }
}
