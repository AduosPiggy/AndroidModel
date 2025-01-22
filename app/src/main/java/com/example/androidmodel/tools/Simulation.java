package com.example.androidmodel.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Environment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class       Simulation {

    public final int signal_strength_mobile;//模拟移动流量信号强度;
    public final int signal_strength_wifi;//模拟 wifi 信号强度;
    public final int battery_level;//模拟电池电量改变为 88%;
    public final long system_time;//模拟修改系统时间为:"2024/09/26 17:20";
    public final String[] phone_pwd = {"1","0","0","6","0","1"};
    public final ExecutorService executorService;
    public final File file_media_mounted;//模拟被挂载的文件
    public final String phone_number_dial;//模拟拨号号码
    public final String phone_number_call;//模拟打电话号码


    public Simulation() {
        //initSimulateData
        signal_strength_mobile = 2;
        signal_strength_wifi = 2;
        battery_level = 88;
        system_time = getSimulateSystemTime();
        executorService = Executors.newSingleThreadExecutor();
        file_media_mounted = new File(Environment.getExternalStorageDirectory(), "dummy");
        phone_number_dial = "123456789";
        phone_number_call = "987654321";
    }

    // 模拟移动信号强度(0-4) 2
    public boolean simulateSignalStrengthMobile(int level) {
        try {
            Process process = Runtime.getRuntime().exec("/system/xbin/asu");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            // 允许Demo Mode
            os.writeBytes("settings put global sysui_demo_allowed 1\n");
            // 进入Demo Mode
            os.writeBytes("am broadcast -a com.android.systemui.demo -e command enter\n");
            // 发送广播命令以模拟WiFi信号强度
            os.writeBytes("am broadcast -a com.android.systemui.demo -e command network -e mobile show -e level " + level + "\n");
            os.flush();
            Thread.sleep(2000);
            os.writeBytes("am broadcast -a com.android.systemui.demo -e command exit\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            os.close();
            process.waitFor();
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 模拟 wifi 信号强度(0-4) 2
    public boolean simulateSignalStrengthWifi(int level) {
        try {
            Process process = Runtime.getRuntime().exec("/system/xbin/asu");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            // 允许Demo Mode
            os.writeBytes("settings put global sysui_demo_allowed 1\n");
            // 进入Demo Mode
            os.writeBytes("am broadcast -a com.android.systemui.demo -e command enter\n");
            // 发送广播命令以模拟WiFi信号强度
            os.writeBytes("am broadcast -a com.android.systemui.demo -e command network -e wifi show -e level " + level + " -e fully true\n");
            os.flush();
            Thread.sleep(2000);
            os.writeBytes("am broadcast -a com.android.systemui.demo -e command exit\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            os.close();
            process.waitFor();
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 模拟电池电量发生变化
    public String simulateBatteryChanged(Context context,int level) {
        Intent intent = new Intent("android.intent.action.BATTERY_CHANGED");
        intent.putExtra(BatteryManager.EXTRA_LEVEL, level);
        context.sendBroadcast(intent);
        return "sendBroadcast: " + intent + "; level: " + level;
    }
    // 模拟当前系统时间 时区(Asia/Shanghai)
    public boolean simulateSystemTime(long time_ms) {
        try {
            Process process = Runtime.getRuntime().exec("/system/xbin/asu");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            //Asia/Shanghai (GMT+8)
            os.writeBytes("setprop persist.sys.timezone Asia/Shanghai\n");
            os.writeBytes("/system/bin/date -s @" + time_ms/1000 + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
            os.close();
            process.waitFor();
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    public long getSimulateSystemTime(){
        long time = 0L;
        try {
            String dateString = "2024/09/26 17:20";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            Date date = dateFormat.parse(dateString);
            if (date != null) {
                time = date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    //    //锁屏, 不知道密码就不能解锁(谨慎使用); tdc测试的模拟器断开重连可恢复屏幕连接
//    //需要 dpm 相关权限, tdc 已经放权;
//    private void simulateScreenLock(Context context){
//        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
//        dpm.lockNow();
//    }
//    private void simulateScreenUnLock(Context context){
//        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        if (!powerManager.isInteractive()) {
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Instrumentation inst = new Instrumentation();
//                        inst.sendKeyDownUpSync(android.view.KeyEvent.KEYCODE_POWER);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//    }
    public boolean simulateScreenLock(){
        try {
            Process process = Runtime.getRuntime().exec("/system/xbin/asu");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("input keyevent 26\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            os.close();
            process.waitFor();
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean simulateScreenUnLock(){
        try {
            Process process = Runtime.getRuntime().exec("/system/xbin/asu");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("input keyevent 26\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            os.close();
            process.waitFor();
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String simulateMediaMounted(Context context,File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent("android.intent.action.MEDIA_MOUNTED",uri);
        context.sendBroadcast(intent);
        return "sendBroadcast: " + intent;
    }
    public String simulateUsbDeviceAttached(Context context) {
        Intent intent = new Intent("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        context.sendBroadcast(intent);
        return "sendBroadcast: " + intent;
    }
    public String simulateDial(Context context,String phoneNumber) {
        Uri uri = Uri.parse("tel:" + phoneNumber);
        Intent intent = new Intent("android.intent.action.DIAL",uri);
        context.startActivity(intent);
        return "startActivity: " + intent + "; phoneNumber: " + phoneNumber;
    }
    public String simulateUserCall(Context context,String phoneNumber) {
        Intent intent = new Intent("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
        return "startActivity: " + intent + "; phoneNumber: " + phoneNumber;
    }
}
