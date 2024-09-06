package com.example.androidmodel.tools;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CmdUtil {

    public static CmdUtil getInstance(){
        return SingletonHolder.instance;
    }
    private static class SingletonHolder {
        private static CmdUtil instance = new CmdUtil();
    }
    public CmdUtil() {
    }

    private static String TAG = "CmdUtil";
    private static final String CHMOD = "chmod 777 /sdcard/Android/data/lems -R";

    public static String exeCmd() {
        Process process = null;
        DataOutputStream os = null;
        String result = "";
        try {
            process = Runtime.getRuntime().exec("/system/xbin/su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(CHMOD + "\n");
            os.writeBytes("exit\n");
            os.flush();
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            Log.d(TAG, "exeCmd result_ori:" + result);
            process.waitFor();
            is.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 执行 cmd 指令
     * @param cmd
     * @return
     */
    public static String exeCmdStr(String cmd) {
        Log.d(TAG, "exeCmdStr:" + cmd);
        Process process = null;
        DataOutputStream os = null;
        String result = "";
        try {
            process = Runtime.getRuntime().exec("/system/xbin/su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            Log.d(TAG, "exeCmd result_ori:" + result);
            process.waitFor();
            is.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
