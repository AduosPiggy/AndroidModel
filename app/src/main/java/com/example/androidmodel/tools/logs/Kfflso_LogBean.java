package com.example.androidmodel.tools.logs;

/**
 * @author kfflso
 * @data 2024/9/24 18:06
 * @plus:
 */
public class Kfflso_LogBean {
    private String time;
    private String tag;
    private String logMsg;

    public Kfflso_LogBean(String time, String tag, String logMsg) {
        this.time = time;
        this.tag = tag;
        this.logMsg = logMsg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLogMsg() {
        return logMsg;
    }

    public void setLogMsg(String logMsg) {
        this.logMsg = logMsg;
    }
}
