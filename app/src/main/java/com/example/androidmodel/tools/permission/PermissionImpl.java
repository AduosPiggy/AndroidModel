package com.example.androidmodel.tools.permission;

import android.Manifest;
import android.app.Activity;
import android.view.accessibility.AccessibilityNodeInfo;

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
}
