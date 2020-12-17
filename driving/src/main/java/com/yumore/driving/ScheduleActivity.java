package com.yumore.driving;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.common.api.GoogleApiClient;
import com.yumore.driving.adapter.AnimalsHeadersAdapter;
import com.yumore.driving.bean.CourseSheetInfo;
import com.yumore.driving.listener.RecyclerItemClickListener;
import com.yumore.sticky.StickyRecyclerHeadersDecoration;
import com.yumore.sticky.StickyRecyclerHeadersTouchListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author nathaniel
 */
public class ScheduleActivity extends AppCompatActivity implements AnimalsHeadersAdapter.OnCourseClickListener {

    private GoogleApiClient client;
    private RecyclerView recyclerView;
    private AnimalsHeadersAdapter animalsHeadersAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        recyclerView = findViewById(R.id.recyclerview);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        animalsHeadersAdapter = new AnimalsHeadersAdapter(this);


        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(animalsHeadersAdapter);
        recyclerView.addItemDecoration(headersDecor);

        // Add decoration for dividers between list items
//        recyclerView.addItemDecoration(new DividerDecoration(this));


        // Add touch listeners
        StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(recyclerView, headersDecor);
        touchListener.setOnHeaderClickListener((header, position, headerId) -> {
//                        Toast.makeText(CourseActivity.this, "Header position: " + position + ", id: " + headerId,
//                                Toast.LENGTH_SHORT).show();
        });
        recyclerView.addOnItemTouchListener(touchListener);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, (view, position) -> {

        }));
        animalsHeadersAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });


        CourseSheetInfo courseSheet = getCourseSheet();
        List<CourseSheetInfo.DataBean.ScheduleBean> data = courseSheet.getData().getData();
        animalsHeadersAdapter.setData(data);

        animalsHeadersAdapter.setOnCourseListener(this);
        recyclerView.setAdapter(animalsHeadersAdapter);


    }


    private CourseSheetInfo getCourseSheet() {
        try {
            InputStream inputStream = getAssets().open("course.json");
            return JSON.parseObject(inputStream2String(inputStream), CourseSheetInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("data.size=", e.toString());
        }
        return null;
    }

    public String inputStream2String(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int index = -1;
        while ((index = inputStream.read()) != -1) {
            byteArrayOutputStream.write(index);
        }
        return byteArrayOutputStream.toString();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }


    @Override
    public void onItemClick(AnimalsHeadersAdapter.CourseViewHolder courseViewHolder, CourseSheetInfo.DataBean.ScheduleBean.DetailBean detailBean, int groupPosition, int childPosition) {
        //选课监听
        CourseSheetInfo.DataBean.ScheduleBean.DetailBean.SubDetailBean subDetailBean = detailBean.getData().get(childPosition);

        String s = subDetailBean.getText2() + subDetailBean.getText();
        Toast.makeText(ScheduleActivity.this, "groupPosition: " + groupPosition + ", childPosition: " + childPosition + ",s=" + s,
                Toast.LENGTH_SHORT).show();
    }
}
