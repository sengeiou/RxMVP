package com.yumore.frame.library.basic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yumore.frame.library.helper.ViewHelper;

public abstract class BaseFragment<P extends BaseContract> extends Fragment implements BaseView, ViewHelper {
    protected Context context;
    protected P presenter;
    protected View rootView;
    private boolean viewCreated = false;
    private boolean viewVisible = false;
    private boolean firstLoaded = true;

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

    protected abstract P initPresenter();

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void initialize() {
        initView();
        loadData();
        bindView();
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
        return context;
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
        viewCreated = false;
        super.onDestroyView();
    }

    @Override
    public void showLoadingDialog(String message) {

    }

    @Override
    public void dismissLoadingDialog() {

    }
}
