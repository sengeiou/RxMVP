package com.yumore.answer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.yumore.answer.adapter.LayoutAdapter;
import com.yumore.answer.adapter.TopicAdapter;
import com.yumore.answer.bean.AnwerInfo;
import com.yumore.sticky.RecyclerViewPager;
import com.yumore.sticky.SlidingUpPanelLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class OutcomeActivity extends AppCompatActivity {

    private RecyclerViewPager mRecyclerView;
    private LayoutAdapter layoutAdapter;
    private SlidingUpPanelLayout mLayout;
    private TopicAdapter topicAdapter;
    private RecyclerView recyclerView;
    private int prePosition;
    private int curPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outcome);


        initViewPager();

        initSlidingUoPanel();


        initList();

        ImageView imageView = findViewById(R.id.common_header_back_iv);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(view -> {
            finish();
        });
        TextView textView = findViewById(R.id.common_header_title_tv);
        textView.setText("考试结果");
        AnwerInfo anwerInfo = getAnwer();

        List<AnwerInfo.DataBean.SubDataBean> datas = anwerInfo.getData().getData();
        Log.i("data.size=", "" + datas.size());

        if (layoutAdapter != null) {
            layoutAdapter.setDataList(datas);
        }

        if (topicAdapter != null) {
            topicAdapter.setDataNum(datas.size());
        }

    }

    private void initList() {
        recyclerView = findViewById(R.id.list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);

        topicAdapter = new TopicAdapter(this);

        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(topicAdapter);


        topicAdapter.setOnTopicClickListener((holder, position) -> {
            curPosition = position;
            Log.i("点击了==>", position + "");
            if (mLayout != null &&
                    (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
            mRecyclerView.smoothScrollToPosition(position);

            topicAdapter.notifyCurPosition(curPosition);
            topicAdapter.notifyPrePosition(prePosition);

            prePosition = curPosition;
        });


    }

    private void initSlidingUoPanel() {
        mLayout = findViewById(R.id.sliding_layout);

        int height = getWindowManager().getDefaultDisplay().getHeight();

        LinearLayout dragView = findViewById(R.id.dragView);
        SlidingUpPanelLayout.LayoutParams params = new SlidingUpPanelLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (height * 0.8f));
        dragView.setLayoutParams(params);


        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("", "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i("", "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(view -> mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED));
    }


    protected void initViewPager() {
        mRecyclerView = findViewById(R.id.viewpager);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setSinglePageFling(true);
        mRecyclerView.setFlingFactor(0.1f);
        mRecyclerView.setTriggerOffset(0.1f);
        layoutAdapter = new LayoutAdapter(this, mRecyclerView);
        mRecyclerView.setAdapter(layoutAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLongClickable(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int i, int i2) {
//
            }
        });
        mRecyclerView.addOnPageChangedListener((oldPosition, newPosition) -> {
            Log.d("test", "oldPosition:" + oldPosition + " newPosition:" + newPosition);
            recyclerView.scrollToPosition(newPosition);

            topicAdapter.notifyCurPosition(newPosition);
            topicAdapter.notifyPrePosition(oldPosition);

            Log.i("DDD", newPosition + "");

        });


        mRecyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {


        });
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    public String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    private AnwerInfo getAnwer() {

        try {
            InputStream in = getAssets().open("answer.json");
            AnwerInfo anwerInfo = JSON.parseObject(inputStream2String(in), AnwerInfo.class);

            return anwerInfo;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("data.size=", e.toString());
        }

        return null;
    }
}
