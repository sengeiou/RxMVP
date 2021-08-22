package com.yumore.provider;


import android.os.Handler;
import android.os.Looper;

import com.yumore.provider.callback.RunnableCallback;

/**
 * @author Nathaniel
 */
public abstract class AbstractTask<T> implements Runnable, RunnableCallback<T> {
    private static final String TAG = AbstractTask.class.getSimpleName();

    public AbstractTask() {
        prepareRunnable();
    }

    @Override
    public void run() {
        final T t = doRunnableCode();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                runnableCallback(t);
            }
        });
    }

    /**
     * 耗时操作
     *
     * @return T data
     */
    protected abstract T doRunnableCode();

    @Override
    public void prepareRunnable() {
        LoggerUtils.logger(TAG, "prepareRunnable() has been called");
    }
}
