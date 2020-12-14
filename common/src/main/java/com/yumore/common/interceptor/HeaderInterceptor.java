package com.yumore.common.interceptor;

import com.yumore.common.utility.EmptyUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Nathaniel
 * @date 2019/6/9 - 9:41
 */
public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request oldRequest = chain.request();
        HttpUrl.Builder builder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host());
        if (!EmptyUtils.isEmpty(getHeaders())) {
            Set<String> stringSet = getHeaders().keySet();
            for (String key : stringSet) {
                builder.addQueryParameter(key, getHeaders().get(key));
            }
        }
        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(builder.build())
                .build();
        return chain.proceed(newRequest);
    }

    private Map<String, String> getHeaders() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("key", "vaule");
        return stringMap;
    }
}
