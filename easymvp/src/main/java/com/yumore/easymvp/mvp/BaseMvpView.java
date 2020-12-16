package com.yumore.easymvp.mvp;

/**
 * @author nathaniel
 */
public interface BaseMvpView {
    void showError(String msg);

    void complete();

    void showProgressUI(boolean isShow);
}
