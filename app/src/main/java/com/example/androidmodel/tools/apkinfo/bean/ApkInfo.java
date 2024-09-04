package com.example.androidmodel.tools.apkinfo.bean;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * @author kfflso
 * @data 2024/9/2 13:59
 * @plus:
 */
public class ApkInfo {
    // 应用名
    private String apkName;
    // 应用图标
    private Drawable apkIcon;
    // 应用包名
    private String apkPackageName;

    // 应用上一次打包时间 apk中 AndroidManifest.xml 上次修改时间; 单位s
    private String apkLastPackedTime;
    // apk 文件大小 单位 byte
    private long apkSize;
    // apk 应用版本
    private long apkVersion;
    // apk targetSDK
    private int apkTargetSDK;
    // 应用申请权限
    private String[] apkRequestedPermission;
    // 应用加固信息
    private String apkHardenInfo;

    //签名证书 V1
    private List<CertificateInfo> certificateV1;
    //签名证书 V2
    private List<CertificateInfo> certificateV2;
    //签名证书 V3
    private List<CertificateInfo> certificateV3;
    //签名证书 V31
    private List<CertificateInfo> certificateV31;
    //签名证书 V4
    private List<CertificateInfo> certificateV4;

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public Drawable getApkIcon() {
        return apkIcon;
    }

    public void setApkIcon(Drawable apkIcon) {
        this.apkIcon = apkIcon;
    }

    public String getApkPackageName() {
        return apkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }

    public String getApkLastPackedTime() {
        return apkLastPackedTime;
    }

    public void setApkLastPackedTime(String apkLastPackedTime) {
        this.apkLastPackedTime = apkLastPackedTime;
    }

    public long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }

    public long getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(long apkVersion) {
        this.apkVersion = apkVersion;
    }

    public int getApkTargetSDK() {
        return apkTargetSDK;
    }

    public void setApkTargetSDK(int apkTargetSDK) {
        this.apkTargetSDK = apkTargetSDK;
    }

    public String[] getApkRequestedPermission() {
        return apkRequestedPermission;
    }

    public void setApkRequestedPermission(String[] apkRequestedPermission) {
        this.apkRequestedPermission = apkRequestedPermission;
    }

    public String getApkHardenInfo() {
        return apkHardenInfo;
    }

    public void setApkHardenInfo(String apkHardenInfo) {
        this.apkHardenInfo = apkHardenInfo;
    }

    public List<CertificateInfo> getCertificateV1() {
        return certificateV1;
    }

    public void setCertificateV1(List<CertificateInfo> certificateV1) {
        this.certificateV1 = certificateV1;
    }

    public List<CertificateInfo> getCertificateV2() {
        return certificateV2;
    }

    public void setCertificateV2(List<CertificateInfo> certificateV2) {
        this.certificateV2 = certificateV2;
    }

    public List<CertificateInfo> getCertificateV3() {
        return certificateV3;
    }

    public void setCertificateV3(List<CertificateInfo> certificateV3) {
        this.certificateV3 = certificateV3;
    }

    public List<CertificateInfo> getCertificateV31() {
        return certificateV31;
    }

    public void setCertificateV31(List<CertificateInfo> certificateV31) {
        this.certificateV31 = certificateV31;
    }

    public List<CertificateInfo> getCertificateV4() {
        return certificateV4;
    }

    public void setCertificateV4(List<CertificateInfo> certificateV4) {
        this.certificateV4 = certificateV4;
    }
}
