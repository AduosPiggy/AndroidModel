package com.example.androidmodel.tools.apkinfo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.SigningInfo;
import android.graphics.drawable.Drawable;

import com.android.apksig.ApkVerifier;
import com.android.apksig.apk.ApkFormatException;
import com.example.androidmodel.tools.apkinfo.bean.CertificateInfo;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author kfflso
 * @data 2024/9/2 13:56
 * @plus:
 *     getApkName; // 应用名
 *     getApkIcon; // 应用图标
 *     getApkPackageName; // 应用包名
 *     getApkHashMd5; // 应用Hash md5
 *     getApkHashSha256; // 应用Hash sha256
 *     getApkSignedDeveloper; // 应用开发者签名developer
 *     getApkSignedKeyHash; // 应用开发者签名 KeyHash
 *     getApkPackedTime; // 应用打包时间
 *     getApkSize; // apk 文件大小
 *     getApkVersion; // apk 应用版本
 *     getApkTargetSDK; // apk targetSDK
 *     getApkRequestedPermission; // 应用申请权限
 *     getApkHardenInfo; // 应用加固信息
 */
public class ApkParserUtils {
    private Context context;
    private String apkPath;
    private PackageManager packageManager;
    private PackageInfo packageInfo;

    //签名信息
    ApkVerifier.Result result;
    List<ApkVerifier.Result.V1SchemeSignerInfo> mV1SchemeSigners = new ArrayList<>();
    List<ApkVerifier.Result.V2SchemeSignerInfo> mV2SchemeSigners = new ArrayList<>();
    List<ApkVerifier.Result.V3SchemeSignerInfo> mV3SchemeSigners = new ArrayList<>();
    List<ApkVerifier.Result.V3SchemeSignerInfo> mV31SchemeSigners = new ArrayList<>();
    List<ApkVerifier.Result.V4SchemeSignerInfo> mV4SchemeSigners = new ArrayList<>();

    public ApkParserUtils(Context context, String apkPath) {
        this.context = context;
        if(isApkPathValid(apkPath)){
            this.apkPath = apkPath;
        }else {
            throw new IllegalArgumentException("apkPath: " + apkPath + " is error!! ");
        }
        packageManager = context.getPackageManager();
        //获取对应apkPath的 packageInfo
        packageInfo = packageManager.getPackageArchiveInfo(apkPath, 0);

//        packageInfo = packageManager.getPackageArchiveInfo(apkPath,
//                PackageManager.GET_ACTIVITIES |
//                        PackageManager.GET_SERVICES |
//                        PackageManager.GET_RECEIVERS |
//                        PackageManager.GET_PROVIDERS);
        result = getApkVerifierResult(apkPath);
        mV1SchemeSigners = result.isVerifiedUsingV1Scheme() ? result.getV1SchemeSigners() : null;
        mV2SchemeSigners = result.isVerifiedUsingV1Scheme() ? result.getV2SchemeSigners() : null;
        mV3SchemeSigners = result.isVerifiedUsingV1Scheme() ? result.getV3SchemeSigners() : null;
        mV31SchemeSigners= result.isVerifiedUsingV1Scheme() ? result.getV31SchemeSigners() : null;
        mV4SchemeSigners = result.isVerifiedUsingV1Scheme() ? result.getV4SchemeSigners() : null;
        result = null;
    }
    /**
     * 检测 APK 路径是否有效
     * @return true：路径有效； false：路径无效
     */
    private Boolean isApkPathValid(String apkPath) {
        File file = new File(apkPath);
        return file.exists() && file.isFile() && file.getName().endsWith(".apk");
    }

    // 应用名
    public String getApkName(){
        return packageInfo != null ? packageInfo.applicationInfo.loadLabel(packageManager).toString() : null;
    }

    // 应用图标
    public Drawable getApkIcon(){
        return packageInfo != null ? packageInfo.applicationInfo.loadIcon(packageManager) : null;
    }

    // 应用包名
    public String getApkPackageName(){
        return packageInfo != null ? packageInfo.packageName : null;
    }

    private ApkVerifier.Result getApkVerifierResult(String apkPath){
        ApkVerifier.Result result = null;
        try {
            File apkFile = new File(apkPath);
            ApkVerifier apkVerifier = new ApkVerifier.Builder(apkFile).build();
            result = apkVerifier.verify();
        } catch (IOException | ApkFormatException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private SigningInfo getApkSigningInfo(){
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkPath,PackageManager.GET_SIGNING_CERTIFICATES);
        return packageInfo != null ? packageInfo.signingInfo : null;
    }

    // 应用上一次打包时间
    public String getFormattedPackedTime() {
        long timestamp = getApkLastPackedTime();
        if (timestamp == 0) {
            return "unKnown time: 0";
        }
        Date date = new Date(timestamp );
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getInstance();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }
    // 应用上一次打包时间 单位ms
    private long getApkLastPackedTime() {
        long time = 0;
        try (ZipFile zipFile = new ZipFile(apkPath)) {
            ZipEntry manifestEntry = zipFile.getEntry("AndroidManifest.xml");
            if (manifestEntry != null) {
//                time = manifestEntry.getTime();
                time = manifestEntry.getLastModifiedTime().toMillis();
                zipFile.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return time;
    }

    // apk 文件大小
    public long getApkSize(){
        try {
            File file = new File(apkPath);
            return file.length();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // apk 应用版本
    public long getApkVersion(){
        return packageInfo != null ? packageInfo.getLongVersionCode() : 0;
    }

    // apk targetSDK
    public int getApkTargetSDK() {
        try {
            PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
            return packageInfo != null ? packageInfo.applicationInfo.targetSdkVersion : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    // 应用申请权限
    public String[] getApkRequestedPermission() {
        try {
            PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_PERMISSIONS);
            return packageInfo != null ? packageInfo.requestedPermissions : new String[0];
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    // 应用加固信息
    // todo 根据加固的壳的特征, 返回采用的加固类型
    // https://blog.csdn.net/g5703129/article/details/85054405
    public String getApkHardenInfo(){
        return "";
    }

    // 应用Hash MD5
    private String getSignHashMD5(){
        return getApkHash("MD5");
    }
    // 应用Hash SHA-1
    private String getSignHashSHA1(){
        return getApkHash("SHA-1");
    }
    // 应用Hash SHA-256
    private String getSignHashSHA256(){
        return getApkHash("SHA-256");
    }

    private String getApkHash(String algorithm) {
        try (FileInputStream fis = new FileInputStream(new File(apkPath))) {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            byte[] hashBytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString().toUpperCase();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<CertificateInfo> getCertificateV1(){
        List<CertificateInfo> certificateInfoList = new ArrayList<>();
        if(mV1SchemeSigners == null){
            return certificateInfoList;
        }
        for(ApkVerifier.Result.V1SchemeSignerInfo schemeSignerInfo: mV1SchemeSigners){
            String signerName = schemeSignerInfo.getName();
            List<X509Certificate> mCertChain = schemeSignerInfo.getCertificateChain();
            String signerBlockFileName = schemeSignerInfo.getSignatureBlockFileName();
            String signerFileName = schemeSignerInfo.getSignatureFileName();

            String signHashMD5 = getSignHashMD5();
            String signHashSHA1 = getSignHashSHA1();
            String signHashSHA256 = getSignHashSHA256();
            for(X509Certificate certificate : mCertChain){
                CertificateInfo certificateInfo = new CertificateInfo();
                certificateInfo.setSignerName(signerName);
                certificateInfo.setSignerBlockFileName(signerBlockFileName);
                certificateInfo.setSignerFileName(signerFileName);
                certificateInfo.setCertificateType(certificate.getType());
                certificateInfo.setCertificateVersion(certificate.getVersion());
                certificateInfo.setCfSerialNumber( String.format("0x%08X", certificate.getSerialNumber()));
                certificateInfo.setCfIX500PName(certificate.getIssuerX500Principal().getName());
                certificateInfo.setCertificateNotBefore(certificate.getNotBefore());
                certificateInfo.setCertificateNotAfter(certificate.getNotAfter());

                PublicKey publicKey = certificate.getPublicKey();
                String cfAlgorithmType = publicKey.getAlgorithm();
                certificateInfo.setPublicKey(publicKey);
                certificateInfo.setCfAlgorithmType(cfAlgorithmType);
                if(publicKey instanceof RSAPublicKey){
                    RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
                    BigInteger rsaModulus = rsaPublicKey.getModulus();
                    int rsaBitLength = rsaModulus.bitLength();
                    certificateInfo.setRsaPublicExponent(rsaPublicKey.getPublicExponent());
                    certificateInfo.setRsaModulus(rsaModulus);
                    certificateInfo.setRsaBitLength(rsaBitLength);
                }
                certificateInfo.setSignAlgorithms(certificate.getSigAlgName());
                certificateInfo.setSignOID(certificate.getSigAlgOID());
                certificateInfo.setSignHashMD5(signHashMD5);
                certificateInfo.setSignHashSHA1(signHashSHA1);
                certificateInfo.setSignHashSHA256(signHashSHA256);

                certificateInfoList.add(certificateInfo);
            }
        }
        return certificateInfoList;
    }
    public List<CertificateInfo> getCertificateV2(){
        List<CertificateInfo> certificateInfoList = new ArrayList<>();
        if(mV2SchemeSigners == null){
            return certificateInfoList;
        }
        for(ApkVerifier.Result.V2SchemeSignerInfo schemeSignerInfo: mV2SchemeSigners){
            int signerIndex = schemeSignerInfo.getIndex();

            List<X509Certificate> mCerts = schemeSignerInfo.getCertificates();
            String signHashMD5 = getSignHashMD5();
            String signHashSHA1 = getSignHashSHA1();
            String signHashSHA256 = getSignHashSHA256();
            for(X509Certificate certificate : mCerts){
                CertificateInfo certificateInfo = new CertificateInfo();
                certificateInfo.setIndex(signerIndex);
                certificateInfo.setCertificateType(certificate.getType());
                certificateInfo.setCertificateVersion(certificate.getVersion());
                certificateInfo.setCfSerialNumber( String.format("0x%08X", certificate.getSerialNumber()));
                certificateInfo.setCfIX500PName(certificate.getIssuerX500Principal().getName());
                certificateInfo.setCertificateNotBefore(certificate.getNotBefore());
                certificateInfo.setCertificateNotAfter(certificate.getNotAfter());

                PublicKey publicKey = certificate.getPublicKey();
                String cfAlgorithmType = publicKey.getAlgorithm();
                certificateInfo.setPublicKey(publicKey);
                certificateInfo.setCfAlgorithmType(cfAlgorithmType);
                if(publicKey instanceof RSAPublicKey){
                    RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
                    BigInteger rsaModulus = rsaPublicKey.getModulus();
                    int rsaBitLength = rsaModulus.bitLength();
                    certificateInfo.setRsaPublicExponent(rsaPublicKey.getPublicExponent());
                    certificateInfo.setRsaModulus(rsaModulus);
                    certificateInfo.setRsaBitLength(rsaBitLength);
                }
                certificateInfo.setSignAlgorithms(certificate.getSigAlgName());
                certificateInfo.setSignOID(certificate.getSigAlgOID());
                certificateInfo.setSignHashMD5(signHashMD5);
                certificateInfo.setSignHashSHA1(signHashSHA1);
                certificateInfo.setSignHashSHA256(signHashSHA256);

                certificateInfoList.add(certificateInfo);
            }
        }
        return certificateInfoList;
    }
    public List<CertificateInfo> getCertificateV3(){
        return getCertificateV3_(mV3SchemeSigners);
    }
    public List<CertificateInfo> getCertificateV31(){
        return getCertificateV3_(mV31SchemeSigners);
    }
    private List<CertificateInfo> getCertificateV3_(List<ApkVerifier.Result.V3SchemeSignerInfo> mV3_SchemeSigners){
        List<CertificateInfo> certificateInfoList = new ArrayList<>();
        if(mV3_SchemeSigners == null){
            return certificateInfoList;
        }
        for(ApkVerifier.Result.V3SchemeSignerInfo schemeSignerInfo: mV3_SchemeSigners){
            int signerIndex = schemeSignerInfo.getIndex();
            List<X509Certificate> mCerts = schemeSignerInfo.getCertificates();
            String signHashMD5 = getSignHashMD5();
            String signHashSHA1 = getSignHashSHA1();
            String signHashSHA256 = getSignHashSHA256();
            for(X509Certificate certificate : mCerts){
                CertificateInfo certificateInfo = new CertificateInfo();
                certificateInfo.setIndex(signerIndex);
                certificateInfo.setCertificateType(certificate.getType());
                certificateInfo.setCertificateVersion(certificate.getVersion());
                certificateInfo.setCfSerialNumber( String.format("0x%08X", certificate.getSerialNumber()));
                certificateInfo.setCfIX500PName(certificate.getIssuerX500Principal().getName());
                certificateInfo.setCertificateNotBefore(certificate.getNotBefore());
                certificateInfo.setCertificateNotAfter(certificate.getNotAfter());

                PublicKey publicKey = certificate.getPublicKey();
                String cfAlgorithmType = publicKey.getAlgorithm();
                certificateInfo.setPublicKey(publicKey);
                certificateInfo.setCfAlgorithmType(cfAlgorithmType);
                if(publicKey instanceof RSAPublicKey){
                    RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
                    BigInteger rsaModulus = rsaPublicKey.getModulus();
                    int rsaBitLength = rsaModulus.bitLength();
                    certificateInfo.setRsaPublicExponent(rsaPublicKey.getPublicExponent());
                    certificateInfo.setRsaModulus(rsaModulus);
                    certificateInfo.setRsaBitLength(rsaBitLength);
                }
                certificateInfo.setSignAlgorithms(certificate.getSigAlgName());
                certificateInfo.setSignOID(certificate.getSigAlgOID());
                certificateInfo.setSignHashMD5(signHashMD5);
                certificateInfo.setSignHashSHA1(signHashSHA1);
                certificateInfo.setSignHashSHA256(signHashSHA256);

                certificateInfoList.add(certificateInfo);
            }
        }
        return certificateInfoList;
    }
    public List<CertificateInfo> getCertificateV4(){
        List<CertificateInfo> certificateInfoList = new ArrayList<>();
        if(mV4SchemeSigners == null){
            return certificateInfoList;
        }
        for(ApkVerifier.Result.V4SchemeSignerInfo schemeSignerInfo: mV4SchemeSigners){
            int signerIndex = schemeSignerInfo.getIndex();
            List<X509Certificate> mCerts = schemeSignerInfo.getCertificates();
            String signHashMD5 = getSignHashMD5();
            String signHashSHA1 = getSignHashSHA1();
            String signHashSHA256 = getSignHashSHA256();
            for(X509Certificate certificate : mCerts){
                CertificateInfo certificateInfo = new CertificateInfo();
                certificateInfo.setIndex(signerIndex);
                certificateInfo.setCertificateType(certificate.getType());
                certificateInfo.setCertificateVersion(certificate.getVersion());
                certificateInfo.setCfSerialNumber( String.format("0x%08X", certificate.getSerialNumber()));
                certificateInfo.setCfIX500PName(certificate.getIssuerX500Principal().getName());
                certificateInfo.setCertificateNotBefore(certificate.getNotBefore());
                certificateInfo.setCertificateNotAfter(certificate.getNotAfter());

                PublicKey publicKey = certificate.getPublicKey();
                String cfAlgorithmType = publicKey.getAlgorithm();
                certificateInfo.setPublicKey(publicKey);
                certificateInfo.setCfAlgorithmType(cfAlgorithmType);
                if(publicKey instanceof RSAPublicKey){
                    RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
                    BigInteger rsaModulus = rsaPublicKey.getModulus();
                    int rsaBitLength = rsaModulus.bitLength();
                    certificateInfo.setRsaPublicExponent(rsaPublicKey.getPublicExponent());
                    certificateInfo.setRsaModulus(rsaModulus);
                    certificateInfo.setRsaBitLength(rsaBitLength);
                }
                certificateInfo.setSignAlgorithms(certificate.getSigAlgName());
                certificateInfo.setSignOID(certificate.getSigAlgOID());
                certificateInfo.setSignHashMD5(signHashMD5);
                certificateInfo.setSignHashSHA1(signHashSHA1);
                certificateInfo.setSignHashSHA256(signHashSHA256);

                certificateInfoList.add(certificateInfo);
            }
        }
        return certificateInfoList;
    }



}
