package com.yumore.frame.sample.surface.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yumore.frame.R;
import com.yumore.frame.library.basic.BaseFragment;
import com.yumore.frame.sample.adapter.TestAdapter;
import com.yumore.frame.sample.entity.Story;
import com.yumore.frame.sample.mvp.presenter.TestPresenter;
import com.yumore.frame.sample.mvp.view.TestView;

import java.util.ArrayList;
import java.util.List;


public class TestFragment extends BaseFragment<TestPresenter> implements TestView {
    private List<Story> list = new ArrayList<>();
    private TestAdapter adapter;

    @Override
    public TestPresenter initPresenter() {
        return new TestPresenter();
    }

    @Override
    public void setData(List<Story> dataList) {
        list.addAll(dataList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void beforeInit() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView() {
        RecyclerView recyclerView = getRootView().findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TestAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadData() {
        presenter.getData();
    }

    @Override
    public void bindView() {

    }
}
