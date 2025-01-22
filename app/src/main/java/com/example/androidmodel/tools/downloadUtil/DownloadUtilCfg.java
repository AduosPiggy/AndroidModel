package com.example.androidmodel.tools.downloadUtil;

public class DownloadUtilCfg {

    public static DownloadUtilCfg getInstance(){
        return SingletonHolder.instance;
    }
    private static class SingletonHolder {
        private static DownloadUtilCfg instance = new DownloadUtilCfg();
    }
    public DownloadUtilCfg() {
    }

    public int getDownloadQueryTime() {
        return 20000;
    }

}
