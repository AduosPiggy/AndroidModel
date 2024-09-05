package com.example.androidmodel.tools.apkinfo.cache;

import com.google.gson.Gson;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author kfflso
 * @data 2024/9/3 9:31
 * @plus:
 *      加固的壳的特征: https://blog.csdn.net/g5703129/article/details/85054405
 *      //注意文件读写权限 ls -l filename.apk 查看
 *
 */
public class ApkShellFeaturesCache {
    //加固类型的特征字典; key-value 加密后文件的路径:加密公司
    private static final Map<String,String> shellFeaturesMap = new HashMap<>();

    public static ApkShellFeaturesCache getInstance(){
        return SingletonHolder.instance;
    }
    private static class SingletonHolder {
        private static ApkShellFeaturesCache instance = new ApkShellFeaturesCache();
    }
    private ApkShellFeaturesCache() {
        initShellFMaps();
    }

    public Map<String, String> getShellFeaturesMap(){
        return shellFeaturesMap;
    }


    private void initShellFMaps() {
        // 腾讯云移动应用安全(腾讯御安全)
        shellFeaturesMap.put(".appkey", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("0000000lllll.dex", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("00000olllll.dex", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("000O00ll111l.dex", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("00O000ll111l.dex", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("0OO00l111l1l", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("libBugly-yaq.so", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("libshell-super.2019.so", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("libshellx-super.2019.so", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("libtosprotection.armeabi-v7a.so", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("libtosprotection.armeabi.so", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("libtosprotection.x86.so", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("libzBugly-yaq.so", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("o0oooOO0ooOo.dat", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("t86", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("tosprotection", "腾讯云移动应用安全(腾讯御安全)");
        shellFeaturesMap.put("tosversion", "腾讯云移动应用安全(腾讯御安全)");

        // 360加固
        shellFeaturesMap.put("360加固", "360加固");
        shellFeaturesMap.put("libjgdtc.so", "360加固");
        shellFeaturesMap.put("libjgdtc_a64.so", "360加固");
        shellFeaturesMap.put("libjgdtc_art.so", "360加固");
        shellFeaturesMap.put("libjgdtc_x64.so", "360加固");
        shellFeaturesMap.put("libjgdtc_x86.so", "360加固");
        shellFeaturesMap.put("libjiagu.so", "360加固");
        shellFeaturesMap.put("libjiagu_a64.so", "360加固");
        shellFeaturesMap.put("libjiagu_art.so", "360加固");
        shellFeaturesMap.put("libjiagu_ls.so", "360加固");
        shellFeaturesMap.put("libjiagu_x64.so", "360加固");
        shellFeaturesMap.put("libjiagu_x86.so", "360加固");
        shellFeaturesMap.put("libprotectClass.so", "360加固");

        // 阿里聚安全
        shellFeaturesMap.put("aliprotect.dat", "阿里聚安全");
        shellFeaturesMap.put("libdemolish.so", "阿里聚安全");
        shellFeaturesMap.put("libdemolishdata.so", "阿里聚安全");
        shellFeaturesMap.put("libfakejni.so", "阿里聚安全");
        shellFeaturesMap.put("libmobisec.so", "阿里聚安全");
        shellFeaturesMap.put("libpreverify1.so", "阿里聚安全");
        shellFeaturesMap.put("libsgmain.so", "阿里聚安全");
        shellFeaturesMap.put("libsgsecuritybody.so", "阿里聚安全");
        shellFeaturesMap.put("libzuma.so", "阿里聚安全");
        shellFeaturesMap.put("libzumadata.so", "阿里聚安全");

        // 百度加固
        shellFeaturesMap.put("baiduprotect1.jar", "百度加固");
        shellFeaturesMap.put("libbaiduprotect.so", "百度加固");
        shellFeaturesMap.put("libbaiduprotect_art.so", "百度加固");
        shellFeaturesMap.put("libbaiduprotect_x86.so", "百度加固");

        // APKProtect
        shellFeaturesMap.put("APKProtect", "APKProtect");
        shellFeaturesMap.put("libAPKProtect.so", "APKProtect");

        // 爱加密
        shellFeaturesMap.put("ijiami.ajm", "爱加密");
        shellFeaturesMap.put("ijiami.dat", "爱加密");
        shellFeaturesMap.put("libexec.so", "爱加密");
        shellFeaturesMap.put("libexecmain.so", "爱加密");

        // 中国移动加固
        shellFeaturesMap.put("libcmvmp.so", "中国移动加固");
        shellFeaturesMap.put("libmogosec_dex.so", "中国移动加固");
        shellFeaturesMap.put("libmogosec_sodecrypt.so", "中国移动加固");
        shellFeaturesMap.put("libmogosecurity.so", "中国移动加固");
        shellFeaturesMap.put("mogosec_classes", "中国移动加固");
        shellFeaturesMap.put("mogosec_data", "中国移动加固");
        shellFeaturesMap.put("mogosec_dexinfo", "中国移动加固");
        shellFeaturesMap.put("mogosec_march", "中国移动加固");

        // 娜迦加固
        shellFeaturesMap.put("libddog.so", "娜迦加固");
        shellFeaturesMap.put("libedog.so", "娜迦加固(企业版)");
        shellFeaturesMap.put("libfdog.so", "娜迦加固");

        // 通付盾
        shellFeaturesMap.put("libNSaferOnly.so", "通付盾");
        shellFeaturesMap.put("libegis.so", "通付盾");

        // 海云安加固
        shellFeaturesMap.put("libitsec.so", "海云安加固");

        // 几维安全
        shellFeaturesMap.put("libkwscr.so", "几维安全");
        shellFeaturesMap.put("libkwslinker.so", "几维安全");
        shellFeaturesMap.put("libkwscmm.so", "几维安全");

        // 腾讯乐固(旧版)
        shellFeaturesMap.put("libshella", "腾讯乐固(旧版)");
        shellFeaturesMap.put("libshellx", "腾讯乐固(旧版)");
        shellFeaturesMap.put("libtup.so", "腾讯乐固(旧版)");
        shellFeaturesMap.put("mix.dex", "腾讯乐固(旧版)");
        shellFeaturesMap.put("mixz.dex", "腾讯乐固(旧版)");

        // 启明星辰
        shellFeaturesMap.put("libvenSec.so", "启明星辰");
        shellFeaturesMap.put("libvenustech.so", "启明星辰");

        // 顶像科技
        shellFeaturesMap.put("libx3g.so", "顶像科技");

        // 瑞星加固
        shellFeaturesMap.put("librsprotect.so", "瑞星加固");

        // 珊瑚灵御
        shellFeaturesMap.put("libreincp.so", "珊瑚灵御");
        shellFeaturesMap.put("libreincp_x86.so", "珊瑚灵御");

        // 梆梆安全
        shellFeaturesMap.put("libDexHelper-x86.so", "梆梆安全(企业版)");
        shellFeaturesMap.put("libDexHelper.so", "梆梆安全(企业版)");
        shellFeaturesMap.put("libSecShell.so", "梆梆安全");
        shellFeaturesMap.put("libsecexe.so", "梆梆安全");
        shellFeaturesMap.put("libsecmain.so", "梆梆安全");

        // UU安全
        shellFeaturesMap.put("UU安全", "UU安全");
        shellFeaturesMap.put("libuusafe.jar.so", "UU安全");
        shellFeaturesMap.put("libuusafe.so", "UU安全");
        shellFeaturesMap.put("libuusafeempty.so", "UU安全");

        // apktoolplus
        shellFeaturesMap.put("apktoolplus", "apktoolplus");
        shellFeaturesMap.put("jiagu_data.bin", "apktoolplus");
        shellFeaturesMap.put("sign.bin", "apktoolplus");

    }









}
