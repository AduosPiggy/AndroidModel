package com.example.androidmodel.tools;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;


import androidx.core.app.ActivityCompat;

import java.io.IOException;

/**
 * @author kfflso
 * @data 2024/8/13 15:04
 * @plus:
 * AM添加权限: SET_WALLPAPER
 *
 */

public class WallPaperUtil {
    private String TAG = getClass().getSimpleName();

    public static WallPaperUtil getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static WallPaperUtil instance = new WallPaperUtil();
    }

    //清除桌面和锁屏壁纸，重新设置回系统默认壁纸
    public void resetWallPaper(Context context) {
        try {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // 清除桌面幕壁纸
                wallpaperManager.clear(WallpaperManager.FLAG_SYSTEM);
                // 清除锁屏壁纸
                wallpaperManager.clear(WallpaperManager.FLAG_LOCK);
            } else {
                // 对于低于 Android N 的版本，只能清除主屏幕壁纸
                wallpaperManager.clear();
            }
        } catch (IOException e) {
            Log.d(getClass().getSimpleName(), "reset default wallpaper failed!");
            throw new RuntimeException(e);
        }
    }

    //设置桌面和锁屏壁纸
    public void setWallpaper(Context context, int resId) {
        setSystemWallpaper(context, resId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setLockWallpaper(context, resId);
        }
    }

    //设置桌面和锁屏壁纸为默认壁纸
//    public void setDefWallpaper(Context context) {
//        int defaultWallpaperResId = com.android.internal.R.drawable.default_lock_wallpaper;
//        int defaultLockWallpaperResId = com.android.internal.R.drawable.default_lock_wallpaper;
//        Log.d(TAG, "default wallpaper system: " + defaultWallpaperResId + ";  lock: " + defaultLockWallpaperResId);
//        setSystemWallpaper(context, defaultWallpaperResId);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            setLockWallpaper(context, defaultLockWallpaperResId);
//        }
//    }

    //桌面壁纸
    //int defaultWallpaperResId = com.android.internal.R.drawable.default_wallpaper;
    public void setSystemWallpaper(Context context, int resourceId) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
            if (bitmap == null) {
                Log.d(TAG, "resourceId: " + resourceId + " 的bitmap, system wallpaper 为 null");
                return;
            }
            wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //锁屏壁纸
    //int defaultLockWallpaperResId = com.android.internal.R.drawable.default_lock_wallpaper;
    public void setLockWallpaper(Context context, int resourceId) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
            if (bitmap == null) {
                Log.d(TAG, "resourceId: " + resourceId + " 的bitmap, Lock wallpaper 为 null");
                return;
            }
            wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取当前桌面壁纸
    public Drawable getCurrentWallpaper(Activity activity) {
        Drawable drawable = null;
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            final int REQUEST_READ_EXTERNAL_STORAGE = 1;
            final String[] PERMISSION_STORAGE = {
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
            ActivityCompat.requestPermissions(activity, PERMISSION_STORAGE, REQUEST_READ_EXTERNAL_STORAGE);
        }
        drawable = wallpaperManager.getDrawable();
        return drawable;
    }

}

