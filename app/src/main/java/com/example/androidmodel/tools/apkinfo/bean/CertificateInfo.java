package com.example.androidmodel.tools.apkinfo.bean;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author kfflso
 * @data 2024/9/4 16:08
 * @plus:
 */
public class CertificateInfo {
    //签名者信息
    private String name;
    private String blockFileName;
    private String fileName;
    private int index;

    // 证书类型 X.509
    private String type;
    // 版本
    private int version;
    // 序列号 -> 应用开发者签名序列号 16进制
    private String serialNumber;
    // developer -> 应用开发者签名主题
    private String distinguishedName;
    // 有效期始
    private Date validFrom;
    // 有效期末
    private Date validTo;

//    //公钥完整信息
//    private PublicKey publicKey;
    private String publicKey;
    // 公钥类型
    private String publicKeyType;
    //公钥指数
    private BigInteger rsaPublicExponent;
    //公钥模数大小(位)
    private int rsaBitLength;
    //公钥模数
    private BigInteger rsaModulus;

    // 签名算法
    private String algorithms;
    // 签名 OID
    private String OID;
    // 应用Hash md5
    private String md5;
    // 应用Hash SHA-1
    private String sha1;
    // 应用Hash SHA256
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

    public BigInteger getRsaModulus() {
        return rsaModulus;
    }

    public void setRsaModulus(BigInteger rsaModulus) {
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
