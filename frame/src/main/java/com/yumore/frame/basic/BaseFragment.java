package com.yumore.frame.basic;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.yumore.frame.R;

/**
 * @author Nathaniel
 */
public abstract class BaseFragment<P extends BaseContract> extends Fragment implements BaseView {
    protected Context context;
    protected P presenter;
    protected View rootView;
    protected ViewGroup viewGroup;
    private boolean viewCreated = false;
    private boolean viewVisible = false;
    private boolean firstLoaded = true;
    private Unbinder unbinder;
    private AlertDialog alertDialog;

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
    public void dismissLoading() {
        if (null != alertDialog) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    public Fragment getFragment() {
        return this;
    }
}
