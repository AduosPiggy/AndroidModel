package com.example.androidmodel.tools.apkinfo.bean;

import java.util.List;
import java.util.Set;

/**
 * @author kfflso
 * @data 2024/9/2 13:59
 * @plus:
 */
public class ApkInfo {
    // apk name
    private String name;
    // apk icon
    private String icon;
    // apk package name
    private String packageName;
    // apk file MD5 Hash
    private String md5;
    // apk file sha1 Hash
    private String sha1;
    // apk file sha256 Hash
    private String sha256;

    // apk build Time, 东八区时间
    private String buildTime;
    // apk size; unit: bytes
    private long size;
    // apk versionCode
    private long versionCode;
    // apk versionName
    private String versionName;
    // apk minSdk
    private int minSdk;
    // apk targetSDK
    private int targetSDK;
    // apk AM文件申请的权限
    private String[] permissions;
    // apk 加固的方式
    private Set<String> shellInfo;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(long versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getMinSdk() {
        return minSdk;
    }

    public void setMinSdk(int minSdk) {
        this.minSdk = minSdk;
    }

    public int getTargetSDK() {
        return targetSDK;
    }

    public void setTargetSDK(int targetSDK) {
        this.targetSDK = targetSDK;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    public Set<String> getShellInfo() {
        return shellInfo;
    }

    public void setShellInfo(Set<String> shellInfo) {
        this.shellInfo = shellInfo;
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
