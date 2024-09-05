package com.example.androidmodel.tools.apkinfo.bean;

import java.util.List;
import java.util.Set;

/**
 * @author kfflso
 * @data 2024/9/2 13:59
 * @plus:
 */
public class ApkInfo {
    // 应用名
    private String name;
    //应用图标
    private String icon;
    // 应用包名
    private String packageName;
    private String md5;
    private String sha1;
    private String sha256;

    // 应用上一次打包时间 apk中 AndroidManifest.xml 上次修改时间; 单位s
    private String lastModifyTime;
    // apk 文件大小 单位 bytes
    private long size;
    // apk 应用版本
    private long version;
    // apk targetSDK
    private int targetSDK;
    // 应用申请权限
    private String[] permissions;
    // 应用加固信息
    private Set<String> stubInfo;

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

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
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

    public Set<String> getStubInfo() {
        return stubInfo;
    }

    public void setStubInfo(Set<String> stubInfo) {
        this.stubInfo = stubInfo;
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
