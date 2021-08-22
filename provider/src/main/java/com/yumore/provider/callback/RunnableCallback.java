package com.yumore.provider.callback;

/**
 * @author Nathaniel
 */
public interface RunnableCallback<T> {
    /**
     * 开始预处理
     */
    void prepareRunnable();

    /**
     * 执行回调
     *
     * @param t T
     */
    void runnableCallback(T t);
}
