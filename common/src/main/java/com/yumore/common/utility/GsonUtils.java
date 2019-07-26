package com.yumore.common.utility;

import com.google.gson.Gson;
import com.yumore.common.entity.ResultData;
import com.yumore.common.entity.StatusEntity;

public class GsonUtils {
    private static Gson gson = new Gson();

    /**
     * 解析json数据   T:泛型
     *
     * @param jsonString json
     * @param clazz clazz
     * @return T
     */
    public static <T> T processJson(String jsonString, Class<T> clazz) {
        return gson.fromJson(jsonString, clazz);
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static <T> boolean buildDataResult(ResultData<T> resultData, String jsonString, StatusEntity statusEntity) {
        if (resultData == null) {
            return false;
        }
        if (resultData.getCode() == 0) {
            return true;
        } else {
            statusEntity.setCode(resultData.getCode());
        }
        return false;
    }

    public static <T> ResultData<T> fromJsonToData(String resJson, java.lang.reflect.Type type) {
        try {
            return gson.fromJson(resJson, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
