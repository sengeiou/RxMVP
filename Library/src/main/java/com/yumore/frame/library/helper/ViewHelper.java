package com.yumore.frame.library.helper;

/**
 * @author Nathaniel
 * @version v1.0.0
 * @date 2018/3/8-16:25
 */

public interface ViewHelper {

    void beforeInit();

    void initialize();

    int getLayoutId();

    void initView();

    void loadData();

    void bindView();
}
