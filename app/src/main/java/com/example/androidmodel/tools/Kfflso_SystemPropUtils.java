package com.example.androidmodel.tools;

import java.lang.reflect.Method;

/**
 * @author kfflso
 * @data 2024/9/29 15:07
 * @plus:
 */
public class Kfflso_SystemPropUtils {
    /**
     * 设置属性值
     *
     * @param key   长度不能超过31，key.length <= 30
     * @param value 长度不能超过91，value.length<=90
     */
    public static void setProp(String key, String value) {
        // android.os.SystemProperties
        // public static void set(String key, String val)
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getMethod("set", String.class, String.class);
            method.invoke(null, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
