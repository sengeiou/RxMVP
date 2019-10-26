package com.yumore.common.mvp;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.yumore.common.basic.BaseViewer;

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
    public <P extends AbstractPresenter> void attachView(BaseViewer baseViewer) {
        PresenterStore presenterStore = presenterProvider.getPresenterStore();
        HashMap<String, P> hashMap = presenterStore.getMap();
        for (Map.Entry<String, P> entry : hashMap.entrySet()) {
            P presenter = entry.getValue();
            if (presenter != null) {
                presenter.attachView(baseViewer);
            }
        }
    }

    public <P extends AbstractPresenter> void detachView() {
        PresenterStore presenterStore = presenterProvider.getPresenterStore();
        HashMap<String, P> hashMap = presenterStore.getMap();
        for (Map.Entry<String, P> entry : hashMap.entrySet()) {
            P presenter = entry.getValue();
            if (presenter != null) {
                presenter.detachView();
            }
        }
    }

    public <P extends AbstractPresenter> void onCreatePresenter(@Nullable Bundle savedInstanceState) {
        PresenterStore presenterStore = presenterProvider.getPresenterStore();
        HashMap<String, P> hashMap = presenterStore.getMap();
        for (Map.Entry<String, P> entry : hashMap.entrySet()) {
            P presenter = entry.getValue();
            if (presenter != null) {
                presenter.onCreatePresenter(savedInstanceState);
            }
        }
    }

    public <P extends AbstractPresenter> void onDestroyPresenter() {
        PresenterStore presenterStore = presenterProvider.getPresenterStore();
        HashMap<String, P> hashMap = presenterStore.getMap();
        for (Map.Entry<String, P> entry : hashMap.entrySet()) {
            P presenter = entry.getValue();
            if (presenter != null) {
                presenter.onDestroyPresenter();
            }
        }
    }

    public <P extends AbstractPresenter> void onSaveInstanceState(Bundle outState) {
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
