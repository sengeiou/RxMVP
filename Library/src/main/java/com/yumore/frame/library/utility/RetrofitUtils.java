package com.yumore.frame.library.utility;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    private static final int READ_TIMEOUT = 60;//读取超时时间,单位  秒
    private static final int CONN_TIMEOUT = 12;//连接超时时间,单位  秒

    private static Retrofit retrofit;

    private RetrofitUtils() {

    }

    public static Retrofit newInstence(String url) {
        retrofit = null;
        OkHttpClient client = new OkHttpClient();
        //client.setReadTimeout(READ_TIMEOUT, TimeUnit.MINUTES);
        //client.setConnectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }


}
