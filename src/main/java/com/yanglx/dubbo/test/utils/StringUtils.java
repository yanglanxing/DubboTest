package com.yanglx.dubbo.test.utils;

public class StringUtils {

    public static boolean isNotBlank(String str){
        return str != null && str.length() > 0;
    }

    public static boolean isBlank(String str){
        return !isNotBlank(str);
    }
}
