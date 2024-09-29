package com.example.androidmodel.tools.apkinfo.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kfflso
 * @data 2024/9/19 10:21
 * @plus:
 */
public class Kfflso_FeaturesMap {

    public static Kfflso_FeaturesMap getInstance(){
        return SingletonHolder.instance;
    }
    private static class SingletonHolder {
        private static Kfflso_FeaturesMap instance = new Kfflso_FeaturesMap();
    }
    private Kfflso_FeaturesMap() {
        initShellFeaturesMap();
        initSdkFeaturesMap();
    }

    //加固类型的特征字典; key-value 加密后文件的路径:加密公司
    private static final Map<String,String> shellFeaturesMap = new HashMap<String, String>();
    //app使用的第三方sdk的特征字典; key-value sdk_packageName_feature - sdk_id
    private static final Map<String,String> sdkFeaturesMap = new HashMap<>();


    public Map<String, String> getShellFeaturesMap(){
        return shellFeaturesMap;
    }
    public Map<String, String> getSdkFeaturesMap(){
        return sdkFeaturesMap;
    }
    private void initShellFeaturesMap() {
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

    private void initSdkFeaturesMap() {
        sdkFeaturesMap.put("zendesk.messaging",                     "1");
        sdkFeaturesMap.put("com.segment.analytics",                 "2");
        sdkFeaturesMap.put("org.prebid",                            "3");
        sdkFeaturesMap.put("org.osmdroid",                          "4");
        sdkFeaturesMap.put("com.iab.omid",                          "5");
        sdkFeaturesMap.put("com.newrelic.agent.android",            "6");
        sdkFeaturesMap.put("com.my.target",                         "7");
        sdkFeaturesMap.put("com.facebook.ads",                      "8");
        sdkFeaturesMap.put("com.mapbox.maps",                       "9");
        sdkFeaturesMap.put("com.airbnb.lottie",                     "10");
        sdkFeaturesMap.put("com.integralads.avid",                  "11");
        sdkFeaturesMap.put("com.instabug.library",                  "12");
        sdkFeaturesMap.put("com.fyber.inneractive.sdk",             "13");
        sdkFeaturesMap.put("com.inmobi.monetization",               "14");
        sdkFeaturesMap.put("com.indooratlas.android",               "15");
        sdkFeaturesMap.put("net.hockeyapp.",                        "16");
        sdkFeaturesMap.put("com.mapbox.search",                     "17");
        sdkFeaturesMap.put("com.helpshift",                         "18");
        sdkFeaturesMap.put("com.google.android.gms",                "19");
        sdkFeaturesMap.put("com.google.firebase",                   "20");
        sdkFeaturesMap.put("com.google.android.gms",                "21");
        sdkFeaturesMap.put("com.giphy.sdk",                         "22");
        sdkFeaturesMap.put("jp.co.geniee.gnadsdk",                  "23");
        sdkFeaturesMap.put("com.gameanalytics.sdk",                 "24");
        sdkFeaturesMap.put("com.fyber.fairbid",                     "25");
        sdkFeaturesMap.put("com.flymob",                            "26");
        sdkFeaturesMap.put("jp.co.geniee.gnadsdk",                  "27");
        sdkFeaturesMap.put("com.mapbox.navigation",                 "28");
        sdkFeaturesMap.put("",                                      "29");
        sdkFeaturesMap.put("com.facebook.share",                    "30");
        sdkFeaturesMap.put("com.facebook.sdk",                      "31");
        sdkFeaturesMap.put("com.dynatrace.android.agent ",          "32");
        sdkFeaturesMap.put("com.chartboost",                        "33");
        sdkFeaturesMap.put("",                                      "34");
        sdkFeaturesMap.put("io.branch.sdk.android",                 "35");
        sdkFeaturesMap.put("com.esri",                              "36");
        sdkFeaturesMap.put("com.appsflyer",                         "37");
        sdkFeaturesMap.put("com.yandex.metrica",                    "38");
        sdkFeaturesMap.put("com.applovin",                          "39");
        sdkFeaturesMap.put("",                                      "40");
        sdkFeaturesMap.put("org.altbeacon",                         "41");
        sdkFeaturesMap.put("org.alohalytics",                       "42");
        sdkFeaturesMap.put("",                                      "43");
        sdkFeaturesMap.put("com.igaworks.ssp",                      "44");
        sdkFeaturesMap.put("",                                      "45");
        sdkFeaturesMap.put("com.adjust.sdk",                        "46");
        sdkFeaturesMap.put("jp.adfully",                            "47");
        sdkFeaturesMap.put("com.adcolony",                          "48");
        sdkFeaturesMap.put("",                                      "49");
        sdkFeaturesMap.put("com.braze",                             "50");
        sdkFeaturesMap.put("com.localytics.androidx",               "51");
        sdkFeaturesMap.put("com.comscore",                          "52");
        sdkFeaturesMap.put("com.locuslabs",                         "53");
        sdkFeaturesMap.put("",                                      "54");
        sdkFeaturesMap.put("com.unity3d.ads",                       "55");
        sdkFeaturesMap.put("com.startapp",                          "56");
        sdkFeaturesMap.put("com.smaato.android.sdk",                "57");
        sdkFeaturesMap.put("org.appcelerator.titanium",             "58");
        sdkFeaturesMap.put("",                                      "59");
        sdkFeaturesMap.put("",                                      "60");
        sdkFeaturesMap.put("ly.count.android.sdknative",            "61");
        sdkFeaturesMap.put("net.kidoz.sdk",                         "62");
        sdkFeaturesMap.put("io.heap.core",                          "63");
        sdkFeaturesMap.put("com.fsn.cauly",                         "64");
        sdkFeaturesMap.put("",                                      "65");
        sdkFeaturesMap.put("io.didomi.sdk",                         "66");
        sdkFeaturesMap.put("com.deltadna.android",                  "67");
        sdkFeaturesMap.put("com.krux.androidsdk",                   "68");
        sdkFeaturesMap.put("com.noqoush.adfalcon.android.sdk",      "69");
        sdkFeaturesMap.put("",                                      "70");
        sdkFeaturesMap.put("",                                      "71");
        sdkFeaturesMap.put("de.infonline.lib",                      "72");
        sdkFeaturesMap.put("com.waze",                              "73");
        sdkFeaturesMap.put("io.adjoe",                              "74");
        sdkFeaturesMap.put("net.admixer.sdk",                       "75");
        sdkFeaturesMap.put("com.gomfactory.adpie",                  "76");
        sdkFeaturesMap.put("",                                      "77");
        sdkFeaturesMap.put("",                                      "78");
        sdkFeaturesMap.put("",                                      "79");
        sdkFeaturesMap.put("com.ad4screen.sdk",                     "80");
        sdkFeaturesMap.put("com.skyhook",                           "81");
        sdkFeaturesMap.put("",                                      "82");
        sdkFeaturesMap.put("com.emarsys",                           "83");
        sdkFeaturesMap.put("",                                      "84");
        sdkFeaturesMap.put("",                                      "85");
        sdkFeaturesMap.put("com.exponea.sdk",                       "86");
        sdkFeaturesMap.put("com.foursquare",                        "87");
        sdkFeaturesMap.put("com.hypertrack",                        "88");
        sdkFeaturesMap.put("",                                      "89");
        sdkFeaturesMap.put("",                                      "90");
        sdkFeaturesMap.put("",                                      "91");
        sdkFeaturesMap.put("com.glympse",                           "92");
        sdkFeaturesMap.put("",                                      "93");
        sdkFeaturesMap.put("com.foresee.sdk",                       "94");
        sdkFeaturesMap.put("com.acuant",                            "95");
        sdkFeaturesMap.put("com.bitly",                             "96");
        sdkFeaturesMap.put("ru.tachos.admitadstatisticsdk",         "97");
        sdkFeaturesMap.put("com.adform.advertising.sdk",            "98");
        sdkFeaturesMap.put("",                                      "99");
        sdkFeaturesMap.put("",                                      "100");
        sdkFeaturesMap.put("com.adlibr",                            "101");
        sdkFeaturesMap.put("",                                      "102");
        sdkFeaturesMap.put("",                                      "103");
        sdkFeaturesMap.put("",                                      "104");
        sdkFeaturesMap.put("com.github.adadaptedinc",               "105");
        sdkFeaturesMap.put("com.singular.sdk",                      "106");
        sdkFeaturesMap.put("com.abtasty",                           "107");
        sdkFeaturesMap.put("hound.android",                         "108");
        sdkFeaturesMap.put("com.pure.internal",                     "109");
        sdkFeaturesMap.put("",                                      "110");
        sdkFeaturesMap.put("net.crowdconnected.android",            "111");
        sdkFeaturesMap.put("com.blesh.sdk",                         "112");
        sdkFeaturesMap.put("com.zendrive.sdk",                      "113");
        sdkFeaturesMap.put("",                                      "114");
        sdkFeaturesMap.put("com.tamoco.sdk",                        "115");
        sdkFeaturesMap.put("com.github.EulerianTechnologies",       "116");
        sdkFeaturesMap.put("com.lisnr",                             "117");
        sdkFeaturesMap.put("com.beintoo.nucleon",                   "118");
        sdkFeaturesMap.put("com.cooladata.android",                 "119");
        sdkFeaturesMap.put("com.adotmob",                           "120");
        sdkFeaturesMap.put("com.sailthru.mobile.sdk",               "121");
        sdkFeaturesMap.put("com.groundtruth.sdk",                   "122");
        sdkFeaturesMap.put("com.complementics",                     "123");
        sdkFeaturesMap.put("com.footmarks",                         "124");
        sdkFeaturesMap.put("",                                      "125");
        sdkFeaturesMap.put("",                                      "");
        sdkFeaturesMap.put("com.fidzup",                            "127");
        sdkFeaturesMap.put("com.timerazor.gravysdk",                "128");
        sdkFeaturesMap.put("com.github.Lenddo",                     "129");
        sdkFeaturesMap.put("",                                      "130");
        sdkFeaturesMap.put("io.herow.sdk",                          "131");
        sdkFeaturesMap.put("",                                      "132");
        sdkFeaturesMap.put("",                                      "133");
        sdkFeaturesMap.put("com.gigya.android",                     "134");
        sdkFeaturesMap.put("",                                      "135");
        sdkFeaturesMap.put("com.leanplum",                          "136");
        sdkFeaturesMap.put("io.huq.sourcekit",                      "137");
        sdkFeaturesMap.put("com.instreamatic",                      "138");
        sdkFeaturesMap.put("com.socdm.d.adgeneration",              "139");
        sdkFeaturesMap.put("com.hyprmx.android",                    "140");
        sdkFeaturesMap.put("io.kontakt.mvn",                        "141");
        sdkFeaturesMap.put("com.smartadserver.android",             "142");

        sdkFeaturesMap.put("com.huawei.android.quickaction",        "666");
    }
}
