package com.yumore.frame.sample.surface.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yumore.frame.R;
import com.yumore.frame.library.basic.BaseActivity;
import com.yumore.frame.sample.adapter.TestAdapter;
import com.yumore.frame.sample.entity.Story;
import com.yumore.frame.sample.mvp.presenter.TestPresenter;
import com.yumore.frame.sample.mvp.view.TestView;

import java.util.ArrayList;
import java.util.List;

/**
 * TestActivity
 *
 * @author Nathaniel
 *         nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/21 - 17:22
 */
public class TestActivity extends BaseActivity<TestPresenter> implements TestView {
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
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView() {
        RecyclerView recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
