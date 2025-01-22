package com.example.androidmodel.tools.permission;

import android.Manifest;
import android.app.Activity;

/**
 * @author kfflso
 * @data 2024/9/26 14:22
 * @plus:
 */
public class PermissionImpl {

    public static void verify_phone(Activity activity){
        String reqPermission = Manifest.permission.READ_PHONE_STATE;
        int requestCode = 11;
        PermissionUtils.checkAndRequestPermission(activity, reqPermission, requestCode);
    }

    public static void verify_screenOnAndOff(Activity activity){
        String[] reqPermissions = { Manifest.permission.BIND_DEVICE_ADMIN, Manifest.permission.WAKE_LOCK };
        int requestCode = 11;
        PermissionUtils.checkAndRequestMorePermissions(activity, reqPermissions, requestCode);
    }
}
