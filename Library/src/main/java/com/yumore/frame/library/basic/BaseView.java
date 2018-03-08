package com.yumore.frame.library.basic;

import android.content.Context;

/**
 * BaseView
 *
 * @author Nathaniel
 *         nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/8 - 15:08
 */
public interface BaseView {
    Context getContext();

    void showLoadingDialog(String message);

    void dismissLoadingDialog();
}
