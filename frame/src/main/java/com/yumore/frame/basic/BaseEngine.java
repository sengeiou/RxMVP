package com.yumore.frame.basic;

import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.yumore.frame.helper.EngineHelper;
import com.yumore.frame.utility.LoggerUtils;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * BaseEngine
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 18-5-23 - 下午3:41
 */
public class BaseEngine implements EngineHelper {
    private static final String TAG = BaseEngine.class.getSimpleName();
    private volatile static Retrofit retrofit = null;
    private Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
    private OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

    public BaseEngine(String baseUrl) {
        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()
                ))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpBuilder.addInterceptor(getLoggerInterceptor()).build())
                .baseUrl(baseUrl);
    }

    @Override
    public Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (BaseEngine.class) {
                if (retrofit == null) {
                    retrofit = retrofitBuilder.build();
                }
            }
        }
        return retrofit;

    }


    @Override
    public OkHttpClient.Builder setInterceptor(Interceptor interceptor) {
        return httpBuilder.addInterceptor(interceptor);
    }

    @Override
    public Retrofit.Builder setConverterFactory(Converter.Factory factory) {
        return retrofitBuilder.addConverterFactory(factory);
    }

    /**
     * 设置日志级别
     *
     * @return HttpLoggingInterceptor
     */
    private HttpLoggingInterceptor getLoggerInterceptor() {
        // 请求/响应行 + 头 + 体
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                LoggerUtils.e(TAG, message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}
