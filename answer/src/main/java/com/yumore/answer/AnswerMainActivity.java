package com.yumore.answer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author nathaniel
 */
public class AnswerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_main);
        ImageView imageView = findViewById(R.id.common_header_back_iv);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener((view) -> {
            finish();
        });

        TextView textView = findViewById(R.id.common_header_title_tv);
        textView.setText("Answer");
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
