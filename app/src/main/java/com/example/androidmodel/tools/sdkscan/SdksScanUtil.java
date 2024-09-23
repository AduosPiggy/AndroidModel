package com.example.androidmodel.tools.sdkscan;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.androidmodel.tools.apkinfo.bean.FeaturesMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author kfflso
 * @data 2024/9/23 15:48
 * @plus:
 */
public class SdksScanUtil {
    private final String TAG = "SdksScanUtils";
    private Context context;
    private String apkPath;
    private PackageManager packageManager;
    private PackageInfo packageInfo;
    //app使用的第三方sdk的特征字典; key-value sdk_packageName_feature - sdk_id
    private static Map<String,String> sdkFeaturesMap ;

    public SdksScanUtil(Context context, String apkPath) {
        this.context = context;
        this.apkPath = apkPath;
        initData();
    }
    private void initData(){
        packageManager = context.getPackageManager();
        packageInfo = packageManager.getPackageArchiveInfo(apkPath, 0);
        sdkFeaturesMap = FeaturesMap.getInstance().getSdkFeaturesMap();
    }

    public Set<String> getSdks(){
        Set<String> sdks = new HashSet<>();
        String classNames = getClassNames();
        for(String key : sdkFeaturesMap.keySet()){
            if(classNames.contains(key)){
                sdks.add(sdkFeaturesMap.get(key));
            }
        }
        return sdks;
    }
    private String getClassNames(){
        String packageName = getPackageName();
        if( packageName==null || packageName.isEmpty()) return "";
        final String classNamesPath = "data/data/" + packageName + "/kfflso/launchAppClassNames";
        return getFileContent(classNamesPath);
    }
    private String getPackageName(){
        return  packageInfo != null ? packageInfo.packageName : null;
    }
    private String getFileContent(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return "";
        }
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString().trim();
    }

}
