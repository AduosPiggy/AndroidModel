package com.example.androidmodel.tools.apkinfo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.android.apksig.ApkVerifier;
import com.android.apksig.apk.ApkFormatException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // 应用Hash md5
    public String getApkHashMd5(){
        return getApkHash("MD5");
    }

    // 应用Hash sha256
    public String getApkHashSha256(){
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

    public String getApkSignedDeveloper() {
        JsonObject jsonObject = new JsonObject();
        String developer = "";

        if(mV1SchemeSigners != null){
            ApkVerifier.Result.V1SchemeSignerInfo V = mV1SchemeSigners.get(0);
            X509Certificate certificate = V.getCertificate();
            developer = certificate.getIssuerX500Principal().getName();
        }
        jsonObject.addProperty("V1",developer.isEmpty()? "" : developer);
        developer = "";

        if(mV2SchemeSigners != null){
            ApkVerifier.Result.V2SchemeSignerInfo V = mV2SchemeSigners.get(0);
            X509Certificate certificate = V.getCertificate();
            developer = certificate.getIssuerX500Principal().getName();
        }
        jsonObject.addProperty("V2",developer.isEmpty()? "" : developer);
        developer = "";

        if(mV3SchemeSigners != null){
            ApkVerifier.Result.V3SchemeSignerInfo V = mV3SchemeSigners.get(0);
            X509Certificate certificate = V.getCertificate();
            developer = certificate.getIssuerX500Principal().getName();
        }
        jsonObject.addProperty("V3",developer.isEmpty()? "" : developer);
        developer = "";

        if(mV31SchemeSigners != null){
            ApkVerifier.Result.V3SchemeSignerInfo V = mV31SchemeSigners.get(0);
            X509Certificate certificate = V.getCertificate();
            developer = certificate.getIssuerX500Principal().getName();
        }
        jsonObject.addProperty("V31",developer.isEmpty()? "" : developer);
        developer = "";

        if(mV4SchemeSigners != null){
            ApkVerifier.Result.V4SchemeSignerInfo V = mV4SchemeSigners.get(0);
            X509Certificate certificate = V.getCertificate();
            developer = certificate.getIssuerX500Principal().getName();
        }
        jsonObject.addProperty("V4",developer.isEmpty()? "" : developer);

        return jsonObject.toString();
    }


    public String getApkSignedSerialNumber() {
        JsonObject jsonObject = new JsonObject();
        StringBuilder stringBuilder = new StringBuilder();
        String serial = "";
        if(mV1SchemeSigners != null){
            ApkVerifier.Result.V1SchemeSignerInfo V = mV1SchemeSigners.get(0);
            X509Certificate certificate = V.getCertificate();
            serial = String.valueOf(certificate.getSerialNumber());
        }
        jsonObject.addProperty("V1",serial.isEmpty() ? "" : serial);
        serial = "";

        if(mV1SchemeSigners != null){
            ApkVerifier.Result.V2SchemeSignerInfo V = mV2SchemeSigners.get(0);
            X509Certificate certificate = V.getCertificate();
            serial = String.valueOf(certificate.getSerialNumber());


        }
        jsonObject.addProperty("V2",serial.isEmpty() ? "" : serial);
        serial = "";

        if(mV1SchemeSigners != null){
            ApkVerifier.Result.V3SchemeSignerInfo V = mV3SchemeSigners.get(0);
            X509Certificate certificate = V.getCertificate();
            serial = String.valueOf(certificate.getSerialNumber());

        }
        jsonObject.addProperty("V3",serial.isEmpty() ? "" : serial);
        serial = "";

        if(mV1SchemeSigners != null){
            ApkVerifier.Result.V3SchemeSignerInfo V = mV31SchemeSigners.get(0);
            X509Certificate certificate = V.getCertificate();
            serial = String.valueOf(certificate.getSerialNumber());
        }
        jsonObject.addProperty("V31",serial.isEmpty() ? "" : serial);
        serial = "";

        if(mV1SchemeSigners != null){
            ApkVerifier.Result.V4SchemeSignerInfo V = mV4SchemeSigners.get(0);
            X509Certificate certificate = V.getCertificate();
            serial = String.valueOf(certificate.getSerialNumber());
        }
        jsonObject.addProperty("V4",serial.isEmpty() ? "" : serial);

        return jsonObject.toString();
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
}
