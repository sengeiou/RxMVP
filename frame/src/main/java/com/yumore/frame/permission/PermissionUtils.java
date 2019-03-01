package com.yumore.frame.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

/**
 * PermissionUtils
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/2/11 - 上午10:52
 */
final class PermissionUtils {
    private PermissionUtils() {
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    protected static List<String> findDeniedPermissions(Activity activity, String... permission) {
        if (activity == null) {
            return null;
        }

        List<String> denyPermissions = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String value : permission) {
                if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                    denyPermissions.add(value);
                }
            }
        }

        return denyPermissions;
    }

    protected static boolean shouldShowRequestPermissionRationale(Object object, String permission) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, permission);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(permission);
        } else {
            return object instanceof android.app.Fragment && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ((android.app.Fragment) object).shouldShowRequestPermissionRationale(permission);
        }
    }

    protected static Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return ((Activity) object);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).getActivity();
        } else {
            return null;
        }
    }

    protected static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }
}
