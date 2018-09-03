package com.yumore.frame.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * NathanielPermission
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/2/11 - 上午10:49
 */
public class NathanielPermission {
    public static final int SETTINGS_REQUEST_CODE = 110112;
    private Object object;
    private String[] permissions;
    private String rationale;
    private int requestCode;
    @StringRes
    private int positiveButtonText = android.R.string.ok;
    @StringRes
    private int negativeButtonText = android.R.string.cancel;

    private NathanielPermission(Object object) {
        this.object = object;
    }

    public static NathanielPermission with(Activity activity) {
        return new NathanielPermission(activity);
    }

    public static NathanielPermission with(Fragment fragment) {
        return new NathanielPermission(fragment);
    }


    public static boolean hasPermissions(@NonNull Context context, String... perms) {
        if (!PermissionUtils.isOverMarshmallow()) {
            return true;
        }

        if (perms == null || perms.length == 0) {
            return true;
        }

        for (String perm : perms) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) {
                return false;
            }
        }

        return true;
    }

    public static boolean hasPermissionAfterSuccess(@NonNull Context context, String... perms) {
        if (!PermissionUtils.isOverMarshmallow()) {
            return true;
        }

        if (perms == null || perms.length == 0) {
            return true;
        }

        for (String permission : perms) {
            int result = ContextCompat.checkSelfPermission(context, permission);
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }

            String permissionToOp = AppOpsManagerCompat.permissionToOp(permission);
            //noinspection ConstantConditions
            if (TextUtils.isEmpty(permissionToOp)) {
                continue;
            }

            result = AppOpsManagerCompat.noteProxyOp(context, permissionToOp, context.getPackageName());
            if (result != AppOpsManagerCompat.MODE_ALLOWED) {
                return false;
            }
        }

        return true;
    }


    public static void requestPermissions(final Object object, String rationale, final int requestCode, final String... perms) {
        requestPermissions(object, rationale, android.R.string.ok, android.R.string.cancel, requestCode, perms);
    }

    public static void requestPermissions(final Object object, String rationale, @StringRes int positiveButton, @StringRes int negativeButton, final int requestCode, final String... permissions) {
        checkCallingObjectSuitability(object);
        PermissionCallback permissionCallback = (PermissionCallback) object;
        if (!PermissionUtils.isOverMarshmallow()) {
            permissionCallback.onPermissionGranted(requestCode, permissions);
            return;
        }

        final List<String> deniedPermissions = PermissionUtils.findDeniedPermissions(PermissionUtils.getActivity(object), permissions);
        if (deniedPermissions == null) {
            return;
        }

        boolean shouldShowRationale = false;
        for (String permission : deniedPermissions) {
            shouldShowRationale = shouldShowRationale || PermissionUtils.shouldShowRequestPermissionRationale(object, permission);
        }

        if (PermissionUtils.isEmpty(deniedPermissions)) {
            permissionCallback.onPermissionGranted(requestCode, permissions);
        } else {

            final String[] deniedPermissionArray = deniedPermissions.toArray(new String[deniedPermissions.size()]);

            if (shouldShowRationale) {
                Activity activity = PermissionUtils.getActivity(object);
                if (null == activity) {
                    return;
                }

                new AlertDialog.Builder(activity).setMessage(rationale)
                        .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                executePermissionsRequest(object, deniedPermissionArray, requestCode);
                            }
                        })
                        .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // act as if the permissions were denied
                                ((PermissionCallback) object).onPermissionDenied(requestCode, deniedPermissionArray);
                            }
                        })
                        .create()
                        .show();
            } else {
                executePermissionsRequest(object, deniedPermissionArray, requestCode);
            }
        }
    }

    @TargetApi(23)
    private static void executePermissionsRequest(Object object, String[] perms, int requestCode) {
        checkCallingObjectSuitability(object);

        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(perms, requestCode);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).requestPermissions(perms, requestCode);
        }
    }


    public static void onRequestPermissionsResult(Object object, int requestCode, String[] permissions, int[] grantResults) {
        checkCallingObjectSuitability(object);

        PermissionCallback permissionCallback = (PermissionCallback) object;

        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }

        if (PermissionUtils.isEmpty(deniedPermissions)) {
            permissionCallback.onPermissionGranted(requestCode, permissions);
        } else {
            permissionCallback.onPermissionDenied(requestCode, deniedPermissions.toArray(new String[deniedPermissions.size()]));
        }
    }


    public static boolean checkDeniedPermissionsNeverAskAgain(final Object object, String rationale, String... deniedPerms) {
        return checkDeniedPermissionsNeverAskAgain(object, rationale, android.R.string.ok, android.R.string.cancel,
                null, deniedPerms);
    }

    public static boolean checkDeniedPermissionsNeverAskAgain(final Object object, String rationale, @StringRes int positiveButton, @StringRes int negativeButton, @Nullable DialogInterface.OnClickListener negativeButtonOnClickListener, String... deniedPerms) {
        boolean shouldShowRationale;
        for (String perm : deniedPerms) {
            shouldShowRationale = PermissionUtils.shouldShowRequestPermissionRationale(object, perm);

            if (!shouldShowRationale) {
                final Activity activity = PermissionUtils.getActivity(object);
                if (null == activity) {
                    return true;
                }

                new AlertDialog.Builder(activity).setMessage(rationale)
                        .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                intent.setData(uri);
                                startAppSettingsScreen(object, intent);
                            }
                        })
                        .setNegativeButton(negativeButton, negativeButtonOnClickListener)
                        .create()
                        .show();

                return true;
            }
        }

        return false;
    }

    @TargetApi(11)
    private static void startAppSettingsScreen(Object object, Intent intent) {
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, SETTINGS_REQUEST_CODE);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, SETTINGS_REQUEST_CODE);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).startActivityForResult(intent, SETTINGS_REQUEST_CODE);
        }
    }

    private static void checkCallingObjectSuitability(Object object) {

        if (!((object instanceof Fragment) || (object instanceof Activity) || (object instanceof android.app.Fragment))) {
            throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
        }


        if (!(object instanceof PermissionCallback)) {
            throw new IllegalArgumentException("Caller must implement PermissionCallback.");
        }
    }

    public NathanielPermission permissions(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    public NathanielPermission rationale(String rationale) {
        this.rationale = rationale;
        return this;
    }

    public NathanielPermission addRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public NathanielPermission positiveButtonText(@StringRes int positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
        return this;
    }

    public NathanielPermission negativeButtonText(@StringRes int negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
        return this;
    }

    public void request() {
        requestPermissions(object, rationale, positiveButtonText, negativeButtonText, requestCode, permissions);
    }
}
