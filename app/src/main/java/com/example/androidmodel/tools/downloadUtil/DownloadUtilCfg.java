package com.example.androidmodel.tools.downloadUtil;

import com.example.androidmodel.tools.CmdUtil;

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
