package com.example.androidmodel.tools.apkinfo.bean;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author kfflso
 * @data 2024/9/4 16:08
 * @plus:
 */
public class Kfflso_CertificateInfo {
    //apk 签名者的名称
    private String name;
    // apk 签名文件的名称
    private String blockFileName;
    // apk 签名文件的名称
    private String fileName;
    // apk 签名者的索引（在证书链中的位置）
    private int index;
    // apk 签名证书类型 X.509
    private String type;
    // apk 签名证书版本号
    private int version;
    // apk 签名证书序列号 16进制
    private String serialNumber;
    // apk 签名者信息
    private String distinguishedName;
    // apk 签名证书有效起始日期
    private Date validFrom;
    // apk 签名证书有效终止日期
    private Date validTo;
    // apk 公钥信息，Base64 编码的证书公钥数据
    private String publicKey;
    // apk 公钥的类型
    private String publicKeyType;
    // apk RSA 公钥指数
    private BigInteger rsaPublicExponent;
    // apk RSA 公钥模数大小(位)
    private int rsaBitLength;
    // apk RSA 公钥模数（Base64 编码的十六进制表示）
    private String rsaModulus;
    // apk 签名使用的算法
    private String algorithms;
    // apk 签名的对象标识符（OID），表示签名算法的类型
    private String OID;
    // apk 签名证书 MD5 Hash 值
    private String md5;
    // apk 签名证书 SHA-1 Hash
    private String sha1;
    // 	apk 签名证书 SHA-256 Hash
    private String sha256;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlockFileName() {
        return blockFileName;
    }

    public void setBlockFileName(String blockFileName) {
        this.blockFileName = blockFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKeyType() {
        return publicKeyType;
    }

    public void setPublicKeyType(String publicKeyType) {
        this.publicKeyType = publicKeyType;
    }

    public BigInteger getRsaPublicExponent() {
        return rsaPublicExponent;
    }

    public void setRsaPublicExponent(BigInteger rsaPublicExponent) {
        this.rsaPublicExponent = rsaPublicExponent;
    }

    public int getRsaBitLength() {
        return rsaBitLength;
    }

    public void setRsaBitLength(int rsaBitLength) {
        this.rsaBitLength = rsaBitLength;
    }

    public String getRsaModulus() {
        return rsaModulus;
    }

    public void setRsaModulus(String rsaModulus) {
        this.rsaModulus = rsaModulus;
    }

    public String getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(String algorithms) {
        this.algorithms = algorithms;
    }

    public String getOID() {
        return OID;
    }

    public void setOID(String OID) {
        this.OID = OID;
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
}
