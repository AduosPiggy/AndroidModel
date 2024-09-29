package com.example.androidmodel.tools.downloadUtil;

public class Kfflso_DownloadUtilCfg {

    public static Kfflso_DownloadUtilCfg getInstance(){
        return SingletonHolder.instance;
    }
    private static class SingletonHolder {
        private static Kfflso_DownloadUtilCfg instance = new Kfflso_DownloadUtilCfg();
    }
    public Kfflso_DownloadUtilCfg() {
    }

    public int getDownloadQueryTime() {
        return 20000;
    }

}
