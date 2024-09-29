package com.example.androidmodel.tools.logs;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.text.SimpleDateFormat;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author kfflso
 * @data 2024/9/24 17:59
 * @plus:
 *      to do 用 queue 存储想要打印的日志, 用 buffer 共享内存来消费队列
 */
public class Kfflso_LogsFileMapperUtils {

    private static LinkedBlockingDeque<Kfflso_LogBean> queue;
    private static MappedByteBuffer buffer;

    private static String logDir;

    public static void init(Context context){
        queue = new LinkedBlockingDeque();
        logDir = context.getFilesDir() + "/log";

    }
    public static void addLogMsg(String logTag, String logMsg){
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        Kfflso_LogBean bean = new Kfflso_LogBean(time,logTag,logMsg);
        queue.add(bean);
    }

    public static void logToFile(){

    }

    private static void initFile(String logDir){
        File file = null;
        try {
            String filename = "log-tdc.txt";
            file = new File(logDir,filename);
            if(!file.exists()){
                file.createNewFile();
                return ;
            }
            String date = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
            String lastDate = new SimpleDateFormat("yyyy-MM-dd").format(file.lastModified());
            if(!date.equals(lastDate)){
                filename = date + "-log-tdc.txt";
                File f = new File(logDir,filename);
                file.renameTo(f);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
