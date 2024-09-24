package com.example.androidmodel.tools;

import android.util.Log;

import com.google.gson.Gson;

import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * @author kfflso
 * @data 2024/8/29 10:47
 * @plus:
 */
public class LogsUtils {
    public static LogsUtils getInstance() {
        return SingletonHolder.instance;
    }
    private static class SingletonHolder {
        private static LogsUtils instance = new LogsUtils();
    }

    public <T> void LogList(String TAG, List<T> list){
        Gson gson = new Gson();

        for (T item : list){
            if( item instanceof List<?>){
                LogList(TAG,(List<?>) item );
            }else {
                Log.d(TAG,gson.toJson(item));
            }
        }
    }
    public <T> void LogList(String TAG, String explain, List<T> list){
        for (T item : list){
            if( item instanceof List<?>){
                LogList(TAG, explain, (List<?>) item );
            }else {
                LogT(TAG,explain,item);
            }
        }
    }

    public <T> void LogSet(String TAG, Set<T> set){
        Gson gson = new Gson();

        for (T item : set){
            if( item instanceof Set<?>){
                LogSet(TAG,(Set<?>) item);
            }else {
                Log.d(TAG,gson.toJson(item));
            }
        }
    }

    public <T> void LogStack(String TAG, Stack<T> stack) {
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
    public <T> void LogStack(String TAG,  String explain, Stack<T> stack) {
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

    public <T> void LogT(String TAG,T t){
        Log.d(TAG,new Gson().toJson(t));
    }

    public <T> void LogT(String TAG,String explain,T t){
        Log.d(TAG,explain + ": " + new Gson().toJson(t));
    }

}
