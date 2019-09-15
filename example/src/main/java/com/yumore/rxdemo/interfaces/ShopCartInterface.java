package com.yumore.rxdemo.interfaces;

import android.view.View;

/**
 * @author yumore
 * @date 16-11-13
 */
public interface ShopCartInterface {
    void add(View view, int postion);

    void remove(View view, int postion);
}
