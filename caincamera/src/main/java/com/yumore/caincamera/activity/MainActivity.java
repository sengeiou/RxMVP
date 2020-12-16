package com.yumore.caincamera.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.yumore.caincamera.R;
import com.yumore.camera.engine.PreviewEngine;
import com.yumore.camera.engine.model.AspectRatio;
import com.yumore.camera.engine.model.GalleryType;
import com.yumore.camera.listener.OnGallerySelectedListener;
import com.yumore.camera.listener.OnPreviewCaptureListener;
import com.yumore.filter.glfilter.resource.FilterHelper;
import com.yumore.filter.glfilter.resource.MakeupHelper;
import com.yumore.filter.glfilter.resource.ResourceHelper;
import com.yumore.image.activity.ImageEditActivity;
import com.yumore.scan.engine.MediaScanEngine;
import com.yumore.scan.listener.OnCaptureListener;
import com.yumore.scan.listener.OnMediaSelectedListener;
import com.yumore.scan.loader.impl.GlideMediaLoader;
import com.yumore.scan.model.MimeType;
import com.yumore.uitls.utils.PermissionUtils;
import com.yumore.video.activity.VideoCutActivity;
import com.yumore.video.activity.VideoEditActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 0;

    private boolean mOnClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        initView();
        initResources();
    }

    private void checkPermissions() {
        PermissionUtils.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_CODE);
    }

    private void initView() {
        findViewById(R.id.btn_camera).setOnClickListener(this);
        findViewById(R.id.btn_edit_video).setOnClickListener(this);
        findViewById(R.id.btn_edit_picture).setOnClickListener(this);
        findViewById(R.id.btn_speed_record).setOnClickListener(this);
        findViewById(R.id.btn_edit_music_merge).setOnClickListener(this);
        findViewById(R.id.btn_ff_media_record).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOnClick = false;
    }

    @Override
    public void onClick(View v) {
        if (mOnClick) {
            return;
        }
        mOnClick = true;
        int id = v.getId();
        if (id == R.id.btn_camera) {
            previewCamera();
        } else if (id == R.id.btn_edit_video) {
            scanMedia(false, false, true);
        } else if (id == R.id.btn_edit_picture) {
            scanMedia(false, true, false);
        } else if (id == R.id.btn_speed_record) {
            Intent intent = new Intent(MainActivity.this, SpeedRecordActivity.class);
            startActivity(intent);
        } else if (id == R.id.btn_edit_music_merge) {
            musicMerge();
        } else if (id == R.id.btn_ff_media_record) {
            ffmpegRecord();
        }
    }

    /**
     * 初始化动态贴纸、滤镜等资源
     */
    private void initResources() {
        new Thread(() -> {
            ResourceHelper.initAssetsResource(MainActivity.this);
            FilterHelper.initAssetsFilter(MainActivity.this);
            MakeupHelper.initAssetsMakeup(MainActivity.this);
        }).start();
    }

    /**
     * 打开预览页面
     */
    private void previewCamera() {
        PreviewEngine.from(this)
                .setCameraRatio(AspectRatio.Ratio_16_9)
                .showFacePoints(false)
                .showFps(true)
                .backCamera(true)
                .setGalleryListener(new OnGallerySelectedListener() {
                    @Override
                    public void onGalleryClickListener(GalleryType type) {
                        scanMedia(type == GalleryType.ALL);
                    }
                })
                .setPreviewCaptureListener(new OnPreviewCaptureListener() {
                    @Override
                    public void onMediaSelectedListener(String path, GalleryType type) {
                        if (type == GalleryType.PICTURE) {
                            Intent intent = new Intent(MainActivity.this, ImageEditActivity.class);
                            intent.putExtra(ImageEditActivity.IMAGE_PATH, path);
                            intent.putExtra(ImageEditActivity.DELETE_INPUT_FILE, true);
                            startActivity(intent);
                        } else if (type == GalleryType.VIDEO) {
                            Intent intent = new Intent(MainActivity.this, VideoEditActivity.class);
                            intent.putExtra(VideoEditActivity.VIDEO_PATH, path);
                            startActivity(intent);
                        }
                    }
                })
                .startPreview();
    }

    /**
     * 扫描媒体库
     */
    private void scanMedia(boolean enableGif) {
        scanMedia(enableGif, true, true);
    }

    /**
     * 扫描媒体库
     *
     * @param enableGif
     * @param enableImage
     * @param enableVideo
     */
    private void scanMedia(boolean enableGif, boolean enableImage, boolean enableVideo) {
        MediaScanEngine.from(this)
                .setMimeTypes(MimeType.ofAll())
                .ImageLoader(new GlideMediaLoader())
                .spanCount(4)
                .showCapture(true)
                .showImage(enableImage)
                .showVideo(enableVideo)
                .enableSelectGif(enableGif)
                .setCaptureListener(new OnCaptureListener() {
                    @Override
                    public void onCapture() {
                        previewCamera();
                    }
                })
                .setMediaSelectedListener(new OnMediaSelectedListener() {
                    @Override
                    public void onSelected(List<Uri> uriList, List<String> pathList, boolean isVideo) {
                        if (isVideo) {
                            Intent intent = new Intent(MainActivity.this, VideoCutActivity.class);
                            intent.putExtra(VideoCutActivity.PATH, pathList.get(0));
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, ImageEditActivity.class);
                            intent.putExtra(ImageEditActivity.IMAGE_PATH, pathList.get(0));
                            startActivity(intent);
                        }
                    }
                })
                .scanMedia();
    }

    /**
     * 音视频混合
     */
    private void musicMerge() {
        MediaScanEngine.from(this)
                .setMimeTypes(MimeType.ofAll())
                .ImageLoader(new GlideMediaLoader())
                .spanCount(4)
                .showCapture(true)
                .showImage(false)
                .showVideo(true)
                .enableSelectGif(false)
                .setCaptureListener(new OnCaptureListener() {
                    @Override
                    public void onCapture() {
                        previewCamera();
                    }
                })
                .setMediaSelectedListener(new OnMediaSelectedListener() {
                    @Override
                    public void onSelected(List<Uri> uriList, List<String> pathList, boolean isVideo) {
                        if (isVideo) {
                            Intent intent = new Intent(MainActivity.this, MusicMergeActivity.class);
                            intent.putExtra(MusicMergeActivity.PATH, pathList.get(0));
                            startActivity(intent);
                        }
                    }
                })
                .scanMedia();
    }

    /**
     * 使用FFmpeg 录制视频
     */
    private void ffmpegRecord() {
        startActivity(new Intent(MainActivity.this, FFMediaRecordActivity.class));
    }
}
