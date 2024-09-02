package com.example.androidmodel.tools.apkinfo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            return sb.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 应用开发者签名 developer
    public String getApkSignedDeveloper(){
        String developerSignature = "";
        SigningInfo signingInfo = getApkSigningInfo();
        if(signingInfo != null ){
            Signature[] signatures = signingInfo.getApkContentsSigners();
            if(signatures != null && signatures.length > 0){
                Signature signature = signatures[0];
                developerSignature = Base64.encodeToString(signature.toByteArray(),Base64.DEFAULT);
            }
        }
        return developerSignature;
    }

    // 应用开发者签名 KeyHash
    public String getApkSignedKeyHash(){
        String keyHash = "";
        try {
            SigningInfo signingInfo = getApkSigningInfo();
            if(signingInfo != null){
                Signature[] signatures = signingInfo.getApkContentsSigners();
                if(signatures != null && signatures.length > 0){
                    Signature signature = signatures[0];
                    MessageDigest md = null;
                    md = MessageDigest.getInstance("SHA-256");
                    md.update(signature.toByteArray());
                    keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }finally {
            return keyHash;
        }
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
    public String getApkHardenInfo(){
        return "";
    }
}
