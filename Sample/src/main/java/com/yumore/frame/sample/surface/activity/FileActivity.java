package com.yumore.frame.sample.surface.activity;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yumore.frame.R;
import com.yumore.frame.library.basic.BaseActivity;
import com.yumore.frame.library.basic.BaseContract;
import com.yumore.frame.library.callback.UiProgressCallback;
import com.yumore.frame.library.listener.OnProgressListener;
import com.yumore.frame.library.utility.OkHttpUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Nathaniel
 * @date 2018/3/29-20:08
 */

public class FileActivity extends BaseActivity {
    private static final String TAG = FileActivity.class.getSimpleName();

    private static final String POST_FILE_URL = "http://192.168.31.46:8080/UploadFileDemo/MutilUploadServlet";
    private static final String DOWNLOAD_TEST_URL = "http://oh0vbg8a6.bkt.clouddn.com/app-debug.apk";

    private static final String STORE_DOWNLOAD_FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "download2.apk";
    private ProgressBar uploadProgress, downloadProgress;
    private TextView upload, download;

    @Override
    public int getLayoutId() {
        return R.layout.file_activity;
    }

    @Override
    public void initView() {
        uploadProgress = findViewById(R.id.upload_progress);
        downloadProgress = findViewById(R.id.download_progress);
        upload = findViewById(R.id.tv_upload_progress);
        download = findViewById(R.id.tv_download_progress);

    }

    private void upload() {
        //这个是非ui线程回调，不可直接操作UI
        final OnProgressListener progressListener = new OnProgressListener() {
            @Override
            public void onProgress(long bytesWrite, long contentLength, boolean done) {
                Log.i(TAG, "bytesWrite:" + bytesWrite);
                Log.i(TAG, "contentLength" + contentLength);
                Log.i(TAG, (100 * bytesWrite) / contentLength + " % done ");
                Log.i(TAG, "done:" + done);
                Log.i(TAG, "================================");
            }
        };


        //这个是ui线程回调，可直接操作UI
        UiProgressCallback uiProgressCallback = new UiProgressCallback() {
            @Override
            public void onProgress(long bytesWrite, long contentLength, boolean done) {
                Log.i(TAG, "bytesWrite:" + bytesWrite);
                Log.i(TAG, "contentLength" + contentLength);
                Log.i(TAG, (100 * bytesWrite) / contentLength + " % done ");
                Log.i(TAG, "done:" + done);
                Log.i(TAG, "================================");
                //ui层回调,设置当前上传的进度值
                int progress = (int) ((100 * bytesWrite) / contentLength);
                uploadProgress.setProgress(progress);
                upload.setText("上传进度值：" + progress + "%");
            }

            @Override
            protected void onUiProgress(long currentBytes, long contentLength, boolean done) {

            }

            @Override
            protected void onUiStart(long currentBytes, long contentLength, boolean done) {

            }

            @Override
            protected void onUiPause(long currentBytes, long contentLength, boolean done) {
                Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onUiFinish(long currentBytes, long contentLength, boolean done) {
                Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
            }
        };


        OkHttpUtils.doPostRequest(POST_FILE_URL, initUploadFile(), uiProgressCallback, new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FileActivity.this, "上传失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "success---->" + response.body().string());
            }
        });

    }


    private void download() {
        final OnProgressListener onProgressListener = new OnProgressListener() {
            @Override
            public void onProgress(long bytesRead, long contentLength, boolean done) {
                Log.i(TAG, "bytesRead:" + bytesRead);
                Log.i(TAG, "contentLength:" + contentLength);
                Log.i(TAG, "done:" + done);
                if (contentLength != -1) {
                    Log.i(TAG, (100 * bytesRead) / contentLength + "% done");
                }
            }
        };


        //这个是ui线程回调，可直接操作UI
        final UiProgressCallback uiProgressCallback = new UiProgressCallback() {
            @Override
            public void onUiProgress(long bytesRead, long contentLength, boolean done) {
                Log.i(TAG, "bytesRead:" + bytesRead);
                Log.i(TAG, "contentLength:" + contentLength);
                Log.i(TAG, "done:" + done);
                if (contentLength != -1) {
                    Log.i(TAG, (100 * bytesRead) / contentLength + "% done");
                }
                int progress = (int) ((100 * bytesRead) / contentLength);
                downloadProgress.setProgress(progress);
                download.setText("下载进度：" + progress + "%");
            }

            @Override
            public void onUiStart(long bytesRead, long contentLength, boolean done) {
                Toast.makeText(getApplicationContext(), "开始下载", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onUiPause(long currentBytes, long contentLength, boolean done) {

            }

            @Override
            public void onUiFinish(long bytesRead, long contentLength, boolean done) {
                Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
            }
        };

        //开启文件下载
        OkHttpUtils.downloadAndSaveFile(this, DOWNLOAD_TEST_URL, STORE_DOWNLOAD_FILE_PATH, uiProgressCallback);

    }

    private List<String> initUploadFile() {
        List<String> fileNames = new ArrayList<>();
        fileNames.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "test.txt");
        fileNames.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "bell.png");
        fileNames.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + File.separator + "kobe.mp4");
        fileNames.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + File.separator + "xinnian.mp3");
        return fileNames;
    }


    @Override
    public void loadData() {

    }

    @Override
    public void bindView() {
        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });
    }

    @Override
    public BaseContract initPresenter() {
        return null;
    }
}
