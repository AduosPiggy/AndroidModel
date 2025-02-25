package com.example.androidmodel.tools;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.Base64;
import android.view.SurfaceControl;


import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("all")
public class ScreenshotUtil {

    private static final String TAG = "ScreenshotUtil";

    public static Method getAsBitmapMethod() throws NoSuchMethodException, ClassNotFoundException {
        Class<?> screenshotHardwareBufferClazz = Class.forName("android.view.SurfaceControl$ScreenshotHardwareBuffer");
        return screenshotHardwareBufferClazz.getDeclaredMethod("asBitmap");
    }

    public static Object makeCaptureArgs(Rect cropRect) throws Exception {
        Class<?> surfaceControlClazz = SurfaceControl.class;
        Method getPrimaryPhysicalDisplayIdM = surfaceControlClazz.getDeclaredMethod("getPrimaryPhysicalDisplayId");
        long displayId = (long) getPrimaryPhysicalDisplayIdM.invoke(null);

        Method getPhysicalDisplayTokenM = surfaceControlClazz.getDeclaredMethod("getPhysicalDisplayToken", long.class);
        IBinder iBinder = (IBinder) getPhysicalDisplayTokenM.invoke(null, displayId);

        Class<?> captureArgsBClazz = Class.forName("android.view.SurfaceControl$DisplayCaptureArgs$Builder");
        Constructor<?> constructor = captureArgsBClazz.getDeclaredConstructor(IBinder.class);
        constructor.setAccessible(true);
        Object captureArgsBObj = constructor.newInstance(iBinder);

        Field f1 = captureArgsBClazz.getDeclaredField("mDisplayToken");
        f1.setAccessible(true);
        f1.set(captureArgsBObj, iBinder);

        Field f2 = captureArgsBClazz.getDeclaredField("mWidth");
        f2.setAccessible(true);
        f2.setInt(captureArgsBObj, cropRect.width());

        Field f3 = captureArgsBClazz.getDeclaredField("mHeight");
        f3.setAccessible(true);
        f3.setInt(captureArgsBObj, cropRect.height());

        Class<?> captureArgsBClazzA = Class.forName("android.view.SurfaceControl$CaptureArgs$Builder");
        Field f5 = captureArgsBClazzA.getDeclaredField("mAllowProtected");
        f5.setAccessible(true);
        f5.setBoolean(captureArgsBObj, true);

        Field f4 = captureArgsBClazzA.getDeclaredField("mSourceCrop");
        f4.setAccessible(true);
        f4.set(captureArgsBObj, cropRect);

        Field f6 = captureArgsBClazzA.getDeclaredField("mUid");
        f6.setAccessible(true);
        f6.setInt(captureArgsBObj, -1);

        Method buildM = captureArgsBClazz.getDeclaredMethod("build");
        return buildM.invoke(captureArgsBObj);
    }

    private static Bitmap getScreenBitmap(Rect cropRect) throws Exception {
        Object captureArgsObj = makeCaptureArgs(cropRect);
        Class captureArgsClazz = Class.forName("android.view.SurfaceControl$DisplayCaptureArgs");
        Method captureDisplayMethod = SurfaceControl.class.getDeclaredMethod("captureDisplay", captureArgsClazz);
        Method asBitmapMethod = getAsBitmapMethod();
        Object screenshotHardwareBuffer = captureDisplayMethod.invoke(null, captureArgsObj);
        Bitmap hardwareBitmap = (Bitmap) asBitmapMethod.invoke(screenshotHardwareBuffer);
        return hardwareBitmap;
    }

    /**
     *          // 获取设备显示相关的信息
     *         DisplayMetrics dm = new DisplayMetrics();
     *         WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
     *         windowManager.getDefaultDisplay().getRealMetrics(dm);
     *         // 供截屏使用
     *         mRect = new Rect(0, 0, dm.widthPixels, dm.heightPixels);
     */
    public static String takeScreenshot(Rect cropRect) {
        try {
            Bitmap hardwareBitmap = getScreenBitmap(cropRect);
            String res = bitmapToBase64(hardwareBitmap);
            // 释放资源
            hardwareBitmap.getHardwareBuffer().close();
            hardwareBitmap.recycle();
            return res;
        } catch (Exception e) {
//            Global.log_e(TAG, "takeScreenshot error", e);
            return null;
        }
    }


    public static byte[] takeScreenshotToBytes(Rect cropRect) {
        try {
            Bitmap hardwareBitmap = getScreenBitmap(cropRect);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            hardwareBitmap.compress(Bitmap.CompressFormat.PNG, 85, byteArrayOutputStream);
            // 释放资源
            hardwareBitmap.getHardwareBuffer().close();
            hardwareBitmap.recycle();
            byte[] res = byteArrayOutputStream.toByteArray();
            return res;
        } catch (Exception e) {
//            Global.log_e(TAG, "takeScreenshotToBytes error", e);
            return null;
        }
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 85, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

//    public static String takeScreenshotByCmd() {
//        String tmp = "/data/local/tmp/tmp.png";
//        CommandTask.exec("screencap " + tmp);
//        try {
//            Path path = Paths.get(tmp);
//            byte[] fileContent = Files.readAllBytes(path);
//            Files.delete(path);
//            return Base64.encodeToString(fileContent, Base64.NO_WRAP);
//        } catch (Exception e) {
//            Global.log_e(TAG, "takeScreenshotByCmd error", e);
//            return null;
//        }
//    }
//
//    public static byte[] takeScreenshotToBytesByCmd() {
//        String tmp = "/data/local/tmp/tmp.png";
//        CommandTask.exec("screencap " + tmp);
//        try {
//            Path path = Paths.get(tmp);
//            byte[] fileContent = Files.readAllBytes(path);
//            Files.delete(path);
//            return fileContent;
//        } catch (Exception e) {
//            Global.log_e(TAG, "takeScreenshotByCmd error", e);
//            return null;
//        }
//    }
}
