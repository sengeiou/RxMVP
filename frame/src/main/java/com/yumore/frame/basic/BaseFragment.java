package com.yumore.frame.basic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.yumore.frame.R;
import com.yumore.frame.permission.NathanielPermission;
import com.yumore.frame.permission.PermissionCallback;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Nathaniel
 */
public abstract class BaseFragment<P extends BaseContract> extends Fragment implements BaseView, PermissionCallback {
    protected Context context;
    protected P presenter;
    protected View rootView;
    protected ViewGroup viewGroup;
    private boolean viewCreated = false;
    private boolean viewVisible = false;
    private boolean firstLoaded = true;
    private Unbinder unbinder;
    private AlertDialog alertDialog;
    private int requestCode;
    private String[] permissions;
    private PermissionCallback permissionCallBack;

    public static BaseFragment createFragment(@NonNull String className) {
        BaseFragment baseFragment = null;
        try {
            Class<?> clazz = Class.forName(className);
            baseFragment = (BaseFragment) clazz.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return baseFragment;
    }

    /**
     * 初始化presenter
     *
     * @return presenter
     */
    protected abstract P initPresenter();

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.viewGroup = container;
        if (null == rootView) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        presenter = initPresenter();
        if (null != presenter) {
            presenter.attachView(this);
        }
        initialize();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewCreated = true;
    }

    @Override
    public void beforeInit() {

    }

    @Override
    public void initialize() {
        initView();
        loadData();
        bindView();
    }

    @Override
    public void initView() {
        if (null == unbinder) {
            unbinder = ButterKnife.bind(this, rootView);
        }
    }

    protected View getRootView() {
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        viewVisible = isVisibleToUser;
        if (isVisibleToUser && viewCreated) {
            visibleToUser();
        }
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewVisible) {
            visibleToUser();
        }
    }

    protected void firstLoad() {

    }

    protected void visibleToUser() {
        if (firstLoaded) {
            firstLoad();
            firstLoaded = false;
        }
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.detachView();
        }
        if (null != unbinder) {
            unbinder.unbind();
            unbinder = null;
        }
        viewCreated = false;
        super.onDestroyView();
    }

    @Override
    public void showMessage(@NonNull String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
        @SuppressWarnings("ConstantConditions")
        WindowManager windowManager = getActivity().getWindowManager();
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
    public void dismissLoading() {
        if (null != alertDialog) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    protected void requestPermission(int requestCode, String[] permissions, String rationale, PermissionCallback permissionCallback) {
        this.requestCode = requestCode;
        this.permissionCallBack = permissionCallback;
        this.permissions = permissions;

        NathanielPermission.with(getFragment())
                .addRequestCode(requestCode)
                .permissions(permissions)
                .negativeButtonText(android.R.string.ok)
                .positiveButtonText(android.R.string.cancel)
                .rationale(rationale)
                .request();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        NathanielPermission.onRequestPermissionsResult(getContext(), requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NathanielPermission.SETTINGS_REQUEST_CODE) {
            if (NathanielPermission.hasPermissions(getContext(), permissions)) {
                onPermissionGranted(this.requestCode, permissions);
            } else {
                onPermissionDenied(this.requestCode, permissions);
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
        if (NathanielPermission.checkDeniedPermissionsNeverAskAgain(getContext(), "授权啊,不授权没法用啊," + "去设置里授权大哥", android.R.string.ok,
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

    public Fragment getFragment() {
        return this;
    }
}
