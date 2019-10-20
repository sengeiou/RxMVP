package com.yumore.common.mvp;

import android.app.Application;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * create by lzx
 * time:2018/7/26
 *
 * @author Nathaniel
 */
public class PresenterProvider {

    private PresenterStore presenterStore = new PresenterStore<>();
    private AppCompatActivity activity;
    private Fragment fragment;
    private Class<?> clazz;

    private PresenterProvider(AppCompatActivity activity, Fragment fragment) {
        if (activity != null) {
            this.activity = activity;
            clazz = this.activity.getClass();
        }
        if (fragment != null) {
            this.fragment = fragment;
            clazz = this.fragment.getClass();
        }
        resolveCreatePresenter();
        resolvePresenterVariable();
    }

    public static PresenterProvider inject(AppCompatActivity activity) {
        return new PresenterProvider(activity, null);
    }

    public static PresenterProvider inject(Fragment fragment) {
        return new PresenterProvider(null, fragment);
    }

    private static Application checkApplication(AppCompatActivity activity) {
        Application application = activity.getApplication();
        if (application == null) {
            throw new IllegalStateException("Your activity/fragment is not yet attached to Application. You can't request PresenterProviders before onCreate call.");
        }
        return application;
    }

    private static AppCompatActivity checkActivity(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) fragment.getActivity();
        if (activity == null) {
            throw new IllegalStateException("Can't create PresenterProviders for detached fragment");
        }
        return activity;
    }

    private static Context checkContext(Context context) {
        Context resultContent = null;
        if (context instanceof AppCompatActivity) {
            resultContent = context;
        }
        if (resultContent == null) {
            throw new IllegalStateException("Context must Activity Context");
        }
        return resultContent;
    }

    @SuppressWarnings("unchecked")
    private <P extends BasePresenter> PresenterProvider resolveCreatePresenter() {
        CreatePresenter createPresenter = clazz.getAnnotation(CreatePresenter.class);
        if (createPresenter != null) {
            Class<P>[] classes = (Class<P>[]) createPresenter.presenter();
            for (Class<P> clazz : classes) {
                String canonicalName = clazz.getCanonicalName();
                try {
                    presenterStore.put(canonicalName, clazz.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    private <P extends BasePresenter> PresenterProvider resolvePresenterVariable() {
        for (Field field : clazz.getDeclaredFields()) {
            //获取字段上的注解
            Annotation[] annotations = field.getDeclaredAnnotations();
            if (annotations.length < 1) {
                continue;
            }
            if (annotations[0] instanceof PresenterVariable) {
                String canonicalName = field.getType().getName();
                P presenterInstance = (P) presenterStore.get(canonicalName);
                if (presenterInstance != null) {
                    try {
                        field.setAccessible(true);
                        field.set(fragment != null ? fragment : activity, presenterInstance);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return this;
    }


    @SuppressWarnings("unchecked")
    public <P extends BasePresenter> P getPresenter(int index) {
        CreatePresenter createPresenter = clazz.getAnnotation(CreatePresenter.class);
        if (createPresenter == null) {
            return null;
        }
        if (createPresenter.presenter().length == 0) {
            return null;
        }
        if (index >= 0 && index < createPresenter.presenter().length) {
            String key = createPresenter.presenter()[index].getCanonicalName();
            BasePresenter presenter = presenterStore.get(key);
            if (presenter != null) {
                return (P) presenter;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public PresenterStore getPresenterStore() {
        return presenterStore;
    }
}
