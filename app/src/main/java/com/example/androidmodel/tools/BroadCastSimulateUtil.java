package com.example.androidmodel.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Environment;

import java.io.File;

/**
 *
 * @author kfflso
 * @data 2024/9/23 17:46
 * @plus:
 * Utility class for simulating various broadcast intents in an Android application.
 * This class provides methods to mock different system events for testing purposes.
 */
public class BroadCastSimulateUtil {
    private Context context;

    public BroadCastSimulateUtil(Context context) {
        this.context = context;
    }

    // Simulate a change in signal strength
    public void simulateSignalStrengthChange(int strength) {
        Intent intent = new Intent("com.example.ACTION_SIGNAL_STRENGTH");
        intent.putExtra("strength", strength);
        context.sendBroadcast(intent);
    }

    // Simulate a change in battery level
    public void simulateBatteryChange(int level) {
        Intent intent = new Intent(Intent.ACTION_BATTERY_CHANGED);
        intent.putExtra(BatteryManager.EXTRA_LEVEL, level);
        context.sendBroadcast(intent);
    }

    // Simulate a change in the current time
    public void simulateTimeChange(long newTime) {
        Intent intent = new Intent("com.example.ACTION_TIME_CHANGED");
        intent.putExtra("newTime", newTime);
        context.sendBroadcast(intent);
    }

    // Simulate unlocking the screen
    public void simulateScreenUnlock() {
        Intent intent = new Intent("com.example.ACTION_SCREEN_UNLOCK");
        context.sendBroadcast(intent);
    }

    // Simulate locking the screen
    public void simulateScreenLock() {
        Intent intent = new Intent("com.example.ACTION_SCREEN_LOCK");
        context.sendBroadcast(intent);
    }

    // Simulate a media being mounted
    public void simulateMediaMounted() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
        intent.setData(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "dummy")));
        context.sendBroadcast(intent);
    }

    // Simulate a change in USB mode
    public void simulateUsbModeChange() {
        Intent intent = new Intent("com.example.ACTION_USB_MODE_CHANGED");
        context.sendBroadcast(intent);
    }

    // Simulate a user dialing a phone number
    public void simulateUserDial(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.sendBroadcast(intent);
    }

    // Simulate a user making a call to a phone number
    public void simulateUserCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.sendBroadcast(intent);
    }
}
