package com.yumore.frame.basic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.yumore.frame.R;
import com.yumore.frame.permission.NathanielPermission;
import com.yumore.frame.permission.PermissionCallback;
import com.yumore.frame.utility.ActivityManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseActivity
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/8 - 15:11
 */
public abstract class BaseActivity<P extends BaseContract> extends AppCompatActivity implements BaseView, PermissionCallback {
    protected P presenter;
    private Context context;
    private Unbinder unbinder;
    private AlertDialog alertDialog;
    private PermissionCallback permissionCallBack;
    private int requestCode;
    private String[] permissions;

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
        if (TextUtils.isEmpty(message)) {
            textView.setText(message);
        }
        alertDialog = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setView(view)
                .create();
        alertDialog.show();
        Window dialogWindow = alertDialog.getWindow();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.width = (int) (display.getWidth() * 0.5);
            layoutParams.height = (int) (display.getWidth() * 0.5);
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

    protected void requestPermission(int requestCode, String[] permissions, String rationale, PermissionCallback permissionCallback) {
        this.requestCode = requestCode;
        this.permissionCallBack = permissionCallback;
        this.permissions = permissions;

        NathanielPermission.with(this)
                .addRequestCode(requestCode)
                .permissions(permissions)
                .negativeButtonText(android.R.string.ok)
                .positiveButtonText(android.R.string.cancel)
                .rationale(rationale)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        NathanielPermission.onRequestPermissionsResult(getContext(), requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NathanielPermission.SETTINGS_REQUEST_CODE) {
            if (NathanielPermission.hasPermissions(getContext(), permissions)) {
                onPermissionGranted(requestCode, permissions);
            } else {
                onPermissionDenied(requestCode, permissions);
            }
        }
    }

    @Override
    public void onPermissionGranted(int requestCode, String... permissions) {
        if (permissionCallBack != null) {
            permissionCallBack.onPermissionGranted(requestCode, permissions);
        }
    }

    @Override
    public void onPermissionDenied(final int requestCode, final String... permissions) {
        if (NathanielPermission.checkDeniedPermissionsNeverAskAgain(getContext(), getResources().getString(R.string.system_permission_tips),
                android.R.string.ok,
                android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (permissionCallBack != null) {
                            permissionCallBack.onPermissionDenied(requestCode, permissions);
                        }
                    }
                }, permissions)) {
            return;
        }

        if (permissionCallBack != null) {
            permissionCallBack.onPermissionDenied(requestCode, permissions);
        }
    }
}
