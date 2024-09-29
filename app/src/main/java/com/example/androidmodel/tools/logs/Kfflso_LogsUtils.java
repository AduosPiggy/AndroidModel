package com.example.androidmodel.tools.logs;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author kfflso
 * @data 2024/8/29 10:47
 * @plus:
 */
public class Kfflso_LogsUtils {
    //文件存在最长时间
    private static long MAX_TIME = 7 * 24 * 60 * 60 * 1000;
    //文件最大大小
    private static int MAX_SIZE = 100 * 1024 * 1024;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public static String logDir = "";
    public static void initLogDir(Context context){
        if(logDir.isEmpty()){
            logDir = context.getFilesDir() + "/log";
        }
    }
    /**
     *
     * logDir getLogDir(context)
     * @param logTag log tag 即可
     * @param logMsg 需要 log 的信息
     *
     */
    public static void logToFileAsync(String logTag, String logMsg){
        if( !logDir.isEmpty() && !logMsg.isEmpty() ){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    logToFile(logDir, logTag, logMsg);
                }
            });
        }
    }

    private static void logToFile(String logDir, String logTag, String logMsg){
        File fileDir = new File(logDir);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        checkClearLogDir(fileDir,MAX_TIME,MAX_SIZE);
        writeToFile(logDir,logTag,logMsg);
    }

    /**
     *
     * @param fileDir 日志文件夹 File形式
     * @param MAX_TIME 文件最大保留时长
     * @param MAX_SIZE 清理文件前, 文件最大大小
     */
    private static void checkClearLogDir(File fileDir, long MAX_TIME, int MAX_SIZE){
        File[] files = fileDir.listFiles();
        if(files != null){
            for(File f_ : files){
                long keepTime = System.currentTimeMillis() - f_.lastModified();
                if(keepTime > MAX_TIME){
                    f_.delete();
                    continue;
                }
                if(f_.length() > MAX_SIZE){
                    f_.delete();
                }
            }
        }
    }

    /**
     *
     * @param logDir 日志文件夹 File形式
     * @param logTag
     * @param logMsg
     */
    private static void writeToFile(String logDir, String logTag, String logMsg){
        try {

            File file = checkFileName(logDir);

            FileWriter fw = new FileWriter(file,true);
            String logTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
            fw.append(logTime).append("\t")
               .append(logTag).append("\t")
               .append(logMsg).append("\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File checkFileName(String logDir){
        File file = null;
        try {
            String filename = "log-tdc.txt";
            file = new File(logDir,filename);
            if(!file.exists()){
                file.createNewFile();
                return file;
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
        return file;
    }


    public static <T> void LogList(String TAG, List<T> list){
        Gson gson = new Gson();

        for (T item : list){
            if( item instanceof List<?>){
                LogList(TAG,(List<?>) item );
            }else {
                Log.d(TAG,gson.toJson(item));
            }
        }
    }
    public static <T> void LogList(String TAG, String explain, List<T> list){
        for (T item : list){
            if( item instanceof List<?>){
                LogList(TAG, explain, (List<?>) item );
            }else {
                LogT(TAG,explain,item);
            }
        }
    }

    public static <T> void LogSet(String TAG, Set<T> set){
        Gson gson = new Gson();

        for (T item : set){
            if( item instanceof Set<?>){
                LogSet(TAG,(Set<?>) item);
            }else {
                Log.d(TAG,gson.toJson(item));
            }
        }
    }

    public static <T> void LogStack(String TAG, Stack<T> stack) {
        Gson gson = new Gson();
        Stack<T> tempStack = new Stack<>();
        while (!stack.isEmpty()) {
            T item = stack.pop();
            tempStack.push(item);
            LogT(TAG, gson.toJson(item));
        }
        while (!tempStack.isEmpty()) {
            stack.push(tempStack.pop());
        }
    }
    public static <T> void LogStack(String TAG,  String explain, Stack<T> stack) {
        Gson gson = new Gson();
        Stack<T> tempStack = new Stack<>();
        while (!stack.isEmpty()) {
            T item = stack.pop();
            tempStack.push(item);
            LogT(TAG, explain, gson.toJson(item));
        }
        while (!tempStack.isEmpty()) {
            stack.push(tempStack.pop());
        }
    }

    public static <T> void LogT(String TAG,T t){
        Log.d(TAG,new Gson().toJson(t));
    }

    public static <T> void LogT(String TAG,String explain,T t){
        Log.d(TAG,explain + ": " + new Gson().toJson(t));
    }

}
