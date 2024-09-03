package com.example.androidmodel.tools.apkinfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author kfflso
 * @data 2024/9/3 9:31
 * @plus:
 *      加固的壳的特征: https://blog.csdn.net/g5703129/article/details/85054405
 *      //注意文件读写权限 ls -l filename.apk 查看
 *
 *      SHELL_MAPPING = {
 *         ".appkey": "360加固",
 *         "0000000lllll.dex": "腾讯云移动应用安全(腾讯御安全)",
 *         "00000olllll.dex": "腾讯云移动应用安全(腾讯御安全)",
 *         "000O00ll111l.dex": "腾讯云移动应用安全(腾讯御安全)",
 *         "00O000ll111l.dex": "腾讯云移动应用安全(腾讯御安全)",
 *         "0OO00l111l1l": "腾讯云移动应用安全(腾讯御安全)",
 *         "360加固": "360加固",
 *         "APKProtect": "APKProtect",
 *         "UU安全": "UU安全",
 *         "aliprotect.dat": "阿里聚安全",
 *         "apktoolplus": "apktoolplus",
 *         "baiduprotect1.jar": "百度加固",
 *         "ijiami.ajm": "爱加密",
 *         "ijiami.dat": "爱加密",
 *         "jiagu_data.bin": "apktoolplus",
 *         "libAPKProtect.so": "APKProtect",
 *         "libBugly-yaq.so": "腾讯云移动应用安全(腾讯御安全)",
 *         "libDexHelper-x86.so": "梆梆安全(企业版)",
 *         "libDexHelper.so": "梆梆安全(企业版)",
 *         "libNSaferOnly.so": "通付盾",
 *         "libSecShell.so": "梆梆安全",
 *         "libapktoolplus_jiagu.so": "apktoolplus",
 *         "libapssec.so": "盛大加固",
 *         "libbaiduprotect.so": "百度加固",
 *         "libbaiduprotect_art.so": "百度加固",
 *         "libbaiduprotect_x86.so": "百度加固",
 *         "libcmvmp.so": "中国移动加固",
 *         "libddog.so": "娜迦加固",
 *         "libdemolish.so": "阿里聚安全",
 *         "libdemolishdata.so": "阿里聚安全",
 *         "libedog.so": "娜迦加固(企业版)",
 *         "libegis.so": "通付盾",
 *         "libexec.so": "爱加密",
 *         "libexecmain.so": "爱加密",
 *         "libfakejni.so": "阿里聚安全",
 *         "libfdog.so": "娜迦加固",
 *         "libitsec.so": "海云安加固",
 *         "libjgdtc.so": "360加固",
 *         "libjgdtc_a64.so": "360加固",
 *         "libjgdtc_art.so": "360加固",
 *         "libjgdtc_x64.so": "360加固",
 *         "libjgdtc_x86.so": "360加固",
 *         "libjiagu.so": "360加固",
 *         "libjiagu_a64.so": "360加固",
 *         "libjiagu_art.so": "360加固",
 *         "libjiagu_ls.so": "360加固",
 *         "libjiagu_x64.so": "360加固",
 *         "libjiagu_x86.so": "360加固",
 *         "libkwscmm.so": "几维安全",
 *         "libkwscr.so": "几维安全",
 *         "libkwslinker.so": "几维安全",
 *         "liblegudb.so": "腾讯乐固(旧版)",
 *         "libmobisec.so": "阿里聚安全",
 *         "libmogosec_dex.so": "中国移动加固",
 *         "libmogosec_sodecrypt.so": "中国移动加固",
 *         "libmogosecurity.so": "中国移动加固",
 *         "libnesec.so": "网易易盾",
 *         "libnqshield.so": "网秦加固",
 *         "libpreverify1.so": "阿里聚安全",
 *         "libprotectClass.so": "360加固",
 *         "libreincp.so": "珊瑚灵御",
 *         "libreincp_x86.so": "珊瑚灵御",
 *         "librsprotect.so": "瑞星加固",
 *         "libsecexe.so": "梆梆安全",
 *         "libsecmain.so": "梆梆安全",
 *         "libsgmain.so": "阿里聚安全",
 *         "libsgsecuritybody.so": "阿里聚安全",
 *         "libshell-super.2019.so": "腾讯云移动应用安全(腾讯御安全)",
 *         "libshella": "腾讯乐固(旧版)",
 *         "libshellx": "腾讯乐固(旧版)",
 *         "libshellx-super.2019.so": "腾讯云移动应用安全(腾讯御安全)",
 *         "libtosprotection.armeabi-v7a.so": "腾讯云移动应用安全(腾讯御安全)",
 *         "libtosprotection.armeabi.so": "腾讯云移动应用安全(腾讯御安全)",
 *         "libtosprotection.x86.so": "腾讯云移动应用安全(腾讯御安全)",
 *         "libtup.so": "腾讯乐固(旧版)",
 *         "libuusafe.jar.so": "UU安全",
 *         "libuusafe.so": "UU安全",
 *         "libuusafeempty.so": "UU安全",
 *         "libvenSec.so": "启明星辰",
 *         "libvenustech.so": "启明星辰",
 *         "libx3g.so": "顶像科技",
 *         "libzBugly-yaq.so": "腾讯云移动应用安全(腾讯御安全)",
 *         "libzuma.so": "阿里聚安全",
 *         "libzumadata.so": "阿里聚安全",
 *         "mix.dex": "腾讯乐固(旧版)",
 *         "mixz.dex": "腾讯乐固(旧版)",
 *         "mogosec_classes": "中国移动加固",
 *         "mogosec_data": "中国移动加固",
 *         "mogosec_dexinfo": "中国移动加固",
 *         "mogosec_march": "中国移动加固",
 *         "o0oooOO0ooOo.dat": "腾讯云移动应用安全(腾讯御安全)",
 *         "sign.bin": "apktoolplus",
 *         "t86": "腾讯云移动应用安全(腾讯御安全)",
 *         "tosprotection": "腾讯云移动应用安全(腾讯御安全)",
 *         "tosversion": "腾讯云移动应用安全(腾讯御安全)",
 *     }
 *
 */
public class CheckShellTypeUtils {

    private final Map<String,Set<String>> cache = new HashMap<>();


//    private Set<String> loadFilesFromApk(String apkPath) {
//        Set<String> fileSet = new HashSet<>();
//        try (ZipFile zipFile = new ZipFile(apkPath)) {
//            for (ZipEntry entry : zipFile.entries()) {
//                fileSet.add(entry.getName());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return fileSet;
//    }


    private static final String[] feature_filepath_360 = {
            "assets/.appkey",
            "assets/libjiagu.so"
    };
    private static final String[] feature_filepath_aijiami = {
            "lib/armeabi/libexecmain.so",
            "assets/ijiami.ajm",
            "assets/af.bin",
            "assets/signed.bin",
            "assets/ijm_lib/armeabi/libexec.so",
            "assets/ijm_lib/X86/libexec.so"
    };
    private static final String[] feature_filepath_kiwisec = {
            "assets/dex.dat",
            "lib/armeabi/kdpdata.so",
            "lib/armeabi/libkdp.so",
            "lib/armeabi/libkwscmm.so"
    };
    private static final String[] feature_filepath_bangcle_free = {
           " assets/secData0.jar",
            "lib/armeabi/libSecShell.so",
           " lib/armeabi/libSecShell-x86.so"
    };
    private static final String[] feature_filepath_bangcle_custom = {
            "assets/classes.jar",
            "lib/armeabi/DexHelper.so"
    };
    private static final String[] feature_filepath_tencent = {
            "tencent_stub",
            "lib/armeabi/libshella-xxxx.so",
            "lib/armeabi/libshellx-xxxx.so",
            "lib/armeabi/mix.dex",
            "lib/armeabi/mixz.dex"
    };

    private static final String[] feature_filepath_tencent_yu_security = {
            "assets/libtosprotection.armeabi-v7a.so",
            "assets/libtosprotection.armeabi.so",
            "assets/libtosprotection.x86.so",
            "assets/tosversion",
            "lib/armeabi/libtest.so",
            "lib/armeabi/libTmsdk-xxx-mfr.so"
    };

    private static final String[] feature_filepath_dingxiang_inc = {
            "lib/armeabi/libx3g.so"
    };

    private static final String[] feature_filepath_ali = {
            "assets/armeabi/libfakejni.so",
            "assets/armeabi/libzuma.so",
            "assets/libzuma.so",
            "assets/libzumadata.so",
            "assets/libpreverify1.so"
    };

    private static final String[] feature_filepath_dexprotect = {
            "assets/classes.dex.dat",
            "assets/dp.arm-v7.so.dat",
            "assets/dp.arm.so.dat"
    };

    private static final String[] feature_filepath_baidu = {
            "lib/armeabi/libbaiduprotect.so",
            "assets/baiduprotect1.jar",
            "assets/baiduprotect.jar"
    };

    private static final String[] feature_filepath_secidea = {
            "assets/itse",
            "lib/armeabi/libitsec.so"
    };

    private static final String[] feature_filepath_apktoolplus = {
            "assets/jiagu_data.bin",
            "assets/sign.bin",
            "lib/armeabi/libapktoolplus_jiagu.so"
    };

    private static final String[] feature_filepath_naga = {
            "libedog.so",
            "libddog.so"
    };

    private static final String[] feature_filepath_tongfudun = {
            "libegis.so"
    };

    private static final String[] feature_filepath_shengda = {
            "libapssec.so"
    };

    private static final String[] feature_filepath_sising = {
            "librsprotect.so"
    };

    private static final String[] feature_filepath_wangqin = {
            "libnqshield.so"
    };

    private static final String[] feature_filepath_uu_security = {
            "assets/libuusafe.jar.so",
            "assets/libuusafe.so",
            "lib/armeabi/libuusafeempty.so"
    };

    private static final String[] feature_filepath_china_mobile = {
            "assets/mogosec_classes",
            "assets/mogosec_data",
            "assets/mogosec_dexinfo",
            "assets/mogosec_march",
            "lib/armeabi/libcmvmp.so",
            "lib/armeabi/libmogosec_dex.so",
            "lib/armeabi/libmogosec_sodecrypt.so",
            "lib/armeabi/libmogosecurity.so"
    };

    private static final String[] feature_filepath_shanhulinyu = {
            "assets/libreincp.so",
            "assets/libreincp_x86.so"
    };




}
