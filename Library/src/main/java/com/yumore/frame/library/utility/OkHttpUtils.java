package com.yumore.frame.library.utility;


import android.app.Activity;
import android.widget.Toast;

import com.yumore.frame.library.callback.UiProgressCallback;
import com.yumore.frame.library.helper.ProgressHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OKHttpUtils
 *
 * @author Nathaniel
 *         nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/29 - 18:23
 */

public class OkHttpUtils {

    private static OkHttpClient client;

    public synchronized static OkHttpClient getOkHttpClientInstance() {
        if (client == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS);

            client = builder.build();
        }
        return client;
    }

    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            //* exe,所有的可执行程序
            contentType = "application/octet-stream";
        }
        return contentType;
    }

    private static Request getRequest(String url, List<String> fileNames) {
        Request.Builder builder = new Request.Builder();
        // 设置请求的标记为url，可在取消时使用
        builder.url(url)
                .post(getRequestBody(fileNames))
                .tag(url);
        return builder.build();
    }

    private static Request getRequest(String url, List<String> fileNames, UiProgressCallback uiProgressCallback) {
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(ProgressHelper.addProgressRequestListener(
                        OkHttpUtils.getRequestBody(fileNames),
                        uiProgressCallback));
        return builder.build();
    }

    private static Request getRequest(String url, HashMap<String, String> map) {
        Request.Builder builder = new Request.Builder();
        // 设置请求的标记为url，可在取消时使用
        builder.url(url)
                .post(getRequestBody(map))
                .tag(url);
        return builder.build();
    }

    private static Request getRequest(String url, HashMap<String, String> map, List<String> fileNames) {
        Request.Builder builder = new Request.Builder();
        // 设置请求的标记为url，可在取消时使用
        builder.url(url)
                .post(getRequestBody(map, fileNames))
                .tag(url);
        return builder.build();
    }

    private static Request getRequest(String downloadUrl) {
        Request.Builder builder = new Request.Builder();
        builder.url(downloadUrl).tag(downloadUrl);
        return builder.build();
    }

    private static RequestBody getRequestBody(HashMap<String, String> map) {
        FormBody.Builder builder = new FormBody.Builder();
        for (HashMap.Entry<String, String> entry : map.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    private static RequestBody getRequestBody(HashMap<String, String> map, List<String> fileNames) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (HashMap.Entry<String, String> entry : map.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        for (int i = 0; i < fileNames.size(); i++) {
            File file = new File(fileNames.get(i));
            String fileType = getMimeType(file.getName());
            builder.addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse(fileType), file));
        }
        return builder.build();
    }

    private static RequestBody getRequestBody(List<String> fileNames) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < fileNames.size(); i++) {
            File file = new File(fileNames.get(i));
            String fileType = getMimeType(file.getName());
            builder.addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse(fileType), file));
        }
        return builder.build();
    }

    public static void doPostRequest(String url, List<String> fileNames, UiProgressCallback uiProgressCallback, Callback callback) {
        Call call = getOkHttpClientInstance().newCall(getRequest(url, fileNames, uiProgressCallback));
        call.enqueue(callback);
    }

    public static void doPostRequest(String url, List<String> fileNames, Callback callback) {
        Call call = getOkHttpClientInstance().newCall(getRequest(url, fileNames));
        call.enqueue(callback);
    }

    public static void doPostRequest(String url, HashMap<String, String> map, Callback callback) {
        Call call = getOkHttpClientInstance().newCall(getRequest(url, map));
        call.enqueue(callback);
    }

    public static void doPostRequest(String url, HashMap<String, String> map, List<String> fileNames, Callback callback) {
        Call call = getOkHttpClientInstance().newCall(getRequest(url, map, fileNames));
        call.enqueue(callback);
    }


    public static void downloadAndSaveFile(final Activity activity, String downloadUrl, final String savePath, UiProgressCallback uiProgressResponseListener) {
        ProgressHelper.addProgressResponseListener(OkHttpUtils.getOkHttpClientInstance(), uiProgressResponseListener, savePath)
                .newCall(getRequest(downloadUrl))
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "下载错误" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        OkHttpUtils.saveDownloadFile(response, savePath);
                    }
                });
    }

    private static void saveDownloadFile(Response response, String savePath) throws IOException {
        InputStream inputStream = getInputStreamFromResponse(response);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        FileOutputStream fileOutputStream = new FileOutputStream(savePath);
        byte[] data = new byte[10 * 1024];
        int len;
        while ((len = bufferedInputStream.read(data)) != -1) {
            fileOutputStream.write(data, 0, len);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        bufferedInputStream.close();
    }

    @SuppressWarnings("ConstantConditions")
    public static String getString(Response response) throws IOException {
        if (response != null && response.isSuccessful()) {
            return response.body().string();
        }
        return null;
    }

    public static byte[] getBytesFromResponse(Response response) throws IOException {
        if (response != null && response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                return responseBody.bytes();
            }
        }
        return null;
    }

    public static InputStream getInputStreamFromResponse(Response response) throws IOException {
        if (response != null && response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                return responseBody.byteStream();
            }
        }
        return null;
    }

    @SuppressWarnings("SynchronizationOnGetClass")
    public static void cancelCallsWithTag(Object tag) {
        if (null == tag) {
            return;
        }

        synchronized (client.dispatcher().getClass()) {
            for (Call call : client.dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }

            for (Call call : client.dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        }
    }
}
