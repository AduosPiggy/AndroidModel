package com.example.androidmodel.tools;

import java.util.Arrays;

/**
 * @author kfflso
 * @data 2024/9/6 14:05
 * @plus:
 */
public class StringUtils {

    public static StringUtils getInstance() {
        return SingletonHolder.instance;
    }
    private static class SingletonHolder {
        private static StringUtils instance = new StringUtils();
    }

    private String str1 = "资质与规则";
    private String str2 = "视频";

    //比较两中文字符串是否相等
    public boolean equals(String str1, String str2){
        boolean equal = false;
        char[] charArray1 = str1.toCharArray();
        char[] charArray2 = str2.toCharArray();
        Arrays.sort(charArray1);
        Arrays.sort(charArray2);
        String sortedStr1 = new String(charArray1);
        String sortedStr2 = new String(charArray2);
        equal = sortedStr1.equals(sortedStr2);
        return equal;
    }
    public boolean equals2(String str1, String str2){
        return str1.equals(str2);
    }
}
