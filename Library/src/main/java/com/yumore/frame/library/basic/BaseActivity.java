package com.yumore.frame.library.basic;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.yumore.frame.library.R;
import com.yumore.frame.library.helper.ViewHelper;
import com.yumore.frame.library.utility.ActivityManager;
import com.yumore.frame.library.utility.StatusBarManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseActivity
 *
 * @author Nathaniel
 *         nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/8 - 15:11
 */
public abstract class BaseActivity<P extends BaseContract> extends AppCompatActivity implements BaseView, ViewHelper {
    public Context context;
    protected P presenter;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        beforeInit();
        setContentView(getLayoutId());
        initialize();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void beforeInit() {
        setStatusBar(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initialize() {
        context = this;
        ActivityManager.getAppInstance().addActivity(this);
        presenter = initPresenter();
        if (null != presenter) {
            presenter.attachView(this);
        }
        initView();
        loadData();
        bindView();
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getAppInstance().removeActivity(this);
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        super.onDestroy();
    }

    @Override
    public void initView() {
        unbinder = ButterKnife.bind(this);
    }

    /**
     * 在子类中初始化对应的presenter
     *
     * @return 相应的presenter
     */
    public abstract P initPresenter();


    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    public void showLoadingDialog(String message) {

    }

    @Override
    public Context getContext() {
        return context;
    }

    private void setStatusBar(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            StatusBarManager tintManager = new StatusBarManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorPrimary);
            activity.getWindow().getDecorView().setFitsSystemWindows(true);
        }
    }
}
