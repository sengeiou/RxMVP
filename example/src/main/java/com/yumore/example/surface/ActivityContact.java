package com.yumore.example.surface;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.BaseActivity;
import com.yumore.utility.entity.ModelContactCity;
import com.yumore.utility.widget.RxTitle;
import com.yumore.utility.widget.wavesidebar.ComparatorLetter;
import com.yumore.utility.widget.wavesidebar.PinnedHeaderDecoration;
import com.yumore.utility.widget.wavesidebar.WaveSideBarView;
import com.yumore.utility.widget.wavesidebar.adapter.AdapterContactCity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yumore
 */
public class ActivityContact extends BaseActivity {

    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R2.id.side_view)
    WaveSideBarView mSideBarView;
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;

    private AdapterContactCity mAdapterContactCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mRxTitle.setLeftFinish(baseActivity);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        mRecyclerView.addItemDecoration(decoration);


        new Thread(new Runnable() {
            @Override
            public void run() {
                Type listType = new TypeToken<ArrayList<ModelContactCity>>() {
                }.getType();
                Gson gson = new Gson();
                final List<ModelContactCity> list = gson.fromJson(ModelContactCity.DATA, listType);
                Collections.sort(list, new ComparatorLetter());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapterContactCity = new AdapterContactCity(baseActivity, list);
                        mRecyclerView.setAdapter(mAdapterContactCity);
                    }
                });
            }
        }).start();

        mSideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = mAdapterContactCity.getLetterPosition(letter);

                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });
    }
}
