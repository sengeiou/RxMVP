package com.yumore.provider;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author nathaniel
 * @version v1.0
 * @datetime 9/8/20-2:55 PM
 */
public class SingletonUtils {
    private static final ConcurrentMap<Class<?>, Object> INSTANCE_MAP = new ConcurrentHashMap<>();

    private SingletonUtils() {
        throw new IllegalStateException("You can't initialized me.");
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSingleton(Class<T> clazz) {
        Object object = INSTANCE_MAP.get(clazz);
        if (EmptyUtils.isEmpty(object)) {
            synchronized (INSTANCE_MAP) {
                try {
                    object = clazz.newInstance();
                    INSTANCE_MAP.put(clazz, object);
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        return (T) object;
    }
}
