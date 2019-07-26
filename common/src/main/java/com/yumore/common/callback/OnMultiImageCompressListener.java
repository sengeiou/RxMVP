package com.yumore.common.callback;

import java.io.File;

/**
 * 压缩回调
 *
 * @author Nathaniel
 */
public interface OnMultiImageCompressListener {
    /**
     * 准备压缩
     */
    void onPrepared();

    /**
     * 压缩成功
     *
     * @param position 队列中的第几个
     * @param file     压缩后的图片
     */
    void onSuccess(int position, File file);

    /**
     * 压缩失败
     *
     * @param throwable 压缩失败的原因
     */
    void onFailure(Throwable throwable);
}
