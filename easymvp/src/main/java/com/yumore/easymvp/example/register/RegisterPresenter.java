package com.yumore.easymvp.example.register;


import com.yumore.easymvp.mvp.BasePresenter;

/**
 * create by yumore
 * time:2018/7/26
 */
public class RegisterPresenter extends BasePresenter<RegisterView> {

    public void register() {
        mView.registerSuccess();
    }
}
