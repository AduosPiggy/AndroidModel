package com.example.androidmodel.tools.broadcast;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Environment;
import android.util.Log;

import com.example.androidmodel.tools.PermissionUtils;

import java.io.File;

/**
 *
 * @author kfflso
 * @data 2024/9/23 17:46
 * @plus:
 * Utility class for simulating various broadcast intents in an Android application.
 * This class provides methods to mock different system events for testing purposes.
 *
 *  1)模拟电话信号强度改变
 *  2)模拟电量改变            <uses-permission android:name="android.permission.BROADCAST_STICKY" />
 *  3)模拟当前时间发生变化
 *  4)模拟屏幕解锁
 *  5)模拟锁屏
 *  6)模拟媒体介质被挂载      <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 *                          <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 *  7)模拟设备进入USB模式
 *  8)模拟用户打电话
 *  9)模拟用户拨号            <uses-permission android:name="android.permission.CALL_PHONE" />
 */
public class BroadCastSendUtil {
    private Context context;
    private String TAG = "BroadCastSimulateUtil";
    public BroadCastSendUtil(Context context) {
        this.context = context;
    }

    public void verifyPermission(Activity activity){
        String[] permissions = {
                Manifest.permission.BROADCAST_STICKY,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE
        };
        int requestCode = 100; // 自定义请求码
        PermissionUtils.INSTANCE.checkAndRequestMorePermissions(activity,permissions,requestCode);
    }

    // Simulate a change in signal strength
    public void simulateSignalStrengthChange(int strength) {
        Intent intent = new Intent("com.example.ACTION_SIGNAL_STRENGTH");
        intent.putExtra("strength", strength);
        context.sendBroadcast(intent);
        Log.d(TAG,"sendBroadcast: " + intent + "; strength: " + strength);
    }

    // Simulate a change in battery level
    public void simulateBatteryChange(int level) {
        Intent intent = new Intent(Intent.ACTION_BATTERY_CHANGED);
        intent.putExtra(BatteryManager.EXTRA_LEVEL, level);
        context.sendBroadcast(intent);
        Log.d(TAG,"sendBroadcast: " + intent + "; level: " + level);
    }

    // Simulate a change in the current time
    public void simulateTimeChange(long newTime) {
        Intent intent = new Intent("com.example.ACTION_TIME_CHANGED");
        intent.putExtra("newTime", newTime);
        context.sendBroadcast(intent);
        Log.d(TAG,"sendBroadcast: " + intent + "; newTime: " + newTime);
    }

    // Simulate unlocking the screen
    public void simulateScreenUnlock() {
        Intent intent = new Intent("com.example.ACTION_SCREEN_UNLOCK");
//        Intent intent = new Intent(Intent.ACTION_SCREEN_OFF);
        context.sendBroadcast(intent);
        Log.d(TAG,"sendBroadcast: " + intent);
    }

    // Simulate locking the screen
    public void simulateScreenLock() {
        Intent intent = new Intent("com.example.ACTION_SCREEN_LOCK");
//        Intent intent = new Intent(Intent.ACTION_SCREEN_ON);
        context.sendBroadcast(intent);
        Log.d(TAG,"sendBroadcast: " + intent);
    }

    // Simulate a media being mounted
    public void simulateMediaMounted() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
        intent.setData(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "dummy")));
        context.sendBroadcast(intent);
        Log.d(TAG,"sendBroadcast: " + intent);
    }

    // Simulate a change in USB mode
    public void simulateUsbModeChange() {
        Intent intent = new Intent("com.example.ACTION_USB_MODE_CHANGED");
        context.sendBroadcast(intent);
        Log.d(TAG,"sendBroadcast: " + intent);
    }

    // Simulate a user dialing a phone number
    public void simulateUserDial(String phoneNumber) {
        Uri uri = Uri.parse(phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL,uri);
        context.sendBroadcast(intent);
        Log.d(TAG,"sendBroadcast: " + intent + "; phoneNumber: " + phoneNumber);
    }

    // Simulate a user making a call to a phone number
    public void simulateUserCall(String phoneNumber) {
        Uri uri = Uri.parse(phoneNumber);
        Intent intent = new Intent(Intent.ACTION_CALL,uri);
        context.sendBroadcast(intent);
        Log.d(TAG,"sendBroadcast: " + intent + "; phoneNumber: " + phoneNumber);
    }

}
