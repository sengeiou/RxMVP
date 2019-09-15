package com.yumore.rxui.view.cardstack.tools;

/**
 * @author yumore
 * @date 2018/6/11 11:36:40 整合修改
 */
public interface RxScrollDelegate {

    void scrollViewTo(int x, int y);

    int getViewScrollY();

    void setViewScrollY(int y);

    int getViewScrollX();

    void setViewScrollX(int x);

}
