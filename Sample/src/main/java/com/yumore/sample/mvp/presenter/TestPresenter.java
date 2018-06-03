package com.yumore.sample.mvp.presenter;

import android.annotation.SuppressLint;

import com.yumore.frame.basic.BasePresenter;
import com.yumore.frame.helper.ExceptionHelper;
import com.yumore.sample.entity.Story;
import com.yumore.sample.entity.Test;
import com.yumore.sample.model.CustomEngine;
import com.yumore.sample.mvp.contact.TestContact;
import com.yumore.sample.mvp.view.TestView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
                        baseView.showLoadingDialog();
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
                        baseView.dismissLoadingDialog();
                        baseView.setData(storiesBeen);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) {
                        baseView.dismissLoadingDialog();
                        ExceptionHelper.handleException(throwable);
                    }
                });
    }
}
