package com.yumore.common.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Nathaniel
 * @date 2019/4/13 - 20:10
 */
public class NetworkInterceptor implements Interceptor {
    private static final int MAX_AGE = 20;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response originalResponse = chain.proceed(request);
        return originalResponse.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=" + MAX_AGE)
                .build();
    }
}
