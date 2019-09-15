package com.yumore.common.mvp;

import android.os.Bundle;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nathaniel
 */
public class PresenterDispatch {
    private PresenterProvider presenterProvider;

    public PresenterDispatch(PresenterProvider presenterProvider) {
        this.presenterProvider = presenterProvider;
    }

    @SuppressWarnings("unchecked")
    public <P extends BasePresenter> void attachView(BaseView view) {
        PresenterStore presenterStore = presenterProvider.getPresenterStore();
        HashMap<String, P> hashMap = presenterStore.getMap();
        for (Map.Entry<String, P> entry : hashMap.entrySet()) {
            P presenter = entry.getValue();
            if (presenter != null) {
                presenter.attachView(view);
            }
        }
    }

    public <P extends BasePresenter> void detachView() {
        PresenterStore presenterStore = presenterProvider.getPresenterStore();
        HashMap<String, P> hashMap = presenterStore.getMap();
        for (Map.Entry<String, P> entry : hashMap.entrySet()) {
            P presenter = entry.getValue();
            if (presenter != null) {
                presenter.detachView();
            }
        }
    }

    public <P extends BasePresenter> void onCreatePresenter(@Nullable Bundle savedInstanceState) {
        PresenterStore presenterStore = presenterProvider.getPresenterStore();
        HashMap<String, P> hashMap = presenterStore.getMap();
        for (Map.Entry<String, P> entry : hashMap.entrySet()) {
            P presenter = entry.getValue();
            if (presenter != null) {
                presenter.onCreatePresenter(savedInstanceState);
            }
        }
    }

    public <P extends BasePresenter> void onDestroyPresenter() {
        PresenterStore presenterStore = presenterProvider.getPresenterStore();
        HashMap<String, P> hashMap = presenterStore.getMap();
        for (Map.Entry<String, P> entry : hashMap.entrySet()) {
            P presenter = entry.getValue();
            if (presenter != null) {
                presenter.onDestroyPresenter();
            }
        }
    }

    public <P extends BasePresenter> void onSaveInstanceState(Bundle outState) {
        PresenterStore presenterStore = presenterProvider.getPresenterStore();
        HashMap<String, P> hashMap = presenterStore.getMap();
        for (Map.Entry<String, P> entry : hashMap.entrySet()) {
            P presenter = entry.getValue();
            if (presenter != null) {
                presenter.onSaveInstanceState(outState);
            }
        }
    }
}
