package com.yumore.common.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.yumore.common.helper.InitializeHelper;


/**
 * apply与commit异同：
 * 1. apply没有返回值而commit返回boolean表明修改是否提交成功
 * 2. apply是将修改数据原子提交到内存, 而后异步真正提交到硬件磁盘, 而commit是同步的提交到硬件磁盘，
 * 因此，在多个并发的提交commit的时候，他们会等待正在处理的commit保存到磁盘后在操作，从而降低了效率。
 * 而apply只是原子的提交到内容，后面有调用apply的函数的将会直接覆盖前面的内存数据，这样从一定程度上提高了很多效率。
 * 3. apply方法不会提示任何失败的提示。
 * 由于在一个进程中，sharedPreference是单实例，一般不会出现并发冲突，
 * 如果对提交的结果不关心的话，建议使用apply，当然需要确保提交成功且有后续操作的话，还是需要用commit的。
 *
 * @author Nathaniel
 * @version v1.0.0
 */
public abstract class SharedPreferencesUtils implements InitializeHelper {
    private static final String DEFAULT_SHARED_PREFERENCES_NAME = "config.sdf";
    protected SharedPreferences.Editor editor;
    protected SharedPreferences sharedPreferences;

    /**
     * 设置Module下的SharedPreferences的文件名
     *
     * @return SharedPreferences的文件名
     */
    protected abstract String getSharedPreferencesName();

    @SuppressLint("CommitPrefEdits")
    @Override
    public void initialize(Context context) {
        if (TextUtils.isEmpty(getSharedPreferencesName())) {
            sharedPreferences = context.getSharedPreferences(DEFAULT_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        } else {
            sharedPreferences = context.getSharedPreferences(getSharedPreferencesName(), Context.MODE_PRIVATE);
        }
        editor = sharedPreferences.edit();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value).apply();
    }

    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value).apply();
    }

    public String getString(final String key, final String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void putString(String key, String value) {
        editor.putString(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    public void remove(String key) {
        editor.remove(key).apply();
    }

    public void remove(String... keys) {
        if (keys != null && keys.length > 0) {
            for (String key : keys) {
                editor.remove(key);
            }
            editor.apply();
        }
    }

    public boolean hasKey(String key) {
        return sharedPreferences.contains(key);
    }

    public boolean hasKey(String... keys) {
        boolean flag = true;
        if (keys != null && keys.length > 0) {
            for (String key : keys) {
                flag = flag && sharedPreferences.contains(key);
            }
        }
        return flag;
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}