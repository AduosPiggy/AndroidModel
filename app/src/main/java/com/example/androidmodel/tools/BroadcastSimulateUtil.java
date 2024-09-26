package com.example.androidmodel.tools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Environment;
import android.util.Log;

import com.example.androidmodel.tools.logs.LogsUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author kfflso
 * @data 2024/9/26 15:43
 * @plus:
 * Utility class for simulating various broadcast intents in an Android application.
 * This class provides methods to mock different system events for testing purposes.
 *
 *  registerReceiver // 注册所有广播, 并开始接收广播消息
 *  sendBroadcast // 发送广播
 *  unregisterReceiver // 取消注册广播
 *
 *  1)模拟电话信号强度改变
 *  2)模拟电量改变
 *  3)模拟当前时间发生变化
 *  4)模拟屏幕解锁
 *  5)模拟锁屏
 *  6)模拟媒体介质被挂载
 *  7)模拟设备进入USB模式
 *  8)模拟用户打电话
 *  9)模拟用户拨号
 */
public class BroadcastSimulateUtil {
    private Activity activity;
    private String TAG = "BroadCastSimulateUtil";
    private BroadcastReceiver broadcastReceiver;
    private final String SIG_STR = "android.intent.action.SIG_STR";
    private final String BATTERY_CHANGED = "android.intent.action.BATTERY_CHANGED";
    private final String TIME_TICK = "android.intent.action.TIME_TICK";
    private final String SCREEN_ON = "android.intent.action.SCREEN_ON";
    private final String SCREEN_OFF = "android.intent.action.SCREEN_OFF";
    private final String MEDIA_MOUNTED = "android.intent.action.MEDIA_MOUNTED";
    private final String UMS_CONNECTED = "android.intent.action.UMS_CONNECTED";
    private final String DIAL = "android.intent.action.DIAL";
    private final String CALL = "android.intent.action.CALL";

    public BroadcastSimulateUtil(Activity activity) {
        this.activity = activity;
    }

    public void registerReceiver(Activity activity){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SIG_STR);
        intentFilter.addAction(BATTERY_CHANGED);
        intentFilter.addAction(TIME_TICK);
        intentFilter.addAction(SCREEN_ON);
        intentFilter.addAction(SCREEN_OFF);
        intentFilter.addAction(MEDIA_MOUNTED);
        intentFilter.addAction(UMS_CONNECTED);
        intentFilter.addAction(DIAL);
        intentFilter.addAction(CALL);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action){
                    case SIG_STR:
                        int strength = intent.getIntExtra("strength", -1);
                        Log.d(TAG, "Received signal strength change: " + strength);
                        LogsUtils.logToFileAsync(TAG, "Received signal strength change: " + strength);
                        break;

                    case BATTERY_CHANGED:
                        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                        Log.d(TAG,"Received battery change: " + level);
                        LogsUtils.logToFileAsync(TAG, "Received battery change: " + level);
                        break;

                    case TIME_TICK:
                        long newTime = intent.getLongExtra("newTime", -1);
                        LogsUtils.logToFileAsync(TAG, "Received time change: " + newTime);
                        Log.d(TAG,"Received time change: " + newTime);

                        break;
                    case SCREEN_ON:
                        LogsUtils.logToFileAsync(TAG, "Screen On");
                        Log.d(TAG,"Screen On");

                        break;
                    case SCREEN_OFF:
                        LogsUtils.logToFileAsync(TAG, "Screen Off");
                        Log.d(TAG,"Screen Off");
                        break;

                    case MEDIA_MOUNTED:
                        LogsUtils.logToFileAsync(TAG, "Media mounted");
                        Log.d(TAG,"Media mounted");
                        break;

                    case UMS_CONNECTED:
                        LogsUtils.logToFileAsync(TAG, "USB mode changed");
                        Log.d(TAG,"USB mode changed");
                        break;

                    case DIAL:
                        String phoneNumberDial = intent.getData() != null ? intent.getData().toString() : "unknown";
                        LogsUtils.logToFileAsync(TAG, "User dialed: " + phoneNumberDial);
                        Log.d(TAG,"User dialed: " + phoneNumberDial);
                        break;
                    case CALL:
                        String phoneNumberCall = intent.getData() != null ? intent.getData().toString() : "unknown";
                        LogsUtils.logToFileAsync(TAG, "User called: " + phoneNumberCall);
                        Log.d(TAG,"User called: " + phoneNumberCall);
                        break;
                }
            }
        };
        activity.registerReceiver(broadcastReceiver,intentFilter);

        LogsUtils.logToFileAsync(TAG, "finished register, start to receive broadcast");
        Log.d(TAG,"finished register, start to receive broadcast ");
    }
    public void screenTest(){
        try {
            simulateScreenOff();
            Thread.sleep(5000);
            simulateScreenOn();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendBroadcast(){
        int strength = -76;
        int batteryLevel = 88; // 88% 电量
        long time = getMockTime();
        String phoneNumberToDial = "tel:123456789";
        String phoneNumberToCall = "987654321";

        simulateSIGSTR(strength);
        simulateBatteryChanged(batteryLevel);
        simulateTimeTick(time);
        simulateScreenOn();
        simulateScreenOff();
        simulateMediaMounted();
        simulateUmsConnected();
        simulateDial(phoneNumberToDial);
        simulateUserCall(phoneNumberToCall);

    }

    public void unregisterReceiver(){
        activity.unregisterReceiver(broadcastReceiver);
    }


    public long getMockTime(){
        long time = 0L; // 初始化为0
        try {
            String dateString = "2024/09/26 17:20:52:521";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS", Locale.getDefault());
            Date date = dateFormat.parse(dateString);
            if (date != null) {
                time = date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
    public void simulateSIGSTR(int strength) {
        Intent intent = new Intent(SIG_STR);
        intent.putExtra("strength", strength);
        activity.sendBroadcast(intent);
        LogsUtils.logToFileAsync(TAG,"sendBroadcast: " + intent + "; strength: " + strength);
        Log.d(TAG,"sendBroadcast: " + intent + "; strength: " + strength);
    }

    public void simulateBatteryChanged(int level) {
        Intent intent = new Intent(BATTERY_CHANGED);
        intent.putExtra(BatteryManager.EXTRA_LEVEL, level);
        activity.sendBroadcast(intent);
        LogsUtils.logToFileAsync(TAG,"sendBroadcast: " + intent + "; level: " + level);
        Log.d(TAG,"sendBroadcast: " + intent + "; level: " + level);
    }

    public void simulateTimeTick(long newTime) {
        Intent intent = new Intent(TIME_TICK);
        intent.putExtra("newTime", newTime);
        activity.sendBroadcast(intent);
        LogsUtils.logToFileAsync(TAG,"sendBroadcast: " + intent + "; newTime: " + newTime);
        Log.d(TAG,"sendBroadcast: " + intent + "; newTime: " + newTime);
    }

    public void simulateScreenOn() {
        Intent intent = new Intent(SCREEN_ON);
        activity.sendBroadcast(intent);
        LogsUtils.logToFileAsync(TAG,"sendBroadcast: " + intent);
        Log.d(TAG,"sendBroadcast: " + intent);
    }

    public void simulateScreenOff() {
        Intent intent = new Intent(SCREEN_OFF);
        activity.sendBroadcast(intent);
        LogsUtils.logToFileAsync(TAG,"sendBroadcast: " + intent);
        Log.d(TAG,"sendBroadcast: " + intent);
    }

    public void simulateMediaMounted() {
        Intent intent = new Intent(MEDIA_MOUNTED);
        intent.setData(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "dummy")));
        activity.sendBroadcast(intent);
        LogsUtils.logToFileAsync(TAG,"sendBroadcast: " + intent);
        Log.d(TAG,"sendBroadcast: " + intent);
    }

    public void simulateUmsConnected() {
        Intent intent = new Intent(UMS_CONNECTED);
        activity.sendBroadcast(intent);
        LogsUtils.logToFileAsync(TAG,"sendBroadcast: " + intent);
        Log.d(TAG,"sendBroadcast: " + intent);
    }

    public void simulateDial(String phoneNumber) {
        Uri uri = Uri.parse("tel:" + phoneNumber);
        Intent intent = new Intent(DIAL,uri);
        activity.startActivity(intent);
        LogsUtils.logToFileAsync(TAG,"sendBroadcast: " + intent + "; phoneNumber: " + phoneNumber);
        Log.d(TAG,"sendBroadcast: " + intent + "; phoneNumber: " + phoneNumber);
    }

    public void simulateUserCall(String phoneNumber) {
        Intent intent = new Intent(CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        activity.startActivity(intent);
        LogsUtils.logToFileAsync(TAG,"sendBroadcast: " + intent + "; phoneNumber: " + phoneNumber);
        Log.d(TAG,"sendBroadcast: " + intent + "; phoneNumber: " + phoneNumber);
    }

}
