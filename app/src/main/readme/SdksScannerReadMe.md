# 扫描App使用的第三方SDK

## 一、 概述

```Java
方案: 通过类加载器存储app启动后加载的类名,然后通过比对sdk的特征包名,判定使用的sdk
步骤: 
1.adb install apk;
2.launch the third app ---> SdksScanUtils.launchTargetApp   
3.修改framework层源码,获取类加载器,遍历类加载器的parent,存储类名为file
4.sdk的特征比对
```

## 二、 实现

### 1 launch the third app

```Java
// SdksScanUtils.launchTargetApp   

public void launchTargetApp(String packageName ) {
    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
    if (packageName == null || packageName.isEmpty()){
        Log.d(TAG,"无法启动应用: packageName is null or empty");
        return;
    }
    context.startActivity(launchIntent);
}
```

### 2 framework modify

#### 2.1 mainMethod

```Java
framework 修改文件: frameworks/base/core/java/android/app/ActivityThread.java  
performLaunchActivity方法 return之前; 开一个子线程,执行后续 saveLaunchAppClassNames方法
```

#### 2.2 saveLaunchAppClassNames

```Java
public void saveLaunchAppClassNames(){
    ClassLoader appClassLoader = getAppClassloader();
    if(appClassLoader != null){

        if(!appClassLoader.toString().contains("java.lang.BootClassLoader")){
            getAndSaveLoadedAppClassNames(appClassLoader);
        }
        ClassLoader parent = appClassLoader.getParent();
        while(parent != null){
            if(!parent.toString().contains("java.lang.BootClassLoader")){
                getAndSaveLoadedAppClassNames(parent);
            }
            parent = parent.getParent();
        }
    }
}
```

#### 2.3 getAppClassloader

```Java
public ClassLoader getAppClassloader() {
    Object currentActivityThread = invokeStaticMethod("android.app.ActivityThread", "currentActivityThread", new Class[]{}, new Object[]{});
    Object mBoundApplication = getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mBoundApplication");
    Object loadedApkInfo = getFieldOjbect("android.app.ActivityThread$AppBindData", mBoundApplication, "info");
    Application mApplication = (Application) getFieldOjbect("android.app.LoadedApk", loadedApkInfo, "mApplication");
    return mApplication.getClassLoader();
}
```

#### 2.4 getAndSaveLoadedAppClassNames

```Java
public void getAndSaveLoadedAppClassNames(ClassLoader classLoader){
        String zzz = "zzz";
        Object pathList_object = getFieldOjbect("dalvik.system.BaseDexClassLoader", classLoader, "pathList");
        Object[] ElementsArray = (Object[]) getFieldOjbect("dalvik.system.DexPathList", pathList_object, "dexElements");
        if(ElementsArray==null){return;}
        Field dexFile_fileField = null;
        try {
            dexFile_fileField = (Field) getClassField(classLoader, "dalvik.system.DexPathList$Element", "dexFile");
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
        Class DexFileClazz = null;
        try {
            DexFileClazz = classLoader.loadClass("dalvik.system.DexFile");
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
        Method getClassNameList_method = null;

        Method dumpMethodCode_method = null;

        for (Method field : DexFileClazz.getDeclaredMethods()) {
            if (field.getName().equals("getClassNameList")) {
                getClassNameList_method = field;
                getClassNameList_method.setAccessible(true);
            }
            if (field.getName().equals("antiydumpMethodCode")) {
                dumpMethodCode_method = field;
                dumpMethodCode_method.setAccessible(true);
            }
        }
        Field mCookiefield = getClassField(classLoader, "dalvik.system.DexFile", "mCookie");

        if (dumpMethodCode_method == null) {
            Log.e(zzz, "AntiyDumpError dumpMethodCode is null!!!");
            return ;
        }
        if (ElementsArray == null) {
            Log.e(zzz, "AntiyDumpError ElementsArray is null!!!");
            return ;
        }

        Log.e(zzz, "dalvik.system.DexPathList.ElementsArray.length:" + ElementsArray.length);
        for (int j = 0; j < ElementsArray.length; j++) {
            Object element = ElementsArray[j];
            Object dexfile = null;
            try {
                dexfile = (Object) dexFile_fileField.get(element);
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Error e) {
                e.printStackTrace();
            }
            if (dexfile == null) {
                Log.e(zzz, "AntiyDumpError dexfile is null");
                continue;
            }
            if (dexfile != null) {
                Object mcookie = getClassFieldObject(classLoader, "dalvik.system.DexFile", dexfile, "mCookie");
                if (mcookie == null) {
                    Log.e(zzz, "AntiyDumpError get resultmcookie is null");
                    Object mInternalCookie = getClassFieldObject(classLoader, "dalvik.system.DexFile", dexfile, "mInternalCookie");
                    if(mInternalCookie!=null)
                    {
                        mcookie=mInternalCookie;
                    }else{
                        Log.e(zzz, "AntiyDumpError get mInternalCookie is null");
                        continue;
                    }

                }
                String[] classnames_ = null;
                try {
                    classnames_ = (String[]) getClassNameList_method.invoke(dexfile, mcookie);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                } catch (Error e) {
                    e.printStackTrace();
                    continue;
                }
                if (classnames_ != null) {
                    //        String processName = ActivityThread.currentProcessName();
                    String processName = "com.example.androidmodel";
                    final String classNamesPath = "data/data/" + processName + "/kfflso/launchAppClassNames";
                    writeClassNameToFile(classNamesPath,classnames_);
//                    for (String eachclassname : classnames_) {
//                        loadClassAndInvoke(classLoader, eachclassname, dumpMethodCode_method);
//                    }
                }

            }
        }
    }
```

![classLoaderData](assets/classLoaderData.png)

#### 2.5 writeClassNameToFile

```Java
public void writeClassNameToFile(String filePath, String[] classNames) {
        // try params will auto close resource;
        try (
                RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
                FileChannel fileChannel = raf.getChannel()) {

            long fileSize = fileChannel.size();
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);

            StringBuilder existingContent = new StringBuilder();
            while (buffer.hasRemaining()) {
                existingContent.append((char) buffer.get());
            }
            StringBuilder content = new StringBuilder(existingContent.toString());
            for (String className : classNames) {
                if (content.toString().contains(className)) {
//                    Log.d("writeClassNameToFile", "Class name already exists: " + className);
                    continue;
                }
                content.append((content.length() == 0) ? "" : "\n")
                        .append(className);
//                Log.d("writeClassNameToFile", "Class name added: " + className);
            }
            buffer.clear();
            buffer.put(content.toString().getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```



```Java
//和两外一种方法应该都可以
//    public static void writeClassNameToFile(String filePath, String[] classNames) {
//        RandomAccessFile raf = null;
//        FileChannel fileChannel = null;
//        try {
//            raf = new RandomAccessFile(filePath, "rw");
//            fileChannel = raf.getChannel();
//
//            for (String className : classNames) {
//                long fileSize = fileChannel.size();
//                MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
//                StringBuilder existingContent = new StringBuilder();
//                while (buffer.hasRemaining()) {
//                    existingContent.append((char) buffer.get());
//                }
//                String content = existingContent.toString();
//                if (content.contains(className)) {
//                    Log.d(AntiyTAG, "Class name already exists: " + className);
//                    return;
//                }
//                String newEntry = (content.isEmpty() ? "" : "\n") + className;
//
//                long newSize = buffer.position() + newEntry.getBytes(StandardCharsets.UTF_8).length;
//                MappedByteBuffer newBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, newSize);
//
//                newBuffer.position(buffer.position());
//                newBuffer.put(newEntry.getBytes(StandardCharsets.UTF_8));
//                Log.d(AntiyTAG, "Class name added: " + className);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//
//            try {
//                if(fileChannel != null) fileChannel.close();
//                if (raf != null) raf.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//        }
//    }
```



### 3 sdk scan

```Java
package com.example.androidmodel.tools.sdkscan;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.androidmodel.tools.apkinfo.bean.Kfflso_FeaturesMap;

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
    private static Map<String, String> sdkFeaturesMap;

    public SdksScanUtil(Context context, String apkPath) {
        this.context = context;
        this.apkPath = apkPath;
        initData();
    }

    private void initData() {
        packageManager = context.getPackageManager();
        packageInfo = packageManager.getPackageArchiveInfo(apkPath, 0);
        sdkFeaturesMap = Kfflso_FeaturesMap.getInstance().getSdkFeaturesMap();
    }

    public Set<String> getSdks() {
        Set<String> sdks = new HashSet<>();
        String classNames = getClassNames();
        for (String key : sdkFeaturesMap.keySet()) {
            if (classNames.contains(key)) {
                sdks.add(sdkFeaturesMap.get(key));
            }
        }
        return sdks;
    }

    private String getClassNames() {
        String packageName = getPackageName();
        if (packageName == null || packageName.isEmpty()) return "";
        final String classNamesPath = "data/data/" + packageName + "/kfflso/launchAppClassNames";
        return getFileContent(classNamesPath);
    }

    private String getPackageName() {
        return packageInfo != null ? packageInfo.packageName : null;
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
```

### 4 FeatureMap

```Java
//app使用的第三方sdk的特征字典; key-value sdk_packageName_feature - sdk_id
private static final Map<String,String> sdkFeaturesMap = new HashMap<>();
public Map<String, String> getSdkFeaturesMap(){
        return sdkFeaturesMap;
}
private void initSdkFeaturesMap() {
        sdkFeaturesMap.put("zendesk.messaging",                     "1");
        sdkFeaturesMap.put("com.segment.analytics",                 "2");
        sdkFeaturesMap.put("org.prebid",                            "3");
        sdkFeaturesMap.put("org.osmdroid",                          "4");
        sdkFeaturesMap.put("com.iab.omid",                          "5");
        sdkFeaturesMap.put("com.newrelic.agent.android",            "6");
        sdkFeaturesMap.put("com.my.target",                         "7");
        sdkFeaturesMap.put("com.facebook.ads",                      "8");
}	
```

