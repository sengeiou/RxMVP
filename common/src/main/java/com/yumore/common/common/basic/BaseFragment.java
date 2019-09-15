package com.yumore.common.common.basic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.yumore.common.R;
import com.yumore.common.mvp.*;
import com.yumore.common.utility.EmptyUtils;

/**
 * @author Nathaniel
 */
public abstract class BaseFragment<P extends BaseContract> extends Fragment implements BaseView {
    protected P presenter;
    protected boolean withoutNext;
    protected ViewGroup viewGroup;
    protected LayoutInflater layoutInflater;
    /**
     * 是否初始化完成
     */
    protected boolean prepared;
    /**
     * 是否可见
     */
    protected boolean visible;
    protected Activity activity;
    private Context context;
    private View rootView;
    private boolean viewCreated;
    private boolean viewVisible;
    private boolean firstLoaded = true;
    private Unbinder unbinder;
    private AlertDialog alertDialog;
    private PresenterProvider presenterProvider;
    private PresenterDispatch presenterDispatch;
    private Toast toast;

    @Override
    public void onAttach(Context context) {
        activity = (Activity) context;
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        beforeInit(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.viewGroup = container;
        // 如果当前view部位空则需要清除原有的子view
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(getLayoutId(), container, false);
            activity = getActivity();
            context = activity;
            this.layoutInflater = inflater;
        }
        // 初始化
        presenter = initPresenter();
        if (null != presenter) {
            presenter.attachView(this);
        }
        // initialize(); 并不在这初始化view
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenterProvider = PresenterProvider.inject(this);
        presenterDispatch = new PresenterDispatch(presenterProvider);

        presenterDispatch.attachView(this);
        presenterDispatch.onCreatePresenter(savedInstanceState);
        prepared = true;
        initialize();
        lazyLoad();
        viewCreated = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewVisible) {
            visibleToUser();
        }
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.detachView();
        }
        if (null != unbinder) {
            unbinder.unbind();
            unbinder = null;
        }
        viewCreated = false;
        presenterDispatch.detachView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.activity = null;
        this.context = null;
    }

    /**
     * 初始化presenter
     *
     * @return presenter
     */
    protected abstract P initPresenter();

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        presenterDispatch.onSaveInstanceState(outState);
    }

    protected BasePresenter getPresenter() {
        return presenterProvider.getPresenter(0);
    }

    public PresenterProvider getPresenterProviders() {
        return presenterProvider;
    }

    @Override
    public void beforeInit(Bundle savedInstanceState) {

    }

    public View findViewById(@IdRes int id) {
        View view;
        if (rootView != null) {
            view = rootView.findViewById(id);
            return view;
        }
        return null;
    }

    /**
     * 懒加载
     */
    private void lazyLoad() {
        if (!prepared || !visible) {
            return;
        }
        lazyLoadData();
        prepared = false;
    }

    /**
     * 懒加载
     */
    protected void lazyLoadData() {

    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {

    }

    @Override
    public void initialize() {
        initView();
        loadData();
        bindView();
    }

    @Override
    public void initView() {
        if (null == unbinder) {
            unbinder = ButterKnife.bind(this, rootView);
        }
    }

    @Override
    public void withoutMore(boolean withoutMore) {
        this.withoutNext = withoutMore;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        viewVisible = isVisibleToUser;
        if (isVisibleToUser && viewCreated) {
            visibleToUser();
        }
        if (getUserVisibleHint()) {
            visible = true;
            onVisible();
        } else {
            visible = false;
            onInvisible();
        }
    }

    @Override
    public Context getContext() {
        return context;
    }

    protected boolean isFirstLoad() {
        return firstLoaded;
    }

    protected void visibleToUser() {
        if (firstLoaded) {
            firstLoaded = false;
        }
    }

    @Override
    public void showMessage(@NonNull String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.common_toast_layout, null);
        if (EmptyUtils.isObjectEmpty(toast)) {
            toast = new Toast(context);
        }
        TextView textView = view.findViewById(R.id.common_toast_message);
        textView.setText(message);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    protected void showMessage(int resId) {
        String message = getContext().getResources().getString(resId);
        showMessage(message);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void showLoading(String message) {
        dismissLoading();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.common_loading_dialog, null);
        TextView textView = view.findViewById(R.id.loading_dialog_message);
        if (!TextUtils.isEmpty(message)) {
            textView.setText(message);
        }
        alertDialog = new AlertDialog.Builder(getContext(), R.style.CustomDialog)
                .setCancelable(false)
                .setView(view)
                .create();
        alertDialog.show();
        Window dialogWindow = alertDialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.alpha = 0.75f;
            dialogWindow.setAttributes(layoutParams);
        }
    }

    @Override
    public void dismissLoading() {
        if (null != alertDialog) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    public Fragment getFragment() {
        return this;
    }
}
