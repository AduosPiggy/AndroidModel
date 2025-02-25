package com.example.androidmodel.tools.properties;

import android.util.Log;

import java.lang.reflect.Method;
/**
 * @author kfflso
 * @data 2025/1/22 20:44
 * @plus:
 */
public class SystemProperties {

    private final static String TAG = "SystemProperties";

    public static String getProp(String key) {
        Class<?> SysProp = null;
        Method method = null;
        String value = null;
        try {
            SysProp = Class.forName("android.os.SystemProperties");
            method = SysProp.getMethod("get", String.class);
            value = (String) method.invoke(null, key);
            Log.i(TAG, "value：" + value);
        } catch (Exception e) {
            Log.e(TAG, "read SystemProperties error", e);
        }
        return value;
    }

    public static String getProp(String key, String defaultValue) {
        Class<?> SysProp = null;
        Method method = null;
        String value = null;
        try {
            SysProp = Class.forName("android.os.SystemProperties");
            method = SysProp.getMethod("get", String.class, String.class);
            value = (String) method.invoke(null, key, defaultValue);
        } catch (Exception e) {
            Log.e(TAG, "read SystemProperties error", e);
        }
        return value;
    }

    public static int getPropInt(String key, int defaultValue) {
        Class<?> SysProp = null;
        Method method = null;
        int value = 0;
        try {
            SysProp = Class.forName("android.os.SystemProperties");
            method = SysProp.getMethod("getInt", String.class, int.class);
            value = (Integer) method.invoke(null, key, defaultValue);
        } catch (Exception e) {
            Log.e(TAG, "read SystemProperties error", e);
        }
        return value;
    }

    public static long getPropLong(String key, long defaultValue) {
        Class<?> SysProp = null;
        Method method = null;
        long value = 0;
        try {
            SysProp = Class.forName("android.os.SystemProperties");
            method = SysProp.getMethod("getLong", String.class, long.class);
            value = (Long) method.invoke(null, key, defaultValue);
        } catch (Exception e) {
            Log.e(TAG, "read SystemProperties error", e);
        }
        return value;
    }

    public static boolean getPropBoolean(String key, boolean defaultValue) {
        Class<?> SysProp = null;
        Method method = null;
        boolean value = false;
        try {
            SysProp = Class.forName("android.os.SystemProperties");
            method = SysProp.getMethod("getBoolean", String.class, boolean.class);
            value = (Boolean) method.invoke(null, key, defaultValue);
        } catch (Exception e) {
            Log.e(TAG, "read SystemProperties error", e);
        }
        return value;
    }
    /**
     * 设置属性值
     *
     * @param key   长度不能超过31，key.length <= 30
     * @param value 长度不能超过91，value.length<=90
     */
    public static void setProp(String key, String value) {
        Class<?> SysProp = null;
        Method method = null;
        try {
            SysProp = Class.forName("android.os.SystemProperties");
            method = SysProp.getMethod("set", String.class, String.class);
            method.invoke(null, key, value);
        } catch (Exception e) {
            Log.e(TAG, "read SystemProperties error", e);
        }
    }

    public static void addChangeCallback(Runnable runnable) {
        Class<?> SysProp = null;
        Method method = null;
        try {
            SysProp = Class.forName("android.os.SystemProperties");
            method = SysProp.getMethod("addChangeCallback", Runnable.class);
            method.invoke(null, runnable);
        } catch (Exception e) {
            Log.e(TAG, "read SystemProperties error", e);
        }
    }
}
