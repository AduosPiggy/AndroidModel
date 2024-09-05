package com.example.androidmodel.tools.apkinfo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.android.apksig.ApkVerifier;
import com.example.androidmodel.tools.apkinfo.bean.CertificateInfo;
import com.example.androidmodel.tools.apkinfo.cache.ApkParserCache;
import com.example.androidmodel.tools.apkinfo.cache.ApkShellFeaturesCache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author kfflso
 * @data 2024/9/2 13:56
 * @plus:
 *      getApkName; // 应用名
 *      getApkIconBase64; // 应用图标 icon -> bytearray -> gzip -> base64
 *      getApkPackageName; // 应用包名
 *      getApkMD5 // apk MD5 的 hash
 *      getApkSHA1 // apk SHA1 的 hash
 *      getApkSHA256 // apk SHA256 的 hash
 *      getApkLastModifyTime //应用上次打包的时间, 检查的 AM 文件, 东八区时间
 *      getApkSize; // apk 文件大小
 *      getApkVersion; // apk 应用版本
 *      getApkTargetSDK; // apk targetSDK
 *      getApkRequestedPermission; // apk 申请的权限
 *      getStubInfo; // 应用采用 ? 公司的加固
 *      getCertificateV1 // apk 签名证书 所有 V1 的信息
 *      getCertificateV2 // apk 签名证书 所有 V2 的信息
 *      getCertificateV3 // apk 签名证书 所有 V3 的信息
 *      getCertificateV31 // apk 签名证书 所有 V31 的信息
 *      getCertificateV4 // apk 签名证书 所有 V4 的信息
 */

public class ApkParserUtils {
    private Context context;
    private String apkPath;
    private PackageManager packageManager;
    private PackageInfo packageInfo;
    private ApkParserCache apkParserCache;

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
        apkParserCache = new ApkParserCache(apkPath);
    }

    private Boolean isApkPathValid(String apkPath) {
        File file = new File(apkPath);
        return file.exists() && file.isFile() && file.getName().endsWith(".apk");
    }


    public String getApkName(){
        return packageInfo != null ? packageInfo.applicationInfo.loadLabel(packageManager).toString() : null;
    }
    public String getApkIconBase64(){
        return getApkIconBase64_();
    }

    public String getApkPackageName(){
        return packageInfo != null ? packageInfo.packageName : null;
    }

    public String getApkMD5(){
        File file = new File(apkPath);
        return getHash("MD5",file);
    }

    public String getApkSHA1(){
        File file = new File(apkPath);
        return getHash("SHA-1",file);
    }

    public String getApkSHA256(){
        File file = new File(apkPath);
        return getHash("SHA-256",file);
    }

    public String getApkLastModifyTime() {
        return getApkLastModifyTime__();
    }

    public long getApkSize(){
        try {
            File file = new File(apkPath);
            return file.length();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long getApkVersion(){
        return packageInfo != null ? packageInfo.getLongVersionCode() : 0;
    }

    public int getApkTargetSDK() {
        try {
            PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
            return packageInfo != null ? packageInfo.applicationInfo.targetSdkVersion : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String[] getApkRequestedPermission() {
        try {
            PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_PERMISSIONS);
            return packageInfo != null ? packageInfo.requestedPermissions : new String[0];
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public Set<String > getStubInfo(){
        return getStubInfo_();
    }

    public List<CertificateInfo> getCertificateV1(){
        return getCertificateV1_();
    }

    public List<CertificateInfo> getCertificateV2(){
        return getCertificateV2_();
    }

    public List<CertificateInfo> getCertificateV3(){
        return getCertificateV3_(apkParserCache.getmV3SchemeSigners());
    }

    public List<CertificateInfo> getCertificateV31(){
        return getCertificateV3_(apkParserCache.getmV31SchemeSigners());
    }

    public List<CertificateInfo> getCertificateV4(){
        return getCertificateV4_();
    }

    private String getCFHashMD5(String algorithm, byte[] certBytes){
       return getHash(algorithm,certBytes);
    }

    private String getCFHashSHA1(String algorithm, byte[] certBytes){
        return getHash(algorithm,certBytes);
    }

    private String getCFHashSHA256(String algorithm, byte[] certBytes){
        return getHash(algorithm,certBytes);
    }


    /**
     *
     * @return
     *  icon 压缩:
     *  1.获取 apk 的 Drawable icon;
     *  2.drawable -> bitmapDrawable ->  bitmap;
     *  3.bitmap -> byteArray;
     *  4.gzip
     *  5.base64
     *  icon解压: 先base64 去除 /n -> gzip 反解压 -> bitmap
     */
    public String getApkIconBase64_(){
        Drawable icon = getApkIcon();
        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
        byte[] byteArray = bitmapToByteArray(bitmap);
        byte[] gzipCompressed = gzipCompress(byteArray);
        return Base64.encodeToString(gzipCompressed, Base64.DEFAULT);
    }
    public Drawable getApkIcon(){
        return packageInfo != null ? packageInfo.applicationInfo.loadIcon(packageManager) : null;
    }
    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
    // gzip 压缩
    private byte[] gzipCompress(byte[] data) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteStream)) {
            gzipOutputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteStream.toByteArray();
    }

    /**
     *
     * @return
     *  1.获取到 apk 压缩文件中 AM 文件上次修改的时间, timestamp ms;
     *  2.将 timestamp 转换为东八区的时间
     */
    public String getApkLastModifyTime__() {
        long timestamp = getApkLastModifyTime_();
        if (timestamp == 0) {
            return "unKnown time: 0";
        }
        Date date = new Date(timestamp );
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getInstance();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf.format(date);
    }
    private long getApkLastModifyTime_() {
        long time = 0;
        try (ZipFile zipFile = new ZipFile(apkPath)) {
            ZipEntry manifestEntry = zipFile.getEntry("AndroidManifest.xml");
            if (manifestEntry != null) {
                time = manifestEntry.getLastModifiedTime().toMillis();
                zipFile.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     *
     * @param algorithm MD5 SHA1 SHA256
     * @param file 要计算 hash 的 file
     * @return file 经过 algorithm 方式计算出来的 hash;
     */
    private String getHash(String algorithm, File file) {
        try (FileInputStream fis = new FileInputStream(file) ) {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            byte[] hashBytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                //将字节 b 转换为两位十六进制的字符串
                sb.append(String.format("%02x", b));
            }
            return sb.toString().toUpperCase();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param algorithm MD5 SHA1 SHA256
     * @param certBytes certificate.getEncoded() apk 签名证书的编码形式
     * @return certBytes 经过 algorithm 计算的 hash
     */
    private String getHash(String algorithm, byte[] certBytes){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] algorithmCertBytes = digest.digest(certBytes);
            for(byte b : algorithmCertBytes){
                stringBuilder.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString().toUpperCase();
    }

    /**
     *
     * @return 加固的方式
     *  1.获取加固的特征规则 map : shellFeaturesMap( key-value : 加固特征文件名称-加固的方式);
     *  2.遍历获取 apkFile 中的文件, 查找 shellFeaturesMap 中是否包含加固特征的文件
     */
    public Set<String> getStubInfo_() {
        Set<String> stubInfo = new HashSet<>();
        Map<String, String> shellFeaturesMap = ApkShellFeaturesCache.getInstance().getShellFeaturesMap();
        try {
            File apkFile = new File(apkPath);
            ZipFile zipFile = new ZipFile(apkFile);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String path = entry.getName();
                String[] names = path.split("/");
                String name = names[(names.length - 1)];
                if ( shellFeaturesMap.containsKey(name) ){
                    stubInfo.add(shellFeaturesMap.get(name));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stubInfo;
    }

    /**
     *
     * @return 获取 apk 所有 V1 类型的证书信息
     */
    public List<CertificateInfo> getCertificateV1_(){
        List<CertificateInfo> certificateInfoList = new ArrayList<>();
        if(apkParserCache.getmV1SchemeSigners() == null){
            return certificateInfoList;
        }
        for(ApkVerifier.Result.V1SchemeSignerInfo schemeSignerInfo: apkParserCache.getmV1SchemeSigners()){
            String name = schemeSignerInfo.getName();
            List<X509Certificate> mCertChain = schemeSignerInfo.getCertificateChain();
            String blockFileName = schemeSignerInfo.getSignatureBlockFileName();
            String fileName = schemeSignerInfo.getSignatureFileName();
            for(X509Certificate x509Certificate : mCertChain){
                CertificateInfo certificateInfo = new CertificateInfo();
                certificateInfo.setName(name);
                certificateInfo.setBlockFileName(blockFileName);
                certificateInfo.setFileName(fileName);
                certificateInfo.setType(x509Certificate.getType());
                certificateInfo.setVersion(x509Certificate.getVersion());
                certificateInfo.setSerialNumber( String.format("0x%08X", x509Certificate.getSerialNumber()));
                certificateInfo.setDistinguishedName(x509Certificate.getIssuerX500Principal().getName());
                certificateInfo.setValidFrom(x509Certificate.getNotBefore());
                certificateInfo.setValidTo(x509Certificate.getNotAfter());
                PublicKey publicKey = x509Certificate.getPublicKey();
                certificateInfo.setPublicKey( Base64.encodeToString(publicKey.getEncoded(),Base64.DEFAULT) );
                certificateInfo.setPublicKeyType(publicKey.getAlgorithm());
                if(publicKey instanceof RSAPublicKey){
                    RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
                    BigInteger rsaModulus = rsaPublicKey.getModulus();
                    int rsaBitLength = rsaModulus.bitLength();
                    certificateInfo.setRsaPublicExponent(rsaPublicKey.getPublicExponent());
                    certificateInfo.setRsaModulus(rsaModulus);
                    certificateInfo.setRsaBitLength(rsaBitLength);
                }
                certificateInfo.setAlgorithms(x509Certificate.getSigAlgName());
                certificateInfo.setOID(x509Certificate.getSigAlgOID());
                try {
                    byte[] certBytes = x509Certificate.getEncoded();
                    certificateInfo.setMd5(getCFHashMD5("MD5",certBytes));
                    certificateInfo.setSha1(getCFHashSHA1("SHA1",certBytes) );
                    certificateInfo.setSha256(getCFHashSHA256("SHA256",certBytes));
                } catch (CertificateEncodingException e) {
                    throw new RuntimeException(e);
                }

                certificateInfoList.add(certificateInfo);
            }
        }
        return certificateInfoList;
    }
    public List<CertificateInfo> getCertificateV2_(){
        List<CertificateInfo> certificateInfoList = new ArrayList<>();
        if(apkParserCache.getmV2SchemeSigners() == null){
            return certificateInfoList;
        }
        for(ApkVerifier.Result.V2SchemeSignerInfo schemeSignerInfo: apkParserCache.getmV2SchemeSigners()){
            int signerIndex = schemeSignerInfo.getIndex();

            List<X509Certificate> mCerts = schemeSignerInfo.getCertificates();
            for(X509Certificate certificate : mCerts){
                CertificateInfo certificateInfo = new CertificateInfo();
                certificateInfo.setIndex(signerIndex);
                certificateInfo.setType(certificate.getType());
                certificateInfo.setVersion(certificate.getVersion());
                certificateInfo.setSerialNumber( String.format("0x%08X", certificate.getSerialNumber()));
                certificateInfo.setDistinguishedName(certificate.getIssuerX500Principal().getName());
                certificateInfo.setValidFrom(certificate.getNotBefore());
                certificateInfo.setValidTo(certificate.getNotAfter());

                PublicKey publicKey = certificate.getPublicKey();
                String cfAlgorithmType = publicKey.getAlgorithm();
//                certificateInfo.setPublicKey(publicKey);
                certificateInfo.setPublicKeyType(cfAlgorithmType);
                if(publicKey instanceof RSAPublicKey){
                    RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
                    BigInteger rsaModulus = rsaPublicKey.getModulus();
                    int rsaBitLength = rsaModulus.bitLength();
                    certificateInfo.setRsaPublicExponent(rsaPublicKey.getPublicExponent());
                    certificateInfo.setRsaModulus(rsaModulus);
                    certificateInfo.setRsaBitLength(rsaBitLength);
                }
                certificateInfo.setAlgorithms(certificate.getSigAlgName());
                certificateInfo.setOID(certificate.getSigAlgOID());
                try {
                    byte[] certBytes = certificate.getEncoded();
                    certificateInfo.setMd5(getCFHashMD5("MD5",certBytes));
                    certificateInfo.setSha1(getCFHashSHA1("SHA1",certBytes) );
                    certificateInfo.setSha256(getCFHashSHA256("SHA256",certBytes));
                } catch (CertificateEncodingException e) {
                    throw new RuntimeException(e);
                }

                certificateInfoList.add(certificateInfo);
            }
        }
        return certificateInfoList;
    }
    private List<CertificateInfo> getCertificateV3_(List<ApkVerifier.Result.V3SchemeSignerInfo> mV3_SchemeSigners){
        List<CertificateInfo> certificateInfoList = new ArrayList<>();
        if(mV3_SchemeSigners == null){
            return certificateInfoList;
        }
        for(ApkVerifier.Result.V3SchemeSignerInfo schemeSignerInfo: mV3_SchemeSigners){
            int signerIndex = schemeSignerInfo.getIndex();
            List<X509Certificate> mCerts = schemeSignerInfo.getCertificates();
            for(X509Certificate certificate : mCerts){
                CertificateInfo certificateInfo = new CertificateInfo();
                certificateInfo.setIndex(signerIndex);
                certificateInfo.setType(certificate.getType());
                certificateInfo.setVersion(certificate.getVersion());
                certificateInfo.setSerialNumber( String.format("0x%08X", certificate.getSerialNumber()));
                certificateInfo.setDistinguishedName(certificate.getIssuerX500Principal().getName());
                certificateInfo.setValidFrom(certificate.getNotBefore());
                certificateInfo.setValidTo(certificate.getNotAfter());

                PublicKey publicKey = certificate.getPublicKey();
                String cfAlgorithmType = publicKey.getAlgorithm();
//                certificateInfo.setPublicKey(publicKey);
                certificateInfo.setPublicKeyType(cfAlgorithmType);
                if(publicKey instanceof RSAPublicKey){
                    RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
                    BigInteger rsaModulus = rsaPublicKey.getModulus();
                    int rsaBitLength = rsaModulus.bitLength();
                    certificateInfo.setRsaPublicExponent(rsaPublicKey.getPublicExponent());
                    certificateInfo.setRsaModulus(rsaModulus);
                    certificateInfo.setRsaBitLength(rsaBitLength);
                }
                certificateInfo.setAlgorithms(certificate.getSigAlgName());
                certificateInfo.setOID(certificate.getSigAlgOID());
                try {
                    byte[] certBytes = certificate.getEncoded();
                    certificateInfo.setMd5(getCFHashMD5("MD5",certBytes));
                    certificateInfo.setSha1(getCFHashSHA1("SHA1",certBytes) );
                    certificateInfo.setSha256(getCFHashSHA256("SHA256",certBytes));
                } catch (CertificateEncodingException e) {
                    throw new RuntimeException(e);
                }

                certificateInfoList.add(certificateInfo);
            }
        }
        return certificateInfoList;
    }
    public List<CertificateInfo> getCertificateV4_(){
        List<CertificateInfo> certificateInfoList = new ArrayList<>();
        if(apkParserCache.getmV4SchemeSigners() == null){
            return certificateInfoList;
        }
        for(ApkVerifier.Result.V4SchemeSignerInfo schemeSignerInfo: apkParserCache.getmV4SchemeSigners()){
            int signerIndex = schemeSignerInfo.getIndex();
            List<X509Certificate> mCerts = schemeSignerInfo.getCertificates();
            for(X509Certificate certificate : mCerts){
                CertificateInfo certificateInfo = new CertificateInfo();
                certificateInfo.setIndex(signerIndex);
                certificateInfo.setType(certificate.getType());
                certificateInfo.setVersion(certificate.getVersion());
                certificateInfo.setSerialNumber( String.format("0x%08X", certificate.getSerialNumber()));
                certificateInfo.setDistinguishedName(certificate.getIssuerX500Principal().getName());
                certificateInfo.setValidFrom(certificate.getNotBefore());
                certificateInfo.setValidTo(certificate.getNotAfter());

                PublicKey publicKey = certificate.getPublicKey();
                String cfAlgorithmType = publicKey.getAlgorithm();
//                certificateInfo.setPublicKey(publicKey);
                certificateInfo.setPublicKeyType(cfAlgorithmType);
                if(publicKey instanceof RSAPublicKey){
                    RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
                    BigInteger rsaModulus = rsaPublicKey.getModulus();
                    int rsaBitLength = rsaModulus.bitLength();
                    certificateInfo.setRsaPublicExponent(rsaPublicKey.getPublicExponent());
                    certificateInfo.setRsaModulus(rsaModulus);
                    certificateInfo.setRsaBitLength(rsaBitLength);
                }
                certificateInfo.setAlgorithms(certificate.getSigAlgName());
                certificateInfo.setOID(certificate.getSigAlgOID());
                try {
                    byte[] certBytes = certificate.getEncoded();
                    certificateInfo.setMd5(getCFHashMD5("MD5",certBytes));
                    certificateInfo.setSha1(getCFHashSHA1("SHA1",certBytes) );
                    certificateInfo.setSha256(getCFHashSHA256("SHA256",certBytes));
                } catch (CertificateEncodingException e) {
                    throw new RuntimeException(e);
                }

                certificateInfoList.add(certificateInfo);
            }
        }
        return certificateInfoList;
    }



}
