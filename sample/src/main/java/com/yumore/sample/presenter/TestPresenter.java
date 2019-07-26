package com.yumore.sample.presenter;

import android.annotation.SuppressLint;
import com.yumore.common.basic.BasePresenter;
import com.yumore.common.helper.ExceptionHelper;
import com.yumore.sample.contact.TestContact;
import com.yumore.sample.engine.CustomEngine;
import com.yumore.sample.entity.Story;
import com.yumore.sample.entity.Test;
import com.yumore.sample.view.TestView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class TestPresenter extends BasePresenter<TestView> implements TestContact {

    @SuppressLint("CheckResult")
    @Override
    public void getData() {
        CustomEngine.getInstance().test()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) {
                        addDisposable(disposable);
                        baseView.showLoading("正在加载数据");
                    }
                })
                .map(new Function<Test, List<Story>>() {
                    @Override
                    public List<Story> apply(@NonNull Test testBean) {
                        return testBean.getStories();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Story>>() {
                    @Override
                    public void accept(@NonNull List<Story> storiesBeen) {
                        baseView.dismissLoading();
                        baseView.setData(storiesBeen);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) {
                        baseView.dismissLoading();
                        ExceptionHelper.handleException(throwable);
                    }
                });
    }
}
