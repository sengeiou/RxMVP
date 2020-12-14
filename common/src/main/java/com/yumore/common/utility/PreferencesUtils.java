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
        return getBoolean(KEY_TRACTION_ENABLED, false);
    }

    public void setTractionEnable(boolean tractionEnable) {
        putBoolean(KEY_TRACTION_ENABLED, tractionEnable);
    }
}
