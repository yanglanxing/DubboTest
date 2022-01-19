package com.yanglx.dubbo.test.utils;

public class StrUtils {

    public static boolean isNotBlank(String str) {
        return str != null && str.length() > 0;
    }

    public static boolean isBlank(String str) {
        return !isNotBlank(str);
    }

    public static String trimClassName(String className){
        if(isBlank(className)){
            return "";
        }
        if (className.indexOf("<") > 0) {
            return className.substring(0, className.indexOf("<"));
        }
        if (className.endsWith("...")) {
            return className.replace("...","[]");
        }
        return className;
    }
}
