package com.example.androidmodel.tools;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Kfflso_PackageUtil {
    public String TAG = getClass().getName();
    private Context context;
    private static volatile Kfflso_PackageUtil instance;
    private DevicePolicyManager dpm;
    private PackageManager packageManager;
    private ActivityManager activityManager;

    private Kfflso_PackageUtil(Context ctx) {
        this.context = ctx;
        dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        packageManager = context.getPackageManager();
        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public static Kfflso_PackageUtil getInstance(Context ctx) {
        if (instance == null) {
            synchronized (Kfflso_PackageUtil.class) {
                if (instance == null) {
                    instance = new Kfflso_PackageUtil(ctx);
                }
            }
        }
        return instance;
    }

    /**
     * 获取第三方app
     */
    public List<ApplicationInfo> getThirdPartyApps() {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(0);
        List<ApplicationInfo> thirdPartyApps = new ArrayList<>();
        for (ApplicationInfo appInfo : apps) {
            // 判断是否为第三方应用（非系统应用）
            if (!isSystemApp(appInfo.packageName) && appInfo.sourceDir.startsWith("/data/app")) {
                thirdPartyApps.add(appInfo);
            }
        }
        return thirdPartyApps;
    }

    /**
     * 判断是否为系统应用
     * @param packName
     * @return
     */
    public boolean isSystemApp(String packName) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packName, 0);
            if (applicationInfo == null) {
                return false;
            }
            return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) || ((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    //取消设备管理员应用权限 for uninstall some apps
    public void disableDevOwnerAdmin(String packageName){
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<Void> future = executorService.submit(() -> {
                // 设备管理员应用，取消它的权限
                final List<ComponentName> allActiveAdmins = dpm.getActiveAdmins();
                if (allActiveAdmins != null) {
                    for (ComponentName activeAdmin : allActiveAdmins) {
                        if (packageName.equals(activeAdmin.getPackageName())) {
                            Log.d(TAG, "关闭设备管理权限: " + packageName);
                            dpm.removeActiveAdmin(activeAdmin);
                            // 参考源码 frameworks/base/services/devicepolicy/java/com/android/server/devicepolicy/DevicePolicyManagerService.java
                            // uninstallPackageWithActiveAdminsStart 中是等待10秒
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                Log.d(TAG, "disableDeviceOwnerOrAdmin", e);
                            }
                            break;
                        }
                    }
                }
                //Device / profile owner
                if (dpm.isDeviceOwnerApp(packageName)) {
                    dpm.clearDeviceOwnerApp(packageName);
                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    dpm.listForegroundAffiliatedUsers();
//                }
                return null;
            });
            // 等待任务执行结束,5s超时
            future.get(5, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public PackageInfo getPackageInfo(String apkFilePath) {
        if (!new File(apkFilePath).exists()) return null;
        PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
        return packageInfo;
    }

    /**
     * 根据包名卸载应用
     * @param packageName
     */
    public void uninstall(String packageName) {
        Intent broadcastIntent = new Intent(context, UnInstallResultReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1,
                broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();
        packageInstaller.uninstall(packageName, pendingIntent.getIntentSender());
    }
    public class UnInstallResultReceiver extends BroadcastReceiver {
        private static final String TAG = "UnInstallResultReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("unInstall", "已收到卸载反馈广播");
            //安装广播
            if (intent != null) {
                final int status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS,
                        PackageInstaller.STATUS_FAILURE);
                if (status == PackageInstaller.STATUS_SUCCESS) {
                    Log.d(TAG, "APP UnInstall Success!");
                } else {
                    String msg = intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE);
                    Log.e(TAG, "UnInstall FAILURE status_massage" + msg);
                }
            }
        }
    }

    /**
     * 根据包名唤起一个 app
     * @param packageName
     * @return
     */
    public boolean launchApp(String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null || packageName == null || packageName.isEmpty()) {
            // TODO: 2023/11/10 无android.intent.action.MAIN, 需要使用Broadcast启动 or use setComponent to start other activity
            return false;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        return true;
    }

    public String getTargetAppProcessName(String tarAppPackageName) {
        String processName = "";
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages){
            //不是系统应用 && 不是更新的系统应用 && 应用未停止
            if (    ( (ApplicationInfo.FLAG_SYSTEM & pi.applicationInfo.flags) == 0)
                    && ((ApplicationInfo.FLAG_UPDATED_SYSTEM_APP & pi.applicationInfo.flags) == 0)
                    && ((ApplicationInfo.FLAG_STOPPED & pi.applicationInfo.flags) == 0)) {
                if(tarAppPackageName.equals(pi.packageName)){
                    processName = pi.applicationInfo.processName;
                    break;
                }
            }
        }
        return processName;
    }
    //但是只能获取到当前app的进程
    public void getRunningAppProcesses( ){
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            Log.d("RunningApp", "Process Name: " + processInfo.processName);
        }

    }
}
