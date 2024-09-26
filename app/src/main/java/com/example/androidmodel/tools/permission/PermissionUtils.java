package com.example.androidmodel.tools.permission;

/**
 * @author kfflso
 * @data 2024/9/26 14:17
 * @plus:
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.androidmodel.base.BaseApp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionUtils {

    /**
     * 检测权限
     * @return true：已授权； false：未授权；
     */
    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检测多个权限
     * @return 未授权的权限
     */
    public static List<String> checkMorePermissions(Context context, String[] permissions) {
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (!checkPermission(context, permission)) {
                permissionList.add(permission);
            }
        }
        return permissionList;
    }

    /**
     * 请求权限
     */
    public static void requestPermission(Activity activity, String permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

    /**
     * 请求多个权限
     */
    public static void requestMorePermissions(Activity activity, List<String> permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions.toArray(new String[0]), requestCode);
    }

    /**
     * 检测权限并请求权限：如果没有权限，则请求权限
     */
    public static void checkAndRequestPermission(Activity activity, String permission, int requestCode) {
        if (!checkPermission(activity, permission)) {
            requestPermission(activity, permission, requestCode);
        }
    }

    /**
     * 检测并请求多个权限
     */
    public static void checkAndRequestMorePermissions(Activity activity, String[] permissions, int requestCode) {
        List<String> permissionList = checkMorePermissions(activity, permissions);
        if (!permissionList.isEmpty()) {
            requestMorePermissions(activity, permissionList, requestCode);
        }
    }

    /**
     * 跳转到权限设置界面
     */
    public static void toAppPermissionSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        } else {
            intent.setAction(Intent.ACTION_VIEW);
        }
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }

    public static List<String> getPermissionFromAM(String packageName) {
        PackageManager pm = BaseApp.application.getPackageManager();
        try {
            String[] requestedPermissions = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions;
            if (requestedPermissions != null) {
                return Arrays.asList(requestedPermissions);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}

