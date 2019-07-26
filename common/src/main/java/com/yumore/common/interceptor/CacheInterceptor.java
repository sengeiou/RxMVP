package com.yumore.common.interceptor;

import android.content.Context;
import com.yumore.common.utility.NetworkUtils;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Nathaniel
 * @date 2019/4/13 - 19:58
 */
public class CacheInterceptor implements Interceptor {
    private Context context;

    public CacheInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return response.newBuilder()
                .header("Cache-Control", "public,max-age=120")
                .build();
    }

    private Response shoudInterceptor(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtils.isConnected(context)) {
            int maxStale = 4 * 7 * 24 * 60;
            CacheControl tempCacheControl = new CacheControl.Builder()
                    .onlyIfCached()
                    .maxStale(maxStale, TimeUnit.SECONDS)
                    .build();
            request = request.newBuilder()
                    .cacheControl(tempCacheControl)
                    .build();
        }
        return chain.proceed(request);
    }
}
