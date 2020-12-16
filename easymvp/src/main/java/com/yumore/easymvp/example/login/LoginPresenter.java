package com.yumore.easymvp.example.login;


import com.yumore.easymvp.mvp.BasePresenter;

/**
 * create by yumore
 * time:2018/7/26
 */
public class LoginPresenter extends BasePresenter<LoginView> {

    public void login() {
        mView.loginSuccess();
    }
}
