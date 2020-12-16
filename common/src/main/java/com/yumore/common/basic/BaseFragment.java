package com.yumore.common.basic;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.yumore.common.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Nathaniel
 */
public abstract class BaseFragment<P extends BaseContract> extends Fragment implements BaseViewer {
    protected P presenter;
    protected boolean withoutMore;
    private Context context;
    private View rootView;
    private ViewGroup viewGroup;
    private boolean viewCreated = false;
    private boolean viewVisible = false;
    private boolean firstLoaded = true;
    private Unbinder unbinder;
    private AlertDialog alertDialog;

    /**
     * @param className className
     * @return BaseFragment
     */
    private static BaseFragment createFragment(@NonNull String className) {
        BaseFragment baseFragment = null;
        try {
            Class<?> clazz = Class.forName(className);
            baseFragment = (BaseFragment) clazz.newInstance();
        } catch (IllegalAccessException | java.lang.InstantiationException | ClassNotFoundException e) {
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
        beforeInit(savedInstanceState);
    }

    @Override
    public void beforeInit(Bundle savedInstanceState) {
        // u can do something before view has been initialized
    }

    @Override
    public void onAttach(@NonNull Context context) {
        // u can convert this context to activity
        this.context = context;
        super.onAttach(context);
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

    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    protected boolean isFirstLoad() {
        return firstLoaded;
    }

    protected void visibleToUser() {
        if (firstLoaded) {
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
    public void dismissLoading() {
        if (null != alertDialog) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    public Fragment getFragment() {
        return this;
    }

    @Override
    public void withoutMore(boolean withoutMore) {
        this.withoutMore = withoutMore;
    }
}
