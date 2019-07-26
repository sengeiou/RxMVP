package com.yumore.common.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * BasePresenter
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/8 - 16:02
 */
public abstract class BasePresenter<V extends BaseView> implements BaseContract<V> {
    protected Context context;
    protected V baseView;
    private CompositeDisposable compositeDisposable;


    @Override
    public void detachView() {
        this.baseView = null;
        unDisposable();
    }

    @Override
    public void attachView(V baseView) {
        this.baseView = baseView;
        context = baseView.getContext();
    }

    @Override
    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void unDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
    }


    public boolean isAttachView() {
        return this.baseView != null;
    }

    public void onCreatePresenter(@Nullable Bundle savedState) {

    }

    protected void onCleared() {

    }

    public void onDestroyPresenter() {
        this.context = null;
        detachView();
    }

    public void onSaveInstanceState(Bundle outState) {

    }

}
