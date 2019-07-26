package com.yumore.common.reflect;

import android.net.ParseException;
import com.google.gson.JsonParseException;
import com.yumore.common.common.utility.EmptyUtils;
import com.yumore.common.common.utility.LoggerUtils;
import io.reactivex.functions.Consumer;
import org.json.JSONException;
import retrofit2.HttpException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author Nathaniel
 * @date 2019/3/17 - 16:15
 */
public abstract class RxThrowable implements Consumer<Throwable> {
    private static final String TAG = RxThrowable.class.getSimpleName();

    @Override
    public void accept(Throwable throwable) {
        throwable.printStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        RxError rxError = new RxError();
        if (throwable instanceof RxException) {
            rxError = ((RxException) throwable).getRxError();
            onFailure(rxError);
            return;
        } else if (throwable instanceof HttpException || throwable instanceof SocketTimeoutException
                || throwable instanceof ConnectException || throwable instanceof UnknownHostException) {
            stringBuilder.append("网络连接异常:");
        } else if (throwable instanceof JsonParseException || throwable instanceof JSONException || throwable instanceof ParseException) {
            stringBuilder.append("数据解析异常:");
        } else if (throwable instanceof IllegalArgumentException) {
            stringBuilder.append("参数非法异常:");
        } else if (throwable instanceof ClassCastException) {
            stringBuilder.append("数据解析类型转换异常:");
        } else {
            stringBuilder.append("未知异常:");
        }
        if (EmptyUtils.isObjectEmpty(throwable.getCause())) {
            stringBuilder.append(throwable.getMessage());
        } else {
            stringBuilder.append(throwable.getCause().getMessage());
        }
        rxError.setMessage(stringBuilder.toString());
        LoggerUtils.e(TAG, stringBuilder.toString());
        onFailure(rxError);
    }

    /**
     * 请求出现错误
     *
     * @param rxError rxError
     */
    public abstract void onFailure(RxError rxError);
}