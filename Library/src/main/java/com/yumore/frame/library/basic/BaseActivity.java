package com.yumore.frame.library.basic;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yumore.frame.library.helper.ViewHelper;
import com.yumore.frame.library.utility.ActivityManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        beforeInit();
        setContentView(getLayoutId());
        initialize();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void beforeInit() {

    }

    @Override
    public void initialize() {
        context = this;
        //将当前activity添加进入管理栈
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
        //将当前activity移除管理栈
        ActivityManager.getAppInstance().removeActivity(this);
        if (presenter != null) {
            //在presenter中解绑释放view
            presenter.detachView();
            presenter = null;
        }
        super.onDestroy();
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
}
