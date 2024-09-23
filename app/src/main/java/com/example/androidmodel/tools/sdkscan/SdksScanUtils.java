package com.example.androidmodel.tools.sdkscan;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author kfflso
 * @data 2024/9/19 14:57
 * @plus:
 *      实现功能: 通过操作系统ActivityThread, 获取app的类加载器, 然后获取到pathList, DexFile... 将启动的 app加载过的类名保存在指定目录下
 *      实现步骤:   1.getPackageName ---> 获取将要启动的app的包名
 *                 2.launchTargetApp ---> 启动app
 *                 3.saveLaunchAppClassNames ---> 启动并保存app加载的类名
 *                 ---> 修改位置:  frameworks/base/core/java/android/app/ActivityThread.java  performLaunchActivity方法 return之前,记得开子线程
 *                      3.1 循环查找每一个 classLoader.getParent;
 *                      3.2 查找每一个 classLoader 加载过的类,并将类名保存在 data/data/ActivityThread.currentProcess.getProcessName()/kfflso/launchAppClassNames
 *                          writeClassNameToFile
 *
 *      参考文章:
 *      https://www.jianshu.com/p/7c59391f0658 // 理解类加载器
 *
 */
public class SdksScanUtils {
    private final String TAG = "SdksScanUtils";
    private Context context;
    private String apkPath;
    private PackageManager packageManager;
    private PackageInfo packageInfo;
    public SdksScanUtils(Context context, String apkPath) {
        this.context = context;
        this.apkPath = apkPath;
        initData();
    }
    private void initData(){
        packageManager = context.getPackageManager();
        packageInfo = packageManager.getPackageArchiveInfo(apkPath, 0);
//        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        sdkFeaturesMap = FeaturesMap.getInstance().getSdkFeaturesMap();
    }

    public String getPackageName(){
        return  packageInfo != null ? packageInfo.packageName : null;
    }

    public void launchTargetApp(String packageName ) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (packageName == null || packageName.isEmpty()){
            Log.d(TAG,"无法启动应用: packageName is null or empty");
            return;
        }
        context.startActivity(launchIntent);
    }

    //save to data/data/ActivityThread.currentProcess.getProcessName()/kfflso/launchAppClassNames
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

    public ClassLoader getAppClassloader() {
        Object currentActivityThread = invokeStaticMethod("android.app.ActivityThread", "currentActivityThread", new Class[]{}, new Object[]{});
        Object mBoundApplication = getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mBoundApplication");
        Object loadedApkInfo = getFieldOjbect("android.app.ActivityThread$AppBindData", mBoundApplication, "info");
        Application mApplication = (Application) getFieldOjbect("android.app.LoadedApk", loadedApkInfo, "mApplication");
        return mApplication.getClassLoader();
    }


    public boolean readFileAndCompare(String filePath, String compareString) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals(compareString)) { // trim() is used to remove trailing and leading whitespace
                    br.close();
                    return true;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void appendToFile(String filePath, String text) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true)); // 'true' for append mode
            bw.write(text);
            bw.newLine(); // This writes a newline
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



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

    //利用classloader 触发 classname的构造器和方法
//    public static void loadClassAndInvoke(ClassLoader appClassloader, String eachclassname, Method dumpMethodCode_method) {
//        String AntiyTAG = "AntiyTAG";
//        Log.i(AntiyTAG, "go into loadClassAndInvoke->" + "classname:" + eachclassname);
//        // class 黑名单
//        String processName = ActivityThread.currentProcessName();
//        final String blackListPath = "/data/data/"+processName+"/unshell/UnshellBlackList.yhs";
//
//
//        if (readFileAndCompare(blackListPath,eachclassname)){
//            Log.i(AntiyTAG,"blacklist dumped -> "+eachclassname);
//            return;
//        }
//        appendToFile(blackListPath,eachclassname);
//
//        Class resultclass = null;
//        try {
//            Log.v(AntiyTAG,"antiydump->try load class:" + eachclassname + "\n");
//            resultclass = appClassloader.loadClass(eachclassname);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        } catch (Error e) {
//            e.printStackTrace();
//            return;
//        }
//        if (resultclass != null) {
//            try {
//                Constructor<?> cons[] = resultclass.getDeclaredConstructors();
//                for (Constructor<?> constructor : cons) {
//                    if (dumpMethodCode_method != null) {
//                        try {
//                            dumpMethodCode_method.invoke(null, constructor);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            continue;
//                        } catch (Error e) {
//                            e.printStackTrace();
//                            continue;
//                        }
//                    } else {
//                        Log.e(AntiyTAG, "dumpMethodCode_method is null ");
//                    }
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } catch (Error e) {
//                e.printStackTrace();
//            }
//            try {
//                Method[] methods = resultclass.getDeclaredMethods();
//                if (methods != null) {
//                    Log.e(AntiyTAG, eachclassname + "--" + methods.length);
//                    for (Method m : methods) {
//                        if (dumpMethodCode_method != null) {
//                            try {
//                                dumpMethodCode_method.invoke(null, m);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                continue;
//                            } catch (Error e) {
//                                e.printStackTrace();
//                                continue;
//                            }
//                        } else {
//                            Log.e(AntiyTAG, "dumpMethodCode_method is null ");
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } catch (Error e) {
//                e.printStackTrace();
//            }
//        }
//    }
    public Object invokeStaticMethod(String class_name, String method_name, Class[] pareTyple, Object[] pareVaules) {
        try {
            Class obj_class = Class.forName(class_name);
            Method method = obj_class.getMethod(method_name, pareTyple);
            return method.invoke(null, pareVaules);
        } catch (SecurityException
                 | IllegalArgumentException
                 | IllegalAccessException
                 | NoSuchMethodException
                 | InvocationTargetException
                 | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getFieldOjbect(String class_name, Object obj, String filedName) {
        try {
            Class obj_class = Class.forName(class_name);
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (SecurityException
                 | NoSuchFieldException
                 | IllegalArgumentException
                 | IllegalAccessException
                 | ClassNotFoundException
                 | NullPointerException e) {
            e.printStackTrace();
        }
        return null;

    }

    public  Field getClassField(ClassLoader classloader, String class_name, String filedName) {
        try {
            Class obj_class = classloader.loadClass(class_name);//Class.forName(class_name);
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            return field;
        } catch (SecurityException
                 | NoSuchFieldException
                 | IllegalArgumentException
                 | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    public  Object getClassFieldObject(ClassLoader classloader, String class_name, Object obj,
                                             String filedName) {

        try {
            Class obj_class = classloader.loadClass(class_name);//Class.forName(class_name);
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            Object result = null;
            result = field.get(obj);
            return result;
            //field.setAccessible(true);
            //return field;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;

    }
}
