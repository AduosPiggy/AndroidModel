package com.example.androidmodel.tools.apkinfo;

import android.content.Context;
import android.util.Log;

import com.example.androidmodel.tools.apkinfo.bean.ApkInfo;
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
//        apkInfo.setApkIcon(apkParserUtils.getApkIcon());
        apkInfo.setApkIconBase64(apkParserUtils.getApkIconBase64());
        apkInfo.setApkPackageName(apkParserUtils.getApkPackageName());
        apkInfo.setApkLastPackedTime(apkParserUtils.getFormattedPackedTime());
        apkInfo.setApkSize(apkParserUtils.getApkSize());
        apkInfo.setApkVersion(apkParserUtils.getApkVersion());
        apkInfo.setApkTargetSDK(apkParserUtils.getApkTargetSDK());
        apkInfo.setApkRequestedPermission(apkParserUtils.getApkRequestedPermission());
        apkInfo.setApkStubInfo(apkParserUtils.getStubInfo());
        apkInfo.setCertificateV1(apkParserUtils.getCertificateV1());
        apkInfo.setCertificateV2(apkParserUtils.getCertificateV2());
        apkInfo.setCertificateV3(apkParserUtils.getCertificateV3());
        apkInfo.setCertificateV31(apkParserUtils.getCertificateV31());
        apkInfo.setCertificateV4(apkParserUtils.getCertificateV4());
    }
    public ApkInfo getApkInfo(){
        return apkInfo;
    }
    public String getApkInfoJson(){
        return new Gson().toJson(apkInfo);
    }

}
