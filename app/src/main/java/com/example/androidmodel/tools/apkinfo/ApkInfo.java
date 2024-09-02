package com.example.androidmodel.tools.apkinfo;

import android.graphics.drawable.Drawable;

import java.util.Date;

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
    // 应用Hash md5
    private String apkHashMd5;
    // 应用Hash sha256
    private String apkHashSha256;
    // 应用开发者签名developer
    private String apkSignedDeveloper;
    // 应用开发者签名 KeyHash
    private String apkSignedKeyHash;
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

    public String getApkHashMd5() {
        return apkHashMd5;
    }

    public void setApkHashMd5(String apkHashMd5) {
        this.apkHashMd5 = apkHashMd5;
    }

    public String getApkHashSha256() {
        return apkHashSha256;
    }

    public void setApkHashSha256(String apkHashSha256) {
        this.apkHashSha256 = apkHashSha256;
    }

    public String getApkSignedDeveloper() {
        return apkSignedDeveloper;
    }

    public void setApkSignedDeveloper(String apkSignedDeveloper) {
        this.apkSignedDeveloper = apkSignedDeveloper;
    }

    public String getApkSignedKeyHash() {
        return apkSignedKeyHash;
    }

    public void setApkSignedKeyHash(String apkSignedKeyHash) {
        this.apkSignedKeyHash = apkSignedKeyHash;
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
}
