package com.yumore.answer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import com.alibaba.fastjson.JSON;
import com.google.android.gms.common.api.GoogleApiClient;
import com.yumore.answer.adapter.AnimalsHeadersAdapter;
import com.yumore.answer.bean.CourseSheetInfo;
import com.yumore.answer.listener.RecyclerItemClickListener;
import com.yumore.sticky.StickyRecyclerHeadersDecoration;
import com.yumore.sticky.StickyRecyclerHeadersTouchListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CourseActivity extends AppCompatActivity implements AnimalsHeadersAdapter.OnCourseClickListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private RecyclerView recyclerView;
    private AnimalsHeadersAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);


        recyclerView = findViewById(R.id.recyclerview);



        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AnimalsHeadersAdapter(this);


        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        recyclerView.addItemDecoration(headersDecor);

        // Add decoration for dividers between list items
//        recyclerView.addItemDecoration(new DividerDecoration(this));


        // Add touch listeners
        StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(recyclerView, headersDecor);
        touchListener.setOnHeaderClickListener(
                new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId) {
//                        Toast.makeText(CourseActivity.this, "Header position: " + position + ", id: " + headerId,
//                                Toast.LENGTH_SHORT).show();
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        }));
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });



        CourseSheetInfo courseSheet = getCourseSheet();
        List<CourseSheetInfo.DataBean.ScheduleBean> data = courseSheet.getData().getData();
        adapter.setData(data);

        adapter.setOnCourseListener(this);
        recyclerView.setAdapter(adapter);


    }


    private CourseSheetInfo getCourseSheet() {

        try {
            InputStream in = getAssets().open("course.json");
            CourseSheetInfo courseSheetInfo = JSON.parseObject(inputStream2String(in), CourseSheetInfo.class);

            return courseSheetInfo;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("data.size=", e.toString());
        }

        return null;
    }

    public String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
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

       String s=  subDetailBean.getText2()+subDetailBean.getText();
        Toast.makeText(CourseActivity.this, "groupPosition: " + groupPosition + ", childPosition: " + childPosition+",s="+s,
                Toast.LENGTH_SHORT).show();
    }
}
