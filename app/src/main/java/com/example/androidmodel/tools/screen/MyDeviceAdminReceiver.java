package com.example.androidmodel.tools.screen;

/**
 * @author kfflso
 * @data 2024/9/26 18:30
 * @plus:
 */
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyDeviceAdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.d("ScreenControl", "设备管理员已启用");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Log.d("ScreenControl", "设备管理员已禁用");
    }

//    @Overri
//    public CharSequence onDisableRequested(Context context, Intent intent) {
//           /* // 这里处理 不可编辑设备。这里可以造成死机状态
//            Intent intent2 = new Intent(context, NoticeSetting.class);
//            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent2);
//            context.stopService(intent);// 是否可以停止*/
//
//        return "这是一个可选的消息，警告有关禁止用户的请求";
//    }
//
//    @Override
//    public void onPasswordChanged(Context context, Intent intent) {
//        // 设备管理：密码己经改变
//        Toast.makeText(context, "设备管理：密码己经改变", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPasswordFailed(Context context, Intent intent) {
//        // 设备管理：改变密码失败
//        Toast.makeText(context, "设备管理：改变密码失败", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPasswordSucceeded(Context context, Intent intent) {
//        // 设备管理：改变密码成功
//        Toast.makeText(context, "设备管理：改变密码成功", Toast.LENGTH_SHORT).show();
//    }

}
