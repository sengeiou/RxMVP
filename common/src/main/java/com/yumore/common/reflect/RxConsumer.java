package com.yumore.common.reflect;

import io.reactivex.functions.Consumer;

/**
 * @author Nathaniel
 * @date 2019/3/17 - 16:13
 */
public abstract class RxConsumer<T> implements Consumer<T> {

    @Override
    public void accept(T t) throws Exception {
        onSuccess(t);
    }

    /**
     * 正常的数据
     *
     * @param t t
     * @throws Exception exception
     */
    public abstract void onSuccess(T t) throws Exception;
}