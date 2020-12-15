package com.yumore.common.utility;

import android.annotation.SuppressLint;
import android.content.Context;

import com.yumore.common.basic.BasePreferences;

/**
 * @author Nathaniel
 * @datetime 2019/10/18 - 17:50
 */
public class PreferencesUtils extends BasePreferences {

    private static final String PREFERENCE_FILENAME = "common.sdf";
    private static final String KEY_TRACTION_ENABLED = "tractionEnable";
    private static final String KEY_INTRODUCE_ENABLED = "introduceEnable";
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;

    @SuppressLint("StaticFieldLeak")
    private static PreferencesUtils preferencesUtils;

    private PreferencesUtils(Context context) {
        initialize(context);
    }

    public static synchronized PreferencesUtils getInstance(Context context) {
        if (EmptyUtils.isEmpty(preferencesUtils)) {
            preferencesUtils = new PreferencesUtils(context);
        }
        return preferencesUtils;
    }

    @Override
    protected String getSharedPreferencesName() {
        return PREFERENCE_FILENAME;
    }

    public boolean getTractionEnable() {
        return getBoolean(KEY_TRACTION_ENABLED, DEFAULT_BOOLEAN_VALUE);
    }

    public void setTractionEnable(boolean tractionEnable) {
        putBoolean(KEY_TRACTION_ENABLED, tractionEnable);
    }

    public boolean getIntroduceEnable() {
        return getBoolean(KEY_INTRODUCE_ENABLED, DEFAULT_BOOLEAN_VALUE);
    }

    public void setIntroduceEnable(boolean introduceEnable) {
        putBoolean(KEY_INTRODUCE_ENABLED, introduceEnable);
    }
}
