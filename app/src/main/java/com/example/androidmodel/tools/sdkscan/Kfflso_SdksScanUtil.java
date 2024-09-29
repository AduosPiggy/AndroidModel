package com.example.androidmodel.tools.sdkscan;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.androidmodel.tools.Kfflso_CmdUtil;
import com.example.androidmodel.tools.Kfflso_PackageUtil;
import com.example.androidmodel.tools.Kfflso_SystemPropUtils;
import com.example.androidmodel.tools.apkinfo.bean.Kfflso_FeaturesMap;
import com.example.androidmodel.tools.logs.Kfflso_LogsUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author kfflso
 * @data 2024/9/23 15:48
 * @plus:
 *     应用层代码:
 *     重点是 apkPath 参数
 *     流程:
 *    ---> 下载apk完成
 *    ---> 安装apk完成
 *    ---> 设置系统属性 setProp("antiy.dumpclass.name", packageName) ---> 在 framework 层决定是要做 dump class names 还是 unShell
 *    ---> app 在apk对应目录下写入特征 featureMap.txt ;
 *    ---> app 系统权限 ---> cp /data/data/com.example.app/dump/sdkFeaturesMap.txt .../com.xxx.apk/...;
 *    ---> 启动app
 *    ---> 加载 featureMap到framework层
 *    ---> dump class names,有一条就和 featuresMap 比对,有新的sdks时,利用sdkScanResultBuffer结果去重后就写Txt;
 *    ---> app 查看 sdk scan 结果: mv SdkScanResPathApk SdkScanResPathTdc; 然后在app中查看
 *
 */
public class Kfflso_SdksScanUtil {
    private final String TAG = "SdksScanUtils";
    private Context context;
    private String apkPath;
    private PackageManager packageManager;
    private PackageInfo packageInfo;
    //app使用的第三方sdk的特征字典; key-value sdk_packageName_feature - sdk_id
    private Map<String, String> sdkFeaturesMap;
    private String packageNameApk;
    private String packageNameTdc;
    private String sdkFeaturesMapPathApk;
    private String sdkFeaturesMapPathTdc;
    private String sdkScanResPathApk;
    private String sdkScanResPathTdc;

    public Kfflso_SdksScanUtil(Context context, String apkPath) {
        this.context = context;
        this.apkPath = apkPath;
        initData();
    }
    private void initData() {
        packageManager = context.getPackageManager();
        packageInfo = packageManager.getPackageArchiveInfo(apkPath, 0);
        packageNameApk = packageInfo != null ? packageInfo.packageName : "";
        if(packageNameApk.isEmpty()){
            Kfflso_LogsUtils.logToFileAsync(TAG,"parse apk name error, please check apk name!");
            return;
        }
        packageNameTdc = "com.antiy.tdc";
        sdkFeaturesMap = Kfflso_FeaturesMap.getInstance().getSdkFeaturesMap();
        sdkFeaturesMapPathApk = "/data/data/" + packageNameApk + "/dumpClassName/sdkFeaturesMap.txt";
        sdkFeaturesMapPathTdc = "/data/data/" + packageNameTdc + "/dumpClassName/sdkFeaturesMap.txt";
        sdkScanResPathApk = "/data/data/" + packageNameApk + "/dumpClassName/sdkScanResult.txt";
        sdkScanResPathTdc = "/data/data/" + packageNameTdc + "/dumpClassName/sdkScanResult.txt";
    }

    public String testTaskFlow(){
        if (packageNameApk.isEmpty()) {
            Kfflso_LogsUtils.logToFileAsync(TAG,"packageName is empty");
            return "";
        }
        Kfflso_SystemPropUtils.setProp("zzz.dumpclass.name", packageNameApk);
        writeSdksFeaturesToFile();
        launchTargetApp();
        //fwk load sdkFeaturesMap
        //fwk check
        String sdks = getSdkResults();
        return sdks;
    }

    /**
     *
     * @return launch app by packageName
     */
    public void launchTargetApp( ) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageNameApk);
        context.startActivity(launchIntent);
    }

    public String writeSdksFeaturesToFile(){
        String sdksJson = new Gson().toJson(sdkFeaturesMap);
        mkFile(sdkFeaturesMapPathTdc);
        writeStringToFile(sdksJson,sdkFeaturesMapPathTdc);
        String sdkFeaturesMapPathDirApk = sdkFeaturesMapPathApk.substring(0,sdkFeaturesMapPathApk.lastIndexOf('/') );
        String cmd = "mkdir -p " + sdkFeaturesMapPathDirApk;
        String errInfo = Kfflso_CmdUtil.execCmdPlus("/system/xbin/asu", "root", "sh", "-c", cmd);
        //如果存在,就只修改文件时间
        cmd = "touch " + sdkFeaturesMapPathApk;
        errInfo = Kfflso_CmdUtil.execCmdPlus("/system/xbin/asu", "root", "sh", "-c", cmd);
        //覆盖式cp
        cmd = "cp " + sdkFeaturesMapPathTdc + " " + sdkFeaturesMapPathApk;
        errInfo = Kfflso_CmdUtil.execCmdPlus("/system/xbin/asu", "root", "sh", "-c", cmd);
        int uid = Kfflso_PackageUtil.getInstance(context).getPackageUid(packageNameApk);
        cmd  = "chown -R " + uid + ":" + uid + " " + sdkFeaturesMapPathDirApk; // 设置用户和用户组 uid 和 gid
        errInfo = Kfflso_CmdUtil.execCmdPlus("/system/xbin/asu", "root", "sh", "-c", cmd);
        return errInfo;
    }

    /**
     *
     * @param filePath
     * 创建文件
     */
    public void mkFile(String filePath){
        if(filePath.isEmpty()){
            return;
        }
        try {
            File file = new File(filePath);
            if(file.exists() && file.length() > 0){
                return;
            }
            File parent = file.getParentFile();
            if(!parent.exists()){
                parent.mkdirs();
            }
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param string 要写入的String
     * @param filePath 最终写入的路径
     *      先确保文件存在, 然后 覆盖 写入 String 到 filePath
     */
    public void writeStringToFile(String string, String filePath){
        try (FileOutputStream fos = new FileOutputStream(filePath,false)){
            byte[] bytes = string.getBytes();
            fos.write(bytes);
            fos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return 获取filePath中的文件内容
     */
    public String getSdkResults() {
        File file = new File(sdkScanResPathApk);
        if (!file.exists() || !file.isFile()) {
            return "";
        }
        String errInfo = Kfflso_CmdUtil.execCmdPlus("/system/xbin/asu", "root", "sh", "-c", "cp " + sdkScanResPathApk + " " + sdkScanResPathTdc);
        if(!errInfo.isEmpty()){
            Kfflso_LogsUtils.logToFileAsync(TAG,"CommandTask.exec err: " + errInfo);
            return "";
        }
        File file_ = new File(sdkScanResPathTdc);
        if (!file_.exists() || !file_.isFile()) {
            return "";
        }
        return readFileToString(sdkScanResPathTdc);

    }


    public String readFileToString(String filePath){
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString().trim();
    }
    public HashMap<String,String> jsonStrToHashMap(String json){
        HashMap<String,String> result = new HashMap<>();
        if(json.isEmpty()) return result;
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> it = jsonObject.keys();
            while(it.hasNext()){
                String key = it.next();
                result.put(key,jsonObject.getString(key));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
