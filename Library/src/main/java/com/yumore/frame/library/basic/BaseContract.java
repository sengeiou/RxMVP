package com.yumore.frame.library.basic;

import io.reactivex.disposables.Disposable;

/**
 * BaseContract
 *
 * @author Nathaniel
 *         nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/8 - 15:02
 */
public interface BaseContract<V extends BaseView> {
    /**
     * 给view绑定presenter
     *
     * @param baseView view
     */
    void attachView(V baseView);

    /**
     * 解除与view绑定的presenter
     */
    void detachView();

    /**
     * 给view添加订阅
     *
     * @param disposable disposable
     */
    void addDisposable(Disposable disposable);

    /**
     * 解除订阅
     */
    void unDisposable();
}
