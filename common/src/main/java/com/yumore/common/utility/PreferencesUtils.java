package com.yumore.common.utility;

import android.annotation.SuppressLint;
import android.content.Context;

import com.yumore.common.basic.BasePreferences;

import java.util.UUID;

/**
 * @author Nathaniel
 * @datetime 2019/10/18 - 17:50
 */
public class PreferencesUtils extends BasePreferences {
    private static final String KEY_USER_INFO = "userInfo";
    /**
     * first install app or not
     * default is false
     */
    private static final String KEY_ENVIRONMENT = "environment";
    private static final String PREFERENCE_FILENAME = "common.sdf";
    private static final String KEY_DEVICE_TOKEN = "deviceToken";
    private static final String KEY_DEVICE_SIGNAL = "deviceSignal";
    private static final String KEY_LASTED_LOGIN = "lastedLogin";
    private static final String KEY_MESSAGE_READABLE = "msgReadable";
    /**
     * traction show or not
     */
    private static final String KEY_VERSION_CODE = "versionCode";
    /**
     * google service available
     */
    private static final String KEY_GOOGLE_SERVICE = "googleService";
    /**
     * new version has bean installed
     */
    private static final String KEY_NEWER_VERSION = "newerVersion";
    /**
     * display payment
     */
    private static final String KEY_DISPLAY_PAYMENT = "displayPayment";
    /**
     * dashboard cached
     */
    private static final String KEY_DASHBOARD_CACHED = "dashboardCached";

    private static final String KEY_MARKETS_CACHED = "marketsCached";

    private static final String KEY_UPGRADE_ACCOUNT = "upgradeAccount";

    private static final String KEY_PUSHABLE_ENABLE = "pushableEnable";

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

    public long getLastedLogin() {
        return getLong(KEY_LASTED_LOGIN, 0L);
    }

    public void setLastedLogin(long lastedLogin) {
        putLong(KEY_LASTED_LOGIN, lastedLogin);
    }

    public String getDeviceToken() {
        return getString(KEY_DEVICE_TOKEN, null);
    }

    public void setDeviceToken(String deviceToken) {
        putString(KEY_DEVICE_TOKEN, deviceToken);
    }

    public String getDeviceSignal() {
        return getString(KEY_DEVICE_SIGNAL, UUID.randomUUID().toString());
    }

    public void setDeviceSignal(String deviceSignal) {
        putString(KEY_DEVICE_SIGNAL, deviceSignal);
    }

    public int getEnvironment() {
        return getInt(KEY_ENVIRONMENT, 0);
    }

    public void setEnvironment(int environment) {
        putInt(KEY_ENVIRONMENT, environment);
    }

    public int getMsgReadable() {
        return getInt(KEY_MESSAGE_READABLE, 0);
    }

    public void setMsgReadable(int readable) {
        putInt(KEY_MESSAGE_READABLE, readable);
    }

    public boolean getGoogleServiced() {
        return getBoolean(KEY_GOOGLE_SERVICE, false);
    }

    public void setGoogleServiced(boolean googleServiced) {
        putBoolean(KEY_GOOGLE_SERVICE, googleServiced);
    }

    public boolean getNewerVersion() {
        return getBoolean(KEY_NEWER_VERSION, true);
    }

    public void setNewerVersion(boolean newerVersion) {
        putBoolean(KEY_NEWER_VERSION, newerVersion);
    }

    public long getVersionCode() {
        return getLong(KEY_VERSION_CODE, 0);
    }

    public void setVersionCode(long versionCode) {
        putLong(KEY_VERSION_CODE, versionCode);
    }

    public boolean getPaymentEnable() {
        return getBoolean(KEY_DISPLAY_PAYMENT, false);
    }

    public void setPaymentEnable(boolean paymentEnable) {
        putBoolean(KEY_DISPLAY_PAYMENT, paymentEnable);
    }

    public boolean getUpgradeAccount() {
        return getBoolean(KEY_UPGRADE_ACCOUNT, false);
    }

    public void setUpgradeAccount(boolean upgraded) {
        putBoolean(KEY_UPGRADE_ACCOUNT, upgraded);
    }

    public boolean getPushableEnable() {
        return getBoolean(KEY_PUSHABLE_ENABLE, false);
    }

    public void setPushableEnable(boolean pushableEnable) {
        putBoolean(KEY_PUSHABLE_ENABLE, pushableEnable);
    }
}
