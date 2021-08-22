package com.yumore.utility.module;

import android.graphics.drawable.Drawable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;


/**
 * '@Transient'表示忽略的字段
 *
 * @author nathaniel
 * @version V1.0.0
 * @contact <a href="mailto:nathanwriting@126.com">contact me</a>
 * @package com.nathaniel.sample
 * @datetime 4/29/21 - 8:00 PM
 */
@Entity
public class PackageEntity {
    @Id(autoincrement = true)
    private long id;
    private String packageName;
    private String appName;
    @Transient
    private Drawable appIcon;
    private int versionCode;
    private String versionName;
    private long wifiRx;
    private long wifiTx;
    private long wifiTotal;
    private long mobileRx;
    private long mobileTx;
    private long mobileTotal;
    private int uid;
    private boolean overlay;
    private byte[] bitmaps;

    @Generated(hash = 727396726)
    public PackageEntity(long id, String packageName, String appName,
                         int versionCode, String versionName, long wifiRx, long wifiTx,
                         long wifiTotal, long mobileRx, long mobileTx, long mobileTotal, int uid,
                         boolean overlay, byte[] bitmaps) {
        this.id = id;
        this.packageName = packageName;
        this.appName = appName;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.wifiRx = wifiRx;
        this.wifiTx = wifiTx;
        this.wifiTotal = wifiTotal;
        this.mobileRx = mobileRx;
        this.mobileTx = mobileTx;
        this.mobileTotal = mobileTotal;
        this.uid = uid;
        this.overlay = overlay;
        this.bitmaps = bitmaps;
    }

    @Generated(hash = 1977018204)
    public PackageEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public long getWifiRx() {
        return wifiRx;
    }

    public void setWifiRx(long wifiRx) {
        this.wifiRx = wifiRx;
    }

    public long getWifiTx() {
        return wifiTx;
    }

    public void setWifiTx(long wifiTx) {
        this.wifiTx = wifiTx;
    }

    public long getWifiTotal() {
        return wifiTotal;
    }

    public void setWifiTotal(long wifiTotal) {
        this.wifiTotal = wifiTotal;
    }

    public long getMobileRx() {
        return mobileRx;
    }

    public void setMobileRx(long mobileRx) {
        this.mobileRx = mobileRx;
    }

    public long getMobileTx() {
        return mobileTx;
    }

    public void setMobileTx(long mobileTx) {
        this.mobileTx = mobileTx;
    }

    public long getMobileTotal() {
        return mobileTotal;
    }

    public void setMobileTotal(long mobileTotal) {
        this.mobileTotal = mobileTotal;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public boolean isOverlay() {
        return overlay;
    }

    @Override
    public String toString() {
        return "PackageEntity{" +
            "id=" + id +
            ", packageName='" + packageName + '\'' +
            ", appName='" + appName + '\'' +
            ", appIcon=" + appIcon +
            ", versionCode=" + versionCode +
            ", versionName='" + versionName + '\'' +
            ", wifiRx=" + wifiRx +
            ", wifiTx=" + wifiTx +
            ", wifiTotal=" + wifiTotal +
            ", mobileRx=" + mobileRx +
            ", mobileTx=" + mobileTx +
            ", mobileTotal=" + mobileTotal +
            ", uid=" + uid +
            ", overlay=" + overlay +
            '}';
    }

    public boolean getOverlay() {
        return this.overlay;
    }

    public void setOverlay(boolean overlay) {
        this.overlay = overlay;
    }

    public byte[] getBitmaps() {
        return this.bitmaps;
    }

    public void setBitmaps(byte[] bitmaps) {
        this.bitmaps = bitmaps;
    }
}
