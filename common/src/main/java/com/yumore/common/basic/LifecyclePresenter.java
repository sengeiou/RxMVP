package com.yumore.common.basic;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import org.jetbrains.annotations.NotNull;

/**
 * 实现presenter的生命周期管理
 * 防止因为一些操作在view生命周期之外
 * 发生操作导致内存泄漏
 *
 * @author Nathaniel
 * @date 2019/7/28 - 18:24
 * <p>
 * 以下几个方法暂时不考虑
 * @ OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
 * void onResume(@NonNull LifecycleOwner lifecycleOwner);
 * @ OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
 * void onPause(@NonNull LifecycleOwner lifecycleOwner);
 * @ OnLifecycleEvent(Lifecycle.Event.ON_STOP)
 * void onStop(@NonNull LifecycleOwner lifecycleOwner);
 */
public interface LifecyclePresenter extends LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate(@NonNull LifecycleOwner lifecycleOwner);

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(@NonNull LifecycleOwner lifecycleOwner);

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onLifecycleChanged(@NotNull LifecycleOwner lifecycleOwner, @NotNull Lifecycle.Event event);
}
