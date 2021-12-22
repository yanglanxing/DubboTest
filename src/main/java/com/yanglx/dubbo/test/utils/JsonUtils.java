package com.yanglx.dubbo.test.utils;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final Gson prettyGson = (new GsonBuilder())
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private static final Gson uglyGson = (new GsonBuilder())
            .serializeNulls()
            .create();

    public static String toJSONString(Object obj) {
        return uglyGson.toJson(obj);
    }

    public static String toPrettyJSONString(Object obj) {
        return prettyGson.toJson(obj);
    }

    public static <T> T toJava(String json, Class<T> tClass) {
        return uglyGson.fromJson(json,tClass);
    }

    public static <T> List<T> toJavaList(String json, Class<T> tClass) {
        List<T> list = new ArrayList<T>();
        JsonArray jsonElements = JsonParser.parseString(json).getAsJsonArray();
        for (JsonElement elem : jsonElements) {
            list.add(uglyGson.fromJson(elem, tClass));
        }
        return list;
    }
}
