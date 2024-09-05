package com.example.androidmodel.tools.apkinfo.cache;

import com.android.apksig.ApkVerifier;
import com.android.apksig.apk.ApkFormatException;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kfflso
 * @data 2024/9/5 11:20
 * @plus:
 *      ApkVerifier 依赖库的缓存数据
 *      implementation("com.android.tools.build:apksig:8.2.2")
 */
public class ApkParserCache {
    //签名信息
    ApkVerifier.Result result;
    List<ApkVerifier.Result.V1SchemeSignerInfo> mV1SchemeSigners = new ArrayList<>();
    List<ApkVerifier.Result.V2SchemeSignerInfo> mV2SchemeSigners = new ArrayList<>();
    List<ApkVerifier.Result.V3SchemeSignerInfo> mV3SchemeSigners = new ArrayList<>();
    List<ApkVerifier.Result.V3SchemeSignerInfo> mV31SchemeSigners = new ArrayList<>();
    List<ApkVerifier.Result.V4SchemeSignerInfo> mV4SchemeSigners = new ArrayList<>();

    public ApkParserCache(String apkPath) {
        initData(apkPath);
    }

    private void initData(String apkPath){
        result = getApkVerifierResult(apkPath);
        mV1SchemeSigners = result.isVerifiedUsingV1Scheme() ? result.getV1SchemeSigners() : null;
        mV2SchemeSigners = result.isVerifiedUsingV1Scheme() ? result.getV2SchemeSigners() : null;
        mV3SchemeSigners = result.isVerifiedUsingV1Scheme() ? result.getV3SchemeSigners() : null;
        mV31SchemeSigners= result.isVerifiedUsingV1Scheme() ? result.getV31SchemeSigners() : null;
        mV4SchemeSigners = result.isVerifiedUsingV1Scheme() ? result.getV4SchemeSigners() : null;
        result = null;
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

    public List<ApkVerifier.Result.V1SchemeSignerInfo> getmV1SchemeSigners() {
        return mV1SchemeSigners;
    }

    public List<ApkVerifier.Result.V2SchemeSignerInfo> getmV2SchemeSigners() {
        return mV2SchemeSigners;
    }

    public List<ApkVerifier.Result.V3SchemeSignerInfo> getmV3SchemeSigners() {
        return mV3SchemeSigners;
    }

    public List<ApkVerifier.Result.V3SchemeSignerInfo> getmV31SchemeSigners() {
        return mV31SchemeSigners;
    }

    public List<ApkVerifier.Result.V4SchemeSignerInfo> getmV4SchemeSigners() {
        return mV4SchemeSigners;
    }
}
