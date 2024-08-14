package com.example.androidmodel.tools;


import android.util.Log;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private static String TAG = "MD5Util";

    public static String getMD5String(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] e = md.digest(value.getBytes());
            return toHexString(e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return value;
        }
    }

    public static String getMD5String(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] e = md.digest(bytes);
            return toHexString(e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";

        }
    }

    private static String toHexString(byte bytes[]) {
        StringBuilder hs = new StringBuilder(bytes.length * 2);
        String stmp = "";
        for (int n = 0; n < bytes.length; n++) {
            stmp = Integer.toHexString(bytes[n] & 0xff);
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString();
    }

    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static String byteArrayToString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(byteToHexString(b[i]));
        }
        return sb.toString();
    }

    @SuppressWarnings("unused")
    private static String byteToNumString(byte b) {

        int _b = b;
        if (_b < 0) {
            _b = 256 + _b;
        }
        return String.valueOf(_b);
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * md5 加密
     */
    public static String encode(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToString(md.digest(resultString.getBytes()));
        } catch (Exception ex) {

        }
        return resultString;
    }

    /**
     * md5计算filePath的16进制Hash值 -> 可用于验证文件完整性
     * @param filePath
     * @return
     * use:
     *      String md5Hash01/md5Hash02 = calFileMd5(path1/path2);
     *      boolean isSame = md5Hash01.equalsIgnoreCase(md5Hash02);
     *      true:两地文件一致; false: 文件不一致,文件可能不完整;
     */
    public static String calFileMd5(String filePath) {
        try {
            InputStream in = new FileInputStream(filePath);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[4096];
            int read;
            while ((read = in.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
            in.close();
            // 计算MD5哈希值
            byte[] hashBytes = md.digest();
            // 将字节数组转换为十六进制字符串
            return toHexString(hashBytes);
        } catch (Exception e) {
            Log.e(TAG, "calFileMd5 failed", e);
            e.printStackTrace();
        }
        return null;
    }
}
