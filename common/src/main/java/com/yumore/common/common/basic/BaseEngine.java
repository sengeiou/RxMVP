package com.yumore.common.common.basic;

import androidx.annotation.NonNull;
import com.google.gson.GsonBuilder;
import com.yumore.common.common.helper.EngineHelper;
import com.yumore.common.common.interceptor.RetryInterceptor;
import com.yumore.common.common.utility.EmptyUtils;
import com.yumore.common.common.utility.LoggerUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * BaseEngine
 * 因为不需要默认的interceptor
 * 所以就不重写了
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 18-5-23 - 下午3:41
 */
public abstract class BaseEngine implements EngineHelper {
    private static final String TAG = BaseEngine.class.getSimpleName();

    private OkHttpClient initHttpBuilder() {
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        httpBuilder.connectTimeout(30, TimeUnit.SECONDS);
        httpBuilder.readTimeout(30, TimeUnit.SECONDS);
        httpBuilder.writeTimeout(30, TimeUnit.SECONDS);
        if (!EmptyUtils.isObjectEmpty(getInterceptor())) {
            httpBuilder.addInterceptor(getInterceptor());
        }
        httpBuilder.retryOnConnectionFailure(true);
        // httpBuilder.addInterceptor(new SessionInterceptor());
        httpBuilder.addInterceptor(new RetryInterceptor());
        httpBuilder.addInterceptor(getLoggerInterceptor());
        return httpBuilder.build();
    }

    @NonNull
    @Override
    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(getFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(initHttpBuilder())
                .baseUrl(getBaseUrl())
                .build();
    }

    private HttpLoggingInterceptor getLoggerInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                LoggerUtils.e(TAG, message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Override
    public Converter.Factory getFactory() {
        return GsonConverterFactory.create(new GsonBuilder().setLenient().create());
    }
}
