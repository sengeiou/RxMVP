package com.yumore.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author Nathaniel
 * @date 2019/4/27 - 11:34
 */
public interface IRecorderProvider extends IProvider {
    int getFromType();

    long getFromId();

    long getFromFlag();

    int getPlatform();
}
