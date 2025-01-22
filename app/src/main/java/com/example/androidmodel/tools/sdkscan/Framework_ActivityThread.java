package com.example.androidmodel.tools.sdkscan;

import android.app.Application;
import android.util.Log;

import com.example.androidmodel.tools.FileUtils;
import com.example.androidmodel.tools.JsonUtils;
import com.example.androidmodel.tools.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author kfflso
 * @data 2024/9/29 15:22
 * @plus:
 *      framework modify: frameworks/base/core/java/android/app/ActivityThread.java  sdkScanThread()
 *      finished: dump all class names when system load the third app by loop its classLoader;
 *      premise:
 *          1.new system property in the operating system;
 *          2.system permissions related to read, write, (cp), and (mv).
 *      steps:
 *          1.check modify places;
 *              1) handleBindApplication
 *              2) performLaunchActivity; // exec performLaunchActivity as long as app need loading activity, which means it will exec multiple times;
 *          2.prepare sdkFeaturesMap ---> Kfflso_FeaturesMap.getSdkFeaturesMap();
 *          3.get ActivityThread.currentProcessName() for sdkFeaturesMapPathApk、sdkScanResPathApk、dumpClassNamesPath;
 *          4.loop classLoader and its parent; get its dexFiles; and load it, then record its classNames by reflection;
 *            to make this, you need to check file:
 *              1) "https://www.jianshu.com/p/7c59391f0658" read this to know basic load process;
 *              2) BaseDexClassLoader
 *              3) art/runtime/native/dalvik_system_DexFile.cc  ---> static jobjectArray DexFile_getClassNameList(JNIEnv* env, jclass, jobject cookie)
 *          5.please remind to sdkScanResultBuffer, which is important for optimizing process! This is a huge reduce for IO file;
 */
public class Framework_ActivityThread {

    private final String TAG = "Framework_ActivityThread";
    private Set<String> sdkScanResultSet = new HashSet<>();


    public Framework_ActivityThread() {
        startTest();
    }
    private void startTest(){
        // 注意调用时机
        // 1.handleBindApplication
        // 2.performLaunchActivity; // 每当加载一个 new activity, 就会调用1次;
        sdkScanThread();
    }
//    private void handleBindApplication(AppBindData data) {
//        // ...
//            sendMessage(H.SET_CONTENT_CAPTURE_OPTIONS_CALLBACK, data.appInfo.packageName);
//            mInitialApplication = app;
//            // =====================================
//            sdkScanThread();
//            // =====================================
//            final boolean updateHttpProxy;
//        // ...
//    }

//    private Activity performLaunchActivity(ActivityClientRecord r, Intent customIntent) {
//        //...
//        // =====================================
//        sdkScanThread();
//        // =====================================
//        return activity;
//    }


    public void sdkScanThread() {
        if (shouldDumpClasses()) {
            new Thread(() -> {
                try {
                    Log.e(TAG, "DumpClassesList start!");
                    sdkScan();
                    Log.e(TAG, "DumpClassesList complete!");
                }catch (Throwable e){
                    Log.e(TAG, "error while DumpClassesList", e);
                }
            }).start();
        }
    }

    // 检测是否应该 dump className, 这里可通过判断有指定系统属性,就开始 dump
    public static boolean shouldDumpClasses() {
//        boolean ret = false;
//        String processName = ActivityThread.currentProcessName();
//        String target = SystemProperties.get("xxx.dumpclass.name");
//        if(processName != null && processName.equals(target)){
//            Log.e(TAG, "dumpclass target: "+target);
//            ret = true;
//        }
//        return ret;
        return true;
    }

    public void sdkScan(){
//        String processName = ActivityThread.currentProcessName();
        String processName = "com.example.testApp";//你启动的app的包名,这里恰巧和进程名称一致
        String sdkFeaturesMapPathApk = "/data/data/" + processName + "/dumpClassName/sdkFeaturesMap.txt";
        String sdkScanResPathApk = "/data/data/" + processName + "/dumpClassName/sdkScanResult.txt";
        String dumpClassNamesPath = "/data/data/" + processName + "/dumpClassName/apkDumpClassNames.txt";
        FileUtils.checkAndCreateFile(sdkFeaturesMapPathApk);
        FileUtils.checkAndCreateFile(sdkScanResPathApk);
        String json = FileUtils.readFileToString(sdkFeaturesMapPathApk);
        HashMap<String,String> sdkFeaturesMap = JsonUtils.jsonStrToHashMap(json);
        if(sdkScanResultSet.size() == 0){
            String sdkResult = FileUtils.readFileToString(sdkScanResPathApk);
            for(String result : sdkResult.split("\n")){
                sdkScanResultSet.add(result);
            }
        }
        ClassLoader parentClassloader = getClassloader();
        while (parentClassloader != null){
            if(!parentClassloader.toString().contains("java.lang.BootClassLoader")) {
                sdkScanImpl(parentClassloader, sdkScanResPathApk, sdkFeaturesMap,sdkScanResultSet, dumpClassNamesPath);
            }
            parentClassloader = parentClassloader.getParent();
        }
        sdkScanResultSet.clear();
    }

    public static ClassLoader getClassloader() {
        ClassLoader resultClassloader = null;
        Object currentActivityThread = ReflectionUtils.invokeStaticMethod("android.app.ActivityThread", "currentActivityThread", new Class[]{}, new Object[]{});
        Object mBoundApplication = ReflectionUtils.getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mBoundApplication");
        Application mInitialApplication = (Application) ReflectionUtils.getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mInitialApplication");
        Object loadedApkInfo = ReflectionUtils.getFieldOjbect("android.app.ActivityThread$AppBindData", mBoundApplication, "info");
        Application mApplication = (Application) ReflectionUtils.getFieldOjbect("android.app.LoadedApk", loadedApkInfo, "mApplication");
        resultClassloader = mApplication.getClassLoader();
        return resultClassloader;
    }

    public void sdkScanImpl(ClassLoader appClassloader, String sdkScanResPathApk, HashMap<String,String> sdkFeaturesMap,Set<String> sdkScanResultSet, String dumpClassNamesPath) {
        Object pathList_object = ReflectionUtils.getFieldOjbect("dalvik.system.BaseDexClassLoader", appClassloader, "pathList");
        Object[] ElementsArray = (Object[]) ReflectionUtils.getFieldOjbect("dalvik.system.DexPathList", pathList_object, "dexElements");
        if (ElementsArray == null) {
            Log.e(TAG, "DumpError ElementsArray is null!!!");
            return;
        }
        Field dexFile_fileField;
        try {
            dexFile_fileField = (Field) ReflectionUtils.getClassField(appClassloader, "dalvik.system.DexPathList$Element", "dexFile");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Method getClassNameList_method;
        Method dumpMethodCode_method;
        try {
            Class<?> DexFileClazz = appClassloader.loadClass("dalvik.system.DexFile");
            getClassNameList_method = DexFileClazz.getDeclaredMethod("getClassNameList", Object.class);
            getClassNameList_method.setAccessible(true);
            dumpMethodCode_method = DexFileClazz.getDeclaredMethod("dumpMethodCode", Object.class);
            dumpMethodCode_method.setAccessible(true);
        } catch (Exception e){
            e.printStackTrace();
            return;
        }

        Log.e(TAG, "dalvik.system.DexPathList.ElementsArray.length:" + ElementsArray.length);
        for (Object element : ElementsArray) {
            Object dexfile;
            try {
                dexfile = (Object) dexFile_fileField.get(element);
                if (dexfile == null) {
                    Log.e(TAG, "DumpError dexfile is null");
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Object mcookie = ReflectionUtils.getClassFieldObject(appClassloader, "dalvik.system.DexFile", dexfile, "mCookie");
            if (mcookie == null) {
                Log.e(TAG, "DumpError get mcookie is null");
                Object mInternalCookie = ReflectionUtils.getClassFieldObject(appClassloader, "dalvik.system.DexFile", dexfile, "mInternalCookie");
                if (mInternalCookie == null) {
                    Log.e(TAG, "DumpError get mInternalCookie is null");
                    continue;
                }
                mcookie = mInternalCookie;
            }
            String[] classnames;
            try {
                classnames = (String[]) getClassNameList_method.invoke(dexfile, mcookie);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            if (classnames != null) {
                for(String className : classnames){
                    for(String key : sdkFeaturesMap.keySet()){
                        if(key.isEmpty()){
                            continue;
                        }
                        if(className.contains(key)){
                            String value = sdkFeaturesMap.get(key);
                            if(value.isEmpty()) {
                                Log.e(TAG,"err: sdkFeaturesMap values contains empty value!");
                            }
                            if(!sdkScanResultSet.contains(value)){
                                sdkScanResultSet.add(value);
                                //todo: consider use value or className as final result;
                                FileUtils.appendToFile(sdkScanResPathApk,value);//应该用这个
//                                appendToFile(sdkScanResPathApk,className);//测试看效果
                            }
                        }
                    }
                }
                FileUtils.appendToFile(dumpClassNamesPath, String.join("\n", classnames));//所有 dumped class names
            }
        }
    }

}
