package com.example.androidmodel.tools;

public class LoadByLazyPreUtil {

    //懒加载: 单例 + 多线程 + 懒加载
    public static LoadByLazyPreUtil getInstance(){
        return SingletonHolder.instance;
    }
    private static class SingletonHolder {
        private static LoadByLazyPreUtil instance = new LoadByLazyPreUtil();
    }
    public LoadByLazyPreUtil() {
    }

    //当调用getInstance加载LoadByLazyPreUtil类时,加载静态块,然后预加载doSomePreLoad();
    static {
        getInstance().doSomePreLoad();
    }
    private void doSomePreLoad() {

    }
}
