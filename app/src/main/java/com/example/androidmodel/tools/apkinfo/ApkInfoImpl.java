package com.example.androidmodel.tools.apkinfo;

import android.content.Context;

import com.example.androidmodel.tools.apkinfo.bean.ApkInfo;
import com.google.gson.Gson;

/**
 * @author kfflso
 * @data 2024/9/2 15:12
 * @plus: 获取apk的信息 + 签名信息; apksig
 */
public class ApkInfoImpl {
    private Context context;
    private String apkPath;
    private ApkInfo apkInfo;

    public ApkInfoImpl(Context context, String apkPath) {
        this.context = context;
        this.apkPath = apkPath;
        setApkInfo(context, apkPath);
    }

    private void setApkInfo(Context context, String apkPath) {
        ApkParserUtils apkParserUtils = new ApkParserUtils(context, apkPath);
        apkInfo = new ApkInfo();
        apkInfo.setName(apkParserUtils.getApkName());
        apkInfo.setIcon(apkParserUtils.getApkIconBase64());
        apkInfo.setPackageName(apkParserUtils.getApkPackageName());
        apkInfo.setMd5(apkParserUtils.getApkMD5());
        apkInfo.setSha1(apkParserUtils.getApkSHA1());
        apkInfo.setSha256(apkParserUtils.getApkSHA256());
        apkInfo.setBuildTime(apkParserUtils.getApkBuildTime());
        apkInfo.setSize(apkParserUtils.getApkSize());
        apkInfo.setVersionCode(apkParserUtils.getApkVersionCode());
        apkInfo.setVersionName(apkParserUtils.getApkVersionName());
        apkInfo.setMinSdk(apkParserUtils.getApkMinSDK());
        apkInfo.setTargetSDK(apkParserUtils.getApkTargetSDK());
        apkInfo.setPermissions(apkParserUtils.getApkRequestedPermission());
        apkInfo.setShellInfo(apkParserUtils.getShellInfo());
        apkInfo.setCertificateV1(apkParserUtils.getCertificateV1());
        apkInfo.setCertificateV2(apkParserUtils.getCertificateV2());
        apkInfo.setCertificateV3(apkParserUtils.getCertificateV3());
        apkInfo.setCertificateV31(apkParserUtils.getCertificateV31());
        apkInfo.setCertificateV4(apkParserUtils.getCertificateV4());
    }

    public ApkInfo getApkInfo() {
        return apkInfo;
    }

    public String getApkInfoJson() {
        return new Gson().toJson(apkInfo);
    }

}
