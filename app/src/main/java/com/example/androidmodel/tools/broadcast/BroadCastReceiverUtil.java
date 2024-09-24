package com.example.androidmodel.tools.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

/**
 * @author kfflso
 * @data 2024/9/24 10:00
 * @plus:
 */
public class BroadCastReceiverUtil {
    private Context context;
    private String TAG = "BroadCastReceiverUtil";
    public BroadCastReceiverUtil(Context context) {
        this.context = context;
    }

    private BroadcastReceiver signalStrengthReceiver;
    private BroadcastReceiver batteryChangeReceiver;
    private BroadcastReceiver timeChangeReceiver;
    private BroadcastReceiver screenUnlockReceiver;
    private BroadcastReceiver screenLockReceiver;
    private BroadcastReceiver mediaMountedReceiver;
    private BroadcastReceiver usbModeChangeReceiver;
    private BroadcastReceiver userDialReceiver;
    private BroadcastReceiver userCallReceiver;

    public void registerReceivers() {
        signalStrengthReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int strength = intent.getIntExtra("strength", -1);
                Log.d(TAG, "Received signal strength change: " + strength);
            }
        };
        context.registerReceiver(signalStrengthReceiver, new IntentFilter("com.example.ACTION_SIGNAL_STRENGTH"));

        batteryChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                Log.d(TAG, "Received battery change: " + level);
            }
        };
        context.registerReceiver(batteryChangeReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        timeChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long newTime = intent.getLongExtra("newTime", -1);
                Log.d(TAG, "Received time change: " + newTime);
            }
        };
        context.registerReceiver(timeChangeReceiver, new IntentFilter("com.example.ACTION_TIME_CHANGED"));

        screenUnlockReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "Screen unlocked");
            }
        };
        context.registerReceiver(screenUnlockReceiver, new IntentFilter("com.example.ACTION_SCREEN_UNLOCK"));

        screenLockReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "Screen Locked");
            }
        };
        context.registerReceiver(screenLockReceiver, new IntentFilter("com.example.ACTION_SCREEN_LOCK"));

        mediaMountedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "Media mounted");
            }
        };
        context.registerReceiver(mediaMountedReceiver, new IntentFilter(Intent.ACTION_MEDIA_MOUNTED));

        usbModeChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "USB mode changed");
            }
        };
        context.registerReceiver(usbModeChangeReceiver, new IntentFilter("com.example.ACTION_USB_MODE_CHANGED"));

        userDialReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String phoneNumber = intent.getData() != null ? intent.getData().toString() : "unknown";
                Log.d(TAG, "User dialed: " + phoneNumber);
            }
        };
        context.registerReceiver(userDialReceiver, new IntentFilter(Intent.ACTION_DIAL));

        userCallReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String phoneNumber = intent.getData() != null ? intent.getData().toString() : "unknown";
                Log.d(TAG, "User called: " + phoneNumber);
            }
        };
        context.registerReceiver(userCallReceiver, new IntentFilter(Intent.ACTION_CALL));

        Log.d(TAG, "finished register, start to receive broadcast;");
    }

    public void unregisterReceiver(){
        context.unregisterReceiver(signalStrengthReceiver);
        context.unregisterReceiver(batteryChangeReceiver);
        context.unregisterReceiver(timeChangeReceiver);
        context.unregisterReceiver(screenUnlockReceiver);
        context.unregisterReceiver(screenLockReceiver);
        context.unregisterReceiver(mediaMountedReceiver);
        context.unregisterReceiver(usbModeChangeReceiver);
        context.unregisterReceiver(userDialReceiver);
        context.unregisterReceiver(userCallReceiver);
    }

}
