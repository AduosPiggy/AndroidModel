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
