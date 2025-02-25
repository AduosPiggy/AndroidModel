package com.example.androidmodel.tools.screen;

import static android.view.KeyEvent.KEYCODE_POWER;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author kfflso
 * @data 2024/9/26 18:29
 * @plus:
 *
 */
public class ScreenControl {
    private Activity activity;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName adminComponent;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private String TAG = "ScreenControl";

    public ScreenControl(Activity activity) {
        this.activity = activity;
        initData();
    }
    private void initData (){
        devicePolicyManager = (DevicePolicyManager) activity.getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(activity, MyDeviceAdminReceiver.class);

        // 检查设备管理员权限
        if (!isDeviceAdmin()) {
            requestDeviceAdmin();
        }
        // 这里可以选择直接获取 WakeLock
        powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "MyApp::MyWakelockTag");

    }
    private boolean isDeviceAdmin() {
        boolean isDeviceAdmin = devicePolicyManager.isAdminActive(adminComponent);
        return isDeviceAdmin;
    }
    private void requestDeviceAdmin() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "需要设备管理权限");
        activity.startActivityForResult(intent, 1);
    }
    private void removeActiveAdmin(ComponentName componentName){
        devicePolicyManager.removeActiveAdmin(componentName);
    }

    //锁屏
    public void turnOffScreen( ) {
        if(!isDeviceAdmin()){
            requestDeviceAdmin();
            Log.d(TAG, "lockScreen ---> 设备管理权限未获得");
            return;
        }
        devicePolicyManager.lockNow();
        Log.d(TAG, "屏幕已锁定");
    }
    public void turnOnScreen(){
        PowerManager powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        boolean isInteractive = powerManager.isInteractive();
        if (!isInteractive) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(KEYCODE_POWER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

//    // 亮屏 ---> 但是处理不了锁屏,不知道为什么一直报权限错误
//    public void turnOnScreen(long timeOutMillis) {
//        if(!isDeviceAdmin()){
//            requestDeviceAdmin();
//            Log.d(TAG, "turnOnScreen ---> 设备管理权限未获得");
//            return;
//        }
//        wakeLock.acquire(timeOutMillis); // 亮屏 3 秒钟
//        Log.d(TAG, "屏幕已亮起");
//    }

    public void destroy(){
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        removeActiveAdmin(adminComponent);
    }

}
