package com.cgfay.camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.cgfay.camera.engine.camera.CameraParam;
import com.cgfay.cameralibrary.R;

/**
 * 相机设置页面
 */
public class CameraSettingActivity extends AppCompatActivity {

    private RelativeLayout mLayoutSelectWatermark;
    private RelativeLayout mLayoutShowFacePoints;
    private TextView mTextFacePoints;
    private RelativeLayout mLayoutShowFps;
    private TextView mTextFps;
    /**
     * 监听事件回调
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.layout_select_watermark) {
                processSelectWatermark();
            } else if (id == R.id.layout_show_face_points) {
                CameraParam.getInstance().drawFacePoints = !CameraParam.getInstance().drawFacePoints;
                processShowFacePoints();
            } else if (id == R.id.layout_show_fps) {
                CameraParam.getInstance().showFps = !CameraParam.getInstance().showFps;
                processShowFps();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_setting);
        initView();
    }

    private void initView() {
        mLayoutSelectWatermark = findViewById(R.id.layout_select_watermark);
        mLayoutShowFacePoints = findViewById(R.id.layout_show_face_points);
        mTextFacePoints = findViewById(R.id.tv_show_face_points);
        processShowFacePoints();

        mLayoutShowFps = findViewById(R.id.layout_show_fps);
        mTextFps = findViewById(R.id.tv_show_fps);
        processShowFps();

        mLayoutSelectWatermark.setOnClickListener(mClickListener);
        mLayoutShowFacePoints.setOnClickListener(mClickListener);
        mLayoutShowFps.setOnClickListener(mClickListener);
    }

    private void processSelectWatermark() {
        Intent intent = new Intent(CameraSettingActivity.this, WatermarkActivity.class);
        startActivity(intent);
    }

    private void processShowFacePoints() {
        mTextFacePoints.setText(CameraParam.getInstance().drawFacePoints
                ? getString(R.string.show_face_points) : getString(R.string.hide_face_points));
    }

    private void processShowFps() {
        mTextFps.setText(CameraParam.getInstance().showFps
                ? getString(R.string.show_fps) : getString(R.string.hide_fps));
    }
}
