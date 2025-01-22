package com.example.androidmodel.tools.properties;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.util.Properties;

/**
 * @author kfflso
 * @data 2025-01-22 20:33
 * @plus:
 */
public class PropUtils {
    public void getPropFromClassBuild(){
        String BOARD = Build.BOARD;//主板
        String BRAND = Build.BRAND; // Android系统定制商
        String CPU_ABI = Build.CPU_ABI; // cpu指令集
        String DEVICE = Build.DEVICE; // 设备参数
        String DISPLAY = Build.DISPLAY; // 显示屏参数
        String FINGERPRINT = Build.FINGERPRINT; // 硬件名称
        String HOST = Build.HOST;  //
        String ID = Build.ID; // 修订版本列表
        String MANUFACTURER = Build.MANUFACTURER; // 硬件制造商
        String MODEL = Build.MODEL; // 版本
        String PRODUCT = Build.PRODUCT; // 手机制造商
        String TAGS = Build.TAGS; // 描述build的标签
        long TIME = Build.TIME; //时间
        String TYPE = Build.TYPE; // builder类型
        String USER = Build.USER;  //用户

        String CODENAME = Build.VERSION.CODENAME;//当前开发代号
        String INCREMENTAL = Build.VERSION.INCREMENTAL;//源码控制版本号
        String RELEASE = Build.VERSION.RELEASE;//版本字符串
        String SDK = Build.VERSION.SDK;  //版本号

        int TIRAMISU = Build.VERSION_CODES.TIRAMISU;//33
        int S_V2 = Build.VERSION_CODES.S_V2;//32
        int S = Build.VERSION_CODES.S;//31
        int R = Build.VERSION_CODES.R;//30
        int Q = Build.VERSION_CODES.Q;//29
        int P = Build.VERSION_CODES.P;//28
    }
    public void getPropFromClassSystem(){
        //获取所有属性
        Properties systemProperties = System.getProperties();
        Log.d("twy001", "key: value");
        systemProperties.forEach((key, value) -> {
            Log.d("twy001", key +": " + value);
        });
        //获取单个属性
        StringBuilder sb = new StringBuilder();
        sb.append("Java 运行时环境版本:"+System.getProperty("java.version")+"\n");
        sb.append("Java 运行时环境供应商:"+System.getProperty("java.vendor")+"\n");
        sb.append("Java 供应商的URL:"+System.getProperty("java.vendor.url")+"\n");
        sb.append("Java 安装目录:"+System.getProperty("java.home")+"\n");
        sb.append("Java 虚拟机规范版本:"+System.getProperty("java.vm.specification.version")+"\n");
        sb.append("Java 类格式版本号:"+System.getProperty("java.class.version")+"\n");
        sb.append("Java类路径："+System.getProperty("java.class.path")+"\n");
        sb.append("加载库时搜索的路径列表:"+System.getProperty("java.library.path")+"\n");
        sb.append("默认的临时文件路径:"+System.getProperty("java.io.tmpdir")+"\n");
        sb.append("要使用的 JIT 编译器的名称:"+System.getProperty("java.compiler")+"\n");
        sb.append("一个或多个扩展目录的路径:"+System.getProperty("java.ext.dirs")+"\n");
        sb.append("操作系统的名称:"+System.getProperty("os.name")+"\n");
        sb.append("操作系统的架构:"+System.getProperty("os.arch")+"\n");
        sb.append("操作系统的版本:"+System.getProperty("os.version")+"\n");
        sb.append("文件分隔符（在 UNIX 系统中是“/”）:"+System.getProperty("file.separator")+"\n");
        sb.append("路径分隔符（在 UNIX 系统中是“:”）:"+System.getProperty("path.separator")+"\n");
        sb.append("行分隔符（在 UNIX 系统中是“/n”）:"+System.getProperty("line.separator")+"\n");
        sb.append("用户的账户名称:"+System.getProperty("user.name")+"\n");
        sb.append("用户的主目录:"+System.getProperty("user.home")+"\n");
        sb.append("用户的当前工作目录:"+System.getProperty("user.dir")+"\n");
        Log.d("TAG", sb.toString());
    }
    public void getSetPropByClassSettings(Context context) throws Settings.SettingNotFoundException {
        ContentResolver contentResolver = context.getContentResolver();
        int SCREEN_BRIGHTNESS = Settings.System.getInt(contentResolver,Settings.System.SCREEN_BRIGHTNESS);
        String SCREEN_OFF_TIMEOUT = Settings.System.getString(contentResolver,Settings.System.SCREEN_OFF_TIMEOUT);
        String AIRPLANE_MODE_ON = Settings.Global.getString(contentResolver,Settings.Global.AIRPLANE_MODE_ON);
        boolean isSuccess = Settings.System.putInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 1);
    }
    public void getSetPropByReflect(){
        //详细分析 SystemProperties类, 包含 get set方法;
        SystemProperties systemProperties = new SystemProperties();
        systemProperties.getProp("");
    }
}
