package com.yumore.sample.surface.fragment;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.yumore.common.basic.BaseFragment;
import com.yumore.sample.R;
import com.yumore.sample.adapter.TestAdapter;
import com.yumore.sample.entity.Story;
import com.yumore.sample.presenter.TestPresenter;
import com.yumore.sample.view.TestView;

import java.util.ArrayList;
import java.util.List;

/**
 * TestFragment
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/7/1 - 14:15
 */
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
        return R.layout.common_list_layout;
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
