package com.yumore.common.mvp;

import java.util.HashMap;

/**
 * create by yumore
 * time:2018/7/26
 *
 * @author Nathaniel
 */
public class PresenterStore<P extends AbstractPresenter> {
    private static final String STORE_DEFAULT_KEY = "PresenterStore.DefaultKey";
    private final HashMap<String, P> hashMap = new HashMap<>();

    public final void put(String key, P presenter) {
        P oldPresenter = hashMap.put(STORE_DEFAULT_KEY + ":" + key, presenter);
        if (oldPresenter != null) {
            oldPresenter.onCleared();
        }
    }

    public final P get(String key) {
        return hashMap.get(STORE_DEFAULT_KEY + ":" + key);
    }

    public final void clear() {
        for (P presenter : hashMap.values()) {
            presenter.onCleared();
        }
        hashMap.clear();
    }

    public int getSize() {
        return hashMap.size();
    }

    public HashMap<String, P> getMap() {
        return hashMap;
    }
}
