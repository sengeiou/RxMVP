package com.yumore.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author Nathaniel
 * @date 19-3-1 - 下午4:49
 */
public interface ISampleProvider extends IProvider {
    /**
     * 获取用户信息
     *
     * @return string
     */
    String getUsername();

    /**
     * 是否展示过图片引导
     *
     * @return true|false
     */
    boolean getTractionEnable();

    /**
     * 标记是否展示过图片引导
     *
     * @param tractionEnable true|false
     */
    void setTractionEnable(boolean tractionEnable);

    /**
     * 是否展示过视频引导
     *
     * @return true|false
     */
    boolean getIntroduceEnable();

    /**
     * 标记是否展示过视频引导
     *
     * @param introduceEnable true|false
     */
    void setIntroduceEnable(boolean introduceEnable);
}
