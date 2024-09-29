package com.example.androidmodel.tools;

public class Kfflso_LoadByLazyPreUtil {

    //懒加载: 单例 + 多线程 + 懒加载
    public static Kfflso_LoadByLazyPreUtil getInstance(){
        return SingletonHolder.instance;
    }
    private static class SingletonHolder {
        private static Kfflso_LoadByLazyPreUtil instance = new Kfflso_LoadByLazyPreUtil();
    }
    public Kfflso_LoadByLazyPreUtil() {
    }

    //当调用getInstance加载LoadByLazyPreUtil类时,加载静态块,然后预加载doSomePreLoad();
    static {
        getInstance().doSomePreLoad();
    }
    private void doSomePreLoad() {

    }
}
