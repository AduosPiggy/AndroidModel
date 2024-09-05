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
    private String signAlgorithms;
    // 签名 OID
    private String signOID;
    // 应用Hash md5
    private String MD5;
    // 应用Hash SHA-1
    private String SHA1;
    // 应用Hash SHA256
    private String SHA256;

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

    public String getSignAlgorithms() {
        return signAlgorithms;
    }

    public void setSignAlgorithms(String signAlgorithms) {
        this.signAlgorithms = signAlgorithms;
    }

    public String getSignOID() {
        return signOID;
    }

    public void setSignOID(String signOID) {
        this.signOID = signOID;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public String getSHA1() {
        return SHA1;
    }

    public void setSHA1(String SHA1) {
        this.SHA1 = SHA1;
    }

    public String getSHA256() {
        return SHA256;
    }

    public void setSHA256(String SHA256) {
        this.SHA256 = SHA256;
    }
}
