package com.yumore.sample.surface.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yumore.frame.basic.BaseFragment;
import com.yumore.sample.R;
import com.yumore.sample.adapter.TestAdapter;
import com.yumore.sample.entity.Story;
import com.yumore.sample.mvp.presenter.TestPresenter;
import com.yumore.sample.mvp.view.TestView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class TestFragment extends BaseFragment<TestPresenter> implements TestView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

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
    public void loadData() {
        presenter.getData();
    }

    @Override
    public void bindView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TestAdapter(list);
        recyclerView.setAdapter(adapter);
    }
}
