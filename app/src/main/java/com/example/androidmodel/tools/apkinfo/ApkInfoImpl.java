package com.example.androidmodel.tools.apkinfo;

import android.content.Context;

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
        apkInfo.setName(apkParserUtils.getApkName());
        apkInfo.setIcon(apkParserUtils.getApkIconBase64());
        apkInfo.setPackageName(apkParserUtils.getApkPackageName());
        apkInfo.setMD5(apkParserUtils.getApkHashMD5());
        apkInfo.setSHA1(apkParserUtils.getApkHashSHA1());
        apkInfo.setSHA256(apkParserUtils.getApkHashSHA256());
        apkInfo.setLastModifyTime(apkParserUtils.getFormattedPackedTime());
        apkInfo.setSize(apkParserUtils.getApkSize());
        apkInfo.setVersion(apkParserUtils.getApkVersion());
        apkInfo.setTargetSDK(apkParserUtils.getApkTargetSDK());
        apkInfo.setPermissions(apkParserUtils.getApkRequestedPermission());
        apkInfo.setStubInfo(apkParserUtils.getStubInfo());
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
