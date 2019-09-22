package com.yumore.answer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.yumore.answer.adapter.LayoutAdapter;
import com.yumore.answer.adapter.TopicAdapter;
import com.yumore.sticky.RecyclerViewPager;
import com.yumore.sticky.SlidingUpPanelLayout;

public class AnswerMainActivity extends AppCompatActivity {

    private RecyclerViewPager mRecyclerView;
    private LayoutAdapter layoutAdapter;
    private SlidingUpPanelLayout mLayout;
    private TopicAdapter topicAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_main);


    }

    public void zeroClick(View view) {

        startActivity(new Intent(this, ReadActivity.class));
    }

    public void oneClick(View view) {

        startActivity(new Intent(this, AnwerActivity.class));
    }

    public void twoClick(View view) {

        startActivity(new Intent(this, AnswerReadActivity.class));
    }


    public void threeClick(View view) {
        startActivity(new Intent(this, CourseActivity.class));

    }

}
