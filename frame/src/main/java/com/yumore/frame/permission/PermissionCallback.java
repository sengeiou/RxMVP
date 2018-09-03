package com.yumore.frame.permission;

import android.support.v4.app.ActivityCompat;

/**
 * @author Nathaniel
 * @date 18-9-3-下午4:14
 */
public interface PermissionCallback extends ActivityCompat.OnRequestPermissionsResultCallback {
    /**
     * 同意授权
     *
     * @param requestCode requestCode
     * @param permissions permissions
     */
    void onPermissionGranted(int requestCode, String... permissions);

    /**
     * 拒绝授权
     *
     * @param requestCode requestCode
     * @param permissions permissions
     */
    void onPermissionDenied(int requestCode, String... permissions);
}
