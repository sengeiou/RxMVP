package com.yumore.example.surface;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.BaseActivity;
import com.yumore.utility.utility.RxBarTool;
import com.yumore.utility.utility.RxDataTool;
import com.yumore.utility.widget.RxRulerWheelView;
import com.yumore.utility.widget.RxTitle;
import com.yumore.utility.widget.wheelhorizontal.AbstractWheel;
import com.yumore.utility.widget.wheelhorizontal.ArrayWheelAdapter;
import com.yumore.utility.widget.wheelhorizontal.OnWheelClickedListener;
import com.yumore.utility.widget.wheelhorizontal.OnWheelScrollListener;
import com.yumore.utility.widget.wheelhorizontal.WheelHorizontalView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yumore
 */
public class ActivityWheelHorizontal extends BaseActivity {


    private final List<String> listYearMonth = new ArrayList<>();
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R2.id.wheelView_year_month)
    WheelHorizontalView mWheelViewYearMonth;
    @BindView(R2.id.imageView1)
    ImageView mImageView1;
    @BindView(R2.id.LinearLayout2)
    LinearLayout mLinearLayout2;
    @BindView(R2.id.wheelview)
    RxRulerWheelView mWheelview;
    @BindView(R2.id.wheelview2)
    RxRulerWheelView mWheelview2;
    @BindView(R2.id.wheelview3)
    RxRulerWheelView mWheelview3;
    @BindView(R2.id.wheelview4)
    RxRulerWheelView mWheelview4;
    @BindView(R2.id.wheelview5)
    RxRulerWheelView mWheelview5;
    @BindView(R2.id.changed_tv)
    TextView mChangedTv;
    @BindView(R2.id.selected_tv)
    TextView mSelectedTv;
    @BindView(R2.id.LinearLayout1)
    LinearLayout mLinearLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        setContentView(R.layout.activity_wheel_horizontal);
        ButterKnife.bind(this);
        initView();
        initData();

        initRulerView();
    }

    private void initRulerView() {

        mWheelview = findViewById(R2.id.wheelview);
        mWheelview2 = findViewById(R2.id.wheelview2);
        mWheelview3 = findViewById(R2.id.wheelview3);
        mWheelview4 = findViewById(R2.id.wheelview4);
        mWheelview5 = findViewById(R2.id.wheelview5);
        mSelectedTv = findViewById(R2.id.selected_tv);
        mChangedTv = findViewById(R2.id.changed_tv);

        final List<String> items = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            items.add(String.valueOf(i * 1000));
        }

        mWheelview.setItems(items);
        mWheelview.selectIndex(8);
        mWheelview.setAdditionCenterMark("???");

        List<String> items2 = new ArrayList<>();
        items2.add("??????");
        items2.add("??????");
        items2.add("??????");
        items2.add("??????");
        items2.add("??????");
        items2.add("??????");
        items2.add("??????");
        items2.add("??????");
        items2.add("??????");
        items2.add("??????");
        items2.add("?????????");
        items2.add("?????????");

        mWheelview2.setItems(items2);

        List<String> items3 = new ArrayList<>();
        items3.add("1");
        items3.add("2");
        items3.add("3");
        items3.add("5");
        items3.add("7");
        items3.add("11");
        items3.add("13");
        items3.add("17");
        items3.add("19");
        items3.add("23");
        items3.add("29");
        items3.add("31");

        mWheelview3.setItems(items3);
        mWheelview3.setAdditionCenterMark("m");

//		mWheelview4.setItems(items);
//		mWheelview4.setEnabled(false);

        mWheelview5.setItems(items);
        mWheelview5.setMinSelectableIndex(3);
        mWheelview5.setMaxSelectableIndex(items.size() - 3);

        items.remove(items.size() - 1);
        items.remove(items.size() - 2);
        items.remove(items.size() - 3);
        items.remove(items.size() - 4);

        mSelectedTv.setText(String.format("onWheelItemSelected???%1$s", ""));
        mChangedTv.setText(String.format("onWheelItemChanged???%1$s", ""));

        mWheelview5.setOnWheelItemSelectedListener(new RxRulerWheelView.OnWheelItemSelectedListener() {
            @Override
            public void onWheelItemSelected(RxRulerWheelView wheelView, int position) {
                mSelectedTv.setText(String.format("onWheelItemSelected???%1$s", wheelView.getItems().get(position)));
            }

            @Override
            public void onWheelItemChanged(RxRulerWheelView wheelView, int position) {
                mChangedTv.setText(String.format("onWheelItemChanged???%1$s", wheelView.getItems().get(position)));
            }
        });

        mWheelview4.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWheelview4.setItems(items);
            }
        }, 3000);
    }

    private void initView() {
        mRxTitle.setLeftFinish(baseActivity);
    }

    private void initData() {
        // TODO Auto-generated method stub
        listYearMonth.clear();
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        for (int i = calendar.get(Calendar.YEAR) - 3; i <= calendar.get(Calendar.YEAR) + 2; i++) {
            for (int j = 1; j <= 12; j++) {
                listYearMonth.add(i + "???" + j + "???");
            }
        }
        String[] arr = listYearMonth.toArray(new String[listYearMonth.size()]);
        int CurrentIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(calendar.get(Calendar.YEAR) + "???" + (calendar.get(Calendar.MONTH) + 1) + "???")) {
                CurrentIndex = i;
                break;
            }
        }

        ArrayWheelAdapter<String> ampmAdapter = new ArrayWheelAdapter<String>(
                this, arr);
        ampmAdapter.setItemResource(R.layout.item_wheel_year_month);
        ampmAdapter.setItemTextResource(R2.id.tv_year);
        mWheelViewYearMonth.setViewAdapter(ampmAdapter);
        // set current time
        mWheelViewYearMonth.setCurrentItem(CurrentIndex);

        mWheelViewYearMonth.addScrollingListener(new OnWheelScrollListener() {
            String before;
            String behind;

            @Override
            public void onScrollingStarted(AbstractWheel wheel) {
                before = listYearMonth.get(wheel.getCurrentItem());
            }

            @Override
            public void onScrollingFinished(AbstractWheel wheel) {
                behind = listYearMonth.get(wheel.getCurrentItem());
                Log.v("addScrollingListener", "listYearMonth:" + listYearMonth.get(wheel.getCurrentItem()));
                if (!before.equals(behind)) {
                    int year = RxDataTool.stringToInt(listYearMonth.get(
                            wheel.getCurrentItem()).substring(0, 4));
                    int month = RxDataTool.stringToInt(listYearMonth.get(
                            wheel.getCurrentItem()).substring(5, 6));
                    //initBarChart(VonUtil.getDaysByYearMonth(year, month));
                }
            }
        });
        mWheelViewYearMonth.addClickingListener(new OnWheelClickedListener() {

            @Override
            public void onItemClicked(AbstractWheel wheel, int itemIndex) {
                Log.v("addScrollingListener", "listYearMonth:" + listYearMonth.get(itemIndex));
                mWheelViewYearMonth.setCurrentItem(itemIndex, true);
                /*
                 * int year =
                 * VonUtil.StringToInt(listYearMonth.get(itemIndex)
                 * .substring(0, 4)); int month =
                 * VonUtil.StringToInt(listYearMonth
                 * .get(itemIndex).substring(5, 6));
                 * initBarChart(VonUtil.getDaysByYearMonth(year, month));
                 */
            }
        });
    }
}
