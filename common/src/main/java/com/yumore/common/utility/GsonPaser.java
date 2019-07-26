package com.yumore.common.utility;

import com.google.gson.Gson;
import com.yumore.common.common.utility.EmptyUtils;
import com.yumore.common.entity.BaseEntity;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Nathaniel
 * @date 18-7-24-下午4:00
 */
public class GsonPaser {
    public static <T> BaseEntity<T> fromJsonObject(ResponseBody responseBody, Class<T> clazz) throws IOException {
        byte[] bytes = responseBody.bytes();
        String string = com.yumore.common.common.utility.EmptyUtils.isObjectEmpty(bytes) ? "" : new String(bytes);
        Type type = new ParameterizedTypeImpl(BaseEntity.class, new Class[]{clazz});
        return new Gson().fromJson(string, type);
    }

    public static <T> BaseEntity<List<T>> fromJsonArray(ResponseBody responseBody, Class<T> clazz) throws IOException {
        byte[] bytes = responseBody.bytes();
        String string = EmptyUtils.isObjectEmpty(bytes) ? "" : new String(bytes);
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
        Type type = new ParameterizedTypeImpl(BaseEntity.class, new Type[]{listType});
        return new Gson().fromJson(string, type);
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class clazz;
        private final Type[] types;

        ParameterizedTypeImpl(Class clazz, Type[] types) {
            this.clazz = clazz;
            this.types = types == null ? new Type[0] : types;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return types;
        }

        @Override
        public Type getRawType() {
            return clazz;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
