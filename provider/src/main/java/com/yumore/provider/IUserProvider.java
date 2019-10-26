package com.yumore.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author Nathaniel
 * @date 19-3-1 - 下午4:49
 */
public interface IUserProvider extends IProvider {
    /**
     * 获取用户信息
     *
     * @return string
     */
    String getUsername();
}
