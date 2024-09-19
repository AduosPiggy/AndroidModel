package com.example.androidmodel.tools.apkinfo;

import com.example.androidmodel.tools.apkinfo.bean.FeaturesMap;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author kfflso
 * @data 2024/9/18 16:51
 * @plus:
 */
public class ApkParserUsedSdkUtils {
    private String apkPath = "";

    public ApkParserUsedSdkUtils(String apkPath) {
        this.apkPath = apkPath;
    }
    /**
     *
     * @return 加固的方式
     *  1.获取加固的特征规则 map : sdkFeaturesMap( key-value : sdk 特征文件包名称 - sdk_id);
     *  2.遍历获取 apkFile 中的文件, 查找 sdkFeaturesMap 中是否包含加固特征的文件
     */
    public Set<String> getSdkUsed_() {
        Set<String> sdk_ids = new HashSet<>();
        Map<String, String> sdkFeaturesMap = FeaturesMap.getInstance().getSdkFeaturesMap();
        try {
            File apkFile = new File(apkPath);
            ZipFile zipFile = new ZipFile(apkFile);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String path = entry.getName();
//                if(path.startsWith("com")){
                    String tmp = path.replace("/",".");
                    for(String key : sdkFeaturesMap.keySet()){
                        if(key.isEmpty()){
                            continue;
                        }
                        if(tmp.startsWith(key)){
                            sdk_ids.add(sdkFeaturesMap.get(key));
                        }
                    }
//                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sdk_ids;
    }
}
