package com.example.androidmodel.tools.apkinfo.bean;

import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Date;

/**
 * @author kfflso
 * @data 2024/9/4 16:08
 * @plus:
 */
public class CertificateInfo {
    //签名者信息
    private String signerName;
    private String signerBlockFileName;
    private String signerFileName;
    private int index;

    // 证书类型 X.509
    private String certificateType;
    // 版本
    private int certificateVersion;
    // 序列号 -> 应用开发者签名序列号 16进制
    private String cfSerialNumber;
    // developer -> 应用开发者签名主题
    private String cfIX500PName;
    // 有效期始
    private Date certificateNotBefore;
    // 有效期末
    private Date certificateNotAfter;

    //公钥完整信息
    private PublicKey publicKey;
    // 公钥类型
    private String cfAlgorithmType;
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
    private String signHashMD5;
    // 应用Hash SHA-1
    private String signHashSHA1;
    // 应用Hash SHA256
    private String signHashSHA256;



    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    public String getSignerBlockFileName() {
        return signerBlockFileName;
    }

    public void setSignerBlockFileName(String signerBlockFileName) {
        this.signerBlockFileName = signerBlockFileName;
    }

    public String getSignerFileName() {
        return signerFileName;
    }

    public void setSignerFileName(String signerFileName) {
        this.signerFileName = signerFileName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public int getCertificateVersion() {
        return certificateVersion;
    }

    public void setCertificateVersion(int certificateVersion) {
        this.certificateVersion = certificateVersion;
    }

    public String getCfSerialNumber() {
        return cfSerialNumber;
    }

    public void setCfSerialNumber(String cfSerialNumber) {
        this.cfSerialNumber = cfSerialNumber;
    }

    public String getCfIX500PName() {
        return cfIX500PName;
    }

    public void setCfIX500PName(String cfIX500PName) {
        this.cfIX500PName = cfIX500PName;
    }

    public Date getCertificateNotBefore() {
        return certificateNotBefore;
    }

    public void setCertificateNotBefore(Date certificateNotBefore) {
        this.certificateNotBefore = certificateNotBefore;
    }

    public Date getCertificateNotAfter() {
        return certificateNotAfter;
    }

    public void setCertificateNotAfter(Date certificateNotAfter) {
        this.certificateNotAfter = certificateNotAfter;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getCfAlgorithmType() {
        return cfAlgorithmType;
    }

    public void setCfAlgorithmType(String cfAlgorithmType) {
        this.cfAlgorithmType = cfAlgorithmType;
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

    public String getSignHashMD5() {
        return signHashMD5;
    }

    public void setSignHashMD5(String signHashMD5) {
        this.signHashMD5 = signHashMD5;
    }

    public String getSignHashSHA1() {
        return signHashSHA1;
    }

    public void setSignHashSHA1(String signHashSHA1) {
        this.signHashSHA1 = signHashSHA1;
    }

    public String getSignHashSHA256() {
        return signHashSHA256;
    }

    public void setSignHashSHA256(String signHashSHA256) {
        this.signHashSHA256 = signHashSHA256;
    }
}
