package com.example.androidmodel.tools.apkinfo;

import android.content.Context;

import com.example.androidmodel.tools.apkinfo.bean.Kfflso_ApkInfo;
import com.google.gson.Gson;

/**
 * @author kfflso
 * @data 2024/9/2 15:12
 * @plus: 获取apk的信息 + 签名信息; apksig
 */
public class Kfflso_ApkInfoImpl {
    private Context context;
    private String apkPath;
    private Kfflso_ApkInfo kfflsoApkInfo;

    public Kfflso_ApkInfoImpl(Context context, String apkPath) {
        this.context = context;
        this.apkPath = apkPath;
        setApkInfo(context,apkPath);
    }
    private void setApkInfo(Context context,String apkPath){
        Kfflso_ApkParserUtils kfflsoApkParserUtils = new Kfflso_ApkParserUtils(context,apkPath);
        kfflsoApkInfo = new Kfflso_ApkInfo();
        kfflsoApkInfo.setName(kfflsoApkParserUtils.getApkName());
        kfflsoApkInfo.setIcon(kfflsoApkParserUtils.getApkIconBase64());
        kfflsoApkInfo.setPackageName(kfflsoApkParserUtils.getApkPackageName());
        kfflsoApkInfo.setMd5(kfflsoApkParserUtils.getApkMD5());
        kfflsoApkInfo.setSha1(kfflsoApkParserUtils.getApkSHA1());
        kfflsoApkInfo.setSha256(kfflsoApkParserUtils.getApkSHA256());
        kfflsoApkInfo.setBuildTime(kfflsoApkParserUtils.getApkBuildTime());
        kfflsoApkInfo.setSize(kfflsoApkParserUtils.getApkSize());
        kfflsoApkInfo.setVersionCode(kfflsoApkParserUtils.getApkVersionCode());
        kfflsoApkInfo.setVersionName(kfflsoApkParserUtils.getApkVersionName());
        kfflsoApkInfo.setMinSdk(kfflsoApkParserUtils.getApkMinSDK());
        kfflsoApkInfo.setTargetSDK(kfflsoApkParserUtils.getApkTargetSDK());
        kfflsoApkInfo.setPermissions(kfflsoApkParserUtils.getApkRequestedPermission());
        kfflsoApkInfo.setShellInfo(kfflsoApkParserUtils.getShellInfo());
        kfflsoApkInfo.setCertificateV1(kfflsoApkParserUtils.getCertificateV1());
        kfflsoApkInfo.setCertificateV2(kfflsoApkParserUtils.getCertificateV2());
        kfflsoApkInfo.setCertificateV3(kfflsoApkParserUtils.getCertificateV3());
        kfflsoApkInfo.setCertificateV31(kfflsoApkParserUtils.getCertificateV31());
        kfflsoApkInfo.setCertificateV4(kfflsoApkParserUtils.getCertificateV4());
    }
    public Kfflso_ApkInfo getApkInfo(){
        return kfflsoApkInfo;
    }
    public String getApkInfoJson(){
        return new Gson().toJson(kfflsoApkInfo);
    }

}
