package com.yumore.frame.sample.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class SharePreUtils {

    public static final String SHARE_NAME = "config";

    public static void put(Context mContext, String key, Object values) {
        if (values instanceof String) {
            putString(mContext, key, (String) values);
        } else if (values instanceof Integer) {
            putInt(mContext, key, (Integer) values);
        } else if (values instanceof Boolean) {
            putBoolean(mContext, key, (Boolean) values);
        } else {
            putString(mContext, key, String.valueOf(values));
        }
    }

    public static void putString(Context mContext, String key, String values) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, values).commit();
    }

    public static String getString(Context mContext, String key, String values) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, values);
    }

    public static void putBoolean(Context mContext, String key, boolean values) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, values).commit();
    }

    public static boolean getBoolean(Context mContext, String key, boolean values) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, values);
    }

    public static void putInt(Context mContext, String key, int values) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key, values).commit();
    }

    public static int getInt(Context mContext, String key, int values) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, values);
    }

    public static void deleShare(Context mContext, String key) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    public static void deleShareAll(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    public static String lookSharePre(Context context) {
        try {
            FileInputStream stream = new FileInputStream(new File("/data/data/" + context.getPackageName() + "/shared_prefs", "config.xml"));
            BufferedReader bff = new BufferedReader(new InputStreamReader(stream));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bff.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "未找到当前配置文件！";
    }
}