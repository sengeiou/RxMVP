package com.yumore.common.reflect;

import com.yumore.common.entity.ResultData;
import com.yumore.common.utility.EmptyUtils;
import com.yumore.common.utility.GsonUtils;
import com.yumore.common.utility.ZipUtils;

import io.reactivex.functions.Function;

/**
 * Created by 8000m on 2017/5/2.
 */

public class RxResponse<T> implements Function<ResultData<T>, T> {

    private final Class<T> clazz;

    public RxResponse(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <T> T parseResData(ResultData<T> res, Class<T> clazz) {
        T data = null;
        if ("gzip".equals(res.getCompress())) {
            data = zipJsonToData(res.getJson(), clazz);
        } else {
            data = res.getData();
        }
        return data;
    }

    /**
     * json字符串先base64解密，再zip解压，再转换成对象T输出
     *
     * @param jsonString jsonString
     * @param clazz      clazz
     * @param <T>        T
     * @return T
     */
    public static <T> T zipJsonToData(String jsonString, Class<T> clazz) {
        T data = null;
        if (EmptyUtils.isEmpty(jsonString)) {
            long timeStart = System.currentTimeMillis();
            long size1 = jsonString.length();
            String decompressed = ZipUtils.gunzip(jsonString);
            if (EmptyUtils.isEmpty(decompressed)) {
                long size2 = decompressed.length();
                data = GsonUtils.processJson(decompressed, clazz);
                long timeEnd = System.currentTimeMillis();
                long timeDur = timeEnd - timeStart;
            }
        }
        return data;
    }

    @Override
    public T apply(ResultData<T> resultData) throws Exception {
        if (resultData.getCode() != 0) {
            throw new RxException(resultData.getCode());
        }
        T t = parseResData(resultData, clazz);
        if (EmptyUtils.isEmpty(t)) {
            throw new RxException(999);
        }
        return t;
    }
}
