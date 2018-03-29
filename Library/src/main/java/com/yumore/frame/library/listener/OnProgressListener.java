package com.yumore.frame.library.listener;

/**
 * @author Nathaniel
 * @date 2018/3/29-17:37
 */

public interface OnProgressListener {
    /**
     * 显示当前的进度
     *
     * @param currentBytes  当前文件字节数
     * @param contentLength 当前长度
     * @param done          是否完成
     */
    void onProgress(long currentBytes, long contentLength, boolean done);
}
