package com.yumore.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author Nathaniel
 * @date 2019/4/27 - 0:01
 */
public interface IVideoProvider extends IProvider {
    /**
     * 返回文件路径
     *
     * @return filePath
     */
    String getVideoPath();

    /**
     * 获取第一帧
     *
     * @return filePath
     */
    String getVideoThumbnail();
}
