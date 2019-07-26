package com.yumore.common.basic;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.yumore.common.R;
import com.yumore.common.utility.ActivityManager;

/**
 * BaseActivity
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/8 - 15:11
 */
public abstract class BaseActivity<P extends BaseContract> extends AppCompatActivity implements BaseView {
    protected P presenter;
    private Context context;
    private Unbinder unbinder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeInit();
        setContentView(getLayoutId());
        initialize();
    }

    @Override
    public void beforeInit() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @SuppressWarnings("unchecked")
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
    public void initView() {
        if (null == unbinder) {
            unbinder = ButterKnife.bind(this);
        }
    }

    @Override
    protected void onDestroy() {
        //将当前activity移除管理栈
        ActivityManager.getAppInstance().removeActivity(this);
        if (null != presenter) {
            //在presenter中解绑释放view
            presenter.detachView();
            presenter = null;
        }
        if (null != unbinder) {
            unbinder.unbind();
            unbinder = null;
        }
        super.onDestroy();
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
        TextView textView = view.findViewById(R.id.loading_text_tv);
        if (!TextUtils.isEmpty(message)) {
            textView.setText(message);
        }
        alertDialog = new AlertDialog.Builder(getContext())
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return context;
    }
}
