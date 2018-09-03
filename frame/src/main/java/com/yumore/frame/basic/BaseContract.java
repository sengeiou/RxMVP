package com.yumore.frame.basic;

import io.reactivex.disposables.Disposable;

/**
 * BasePresenter
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/8 - 15:02
 */
public interface BaseContract<V extends BaseView> {
    @SuppressWarnings("unchecked")
    void attachView(V baseView);

    void detachView();

    void addDisposable(Disposable disposable);

    void unDisposable();
}
