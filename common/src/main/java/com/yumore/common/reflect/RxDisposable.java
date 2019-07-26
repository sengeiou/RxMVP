package com.yumore.common.reflect;

import io.reactivex.functions.Consumer;

/**
 * @author Nathaniel
 * @date 2019/3/17 - 17:27
 */
public abstract class RxDisposable<T> implements Consumer<T> {
    @Override
    public void accept(T t) throws Exception {
        onDisposable(t);
    }

    public abstract void onDisposable(T t) throws Exception;
}
