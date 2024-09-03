package com.example.androidmodel.tools.apkinfo;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

/**
 * @author kfflso
 * @data 2024/9/2 15:12
 * @plus:
 */
public class ApkInfoImpl {
    private Context context;
    private String apkPath;
    private ApkInfo apkInfo;

    public ApkInfoImpl(Context context, String apkPath) {
        this.context = context;
        this.apkPath = apkPath;
        setApkInfo(context,apkPath);
    }
    private void setApkInfo(Context context,String apkPath){
        ApkParserUtils apkParserUtils = new ApkParserUtils(context,apkPath);
        apkInfo = new ApkInfo();
        apkInfo.setApkName(apkParserUtils.getApkName());
        apkInfo.setApkIcon(apkParserUtils.getApkIcon());
        apkInfo.setApkPackageName(apkParserUtils.getApkPackageName());
        apkInfo.setApkHashMd5(apkParserUtils.getApkHashMd5());
        apkInfo.setApkHashSha256(apkParserUtils.getApkHashSha256());
        apkInfo.setApkSignedDeveloper(apkParserUtils.getApkSignedDeveloper());
        apkInfo.setApkSignedSerialNumber(apkParserUtils.getApkSignedSerialNumber());
        Log.d("twy001", "developer: " + new Gson().toJson(apkInfo.getApkSignedDeveloper()));
        Log.d("twy001", "----------------------------------------------------------------------------------");
        Log.d("twy001", "developer: " + new Gson().toJson(apkInfo.getApkSignedSerialNumber()));

        apkInfo.setApkLastPackedTime(apkParserUtils.getFormattedPackedTime());
        apkInfo.setApkSize(apkParserUtils.getApkSize());
        apkInfo.setApkVersion(apkParserUtils.getApkVersion());
        apkInfo.setApkTargetSDK(apkParserUtils.getApkTargetSDK());
        apkInfo.setApkRequestedPermission(apkParserUtils.getApkRequestedPermission());
        apkInfo.setApkHardenInfo(apkParserUtils.getApkHardenInfo());
        Log.d("ApkInfo",new Gson().toJson(apkInfo));
    }
    public ApkInfo getApkInfo(){
        return apkInfo;
    }

}
