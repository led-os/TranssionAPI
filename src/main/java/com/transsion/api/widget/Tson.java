package com.transsion.api.widget;

import com.transsion.api.ApiManager;
import com.transsion.api.utils.EmptyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * json解析工具类
 * <p>
 * DEFAULT模式只支持基础类及其包装类、String类、简单bean类和数组类型申明的变量
 *
 * @author 孙志刚.
 * @date 17-11-18.
 * ==============================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class Tson {
    public static final int MODE_UNKNOWN = 0;
    public static final int MODE_DEFAULT = MODE_UNKNOWN + 1;
    public static final int MODE_GSON = MODE_DEFAULT + 1;
    public static final Class[] BASE_CLASS = new Class[]{
            int.class,
            float.class,
            double.class,
            byte.class,
            long.class,
            Integer.class,
            Float.class,
            Double.class,
            Byte.class,
            Long.class,
            String.class
    };

    /**
     * 使用模式
     */
    private static int mode = 0;
    /**
     * 临时对象
     */
    private static Object tempObject = null;

    /**
     * 获取当前解析模式
     *
     * @return
     */
    public static int getMode() {
        return mode;
    }

    private static void initMode() {
        if (mode == MODE_UNKNOWN) {
            try {
                Class.forName("com.google.gson.Gson");
                mode = MODE_GSON;
            } catch (ClassNotFoundException e) {
                mode = MODE_DEFAULT;
            }
        }
    }

    private static boolean isBaseClass(Class cls) {
        for (Class sub : BASE_CLASS) {
            if (sub.equals(cls)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 将jsonobject对象解析为bean
     *
     * @param obj
     * @param cls
     * @return
     */
    private static Object fromJsonObject(JSONObject obj, Class cls) throws IllegalAccessException, InstantiationException, JSONException, InvocationTargetException {
        Object bean = cls.newInstance();
        Method[] methods = cls.getMethods();

        for (Iterator<String> it = obj.keys(); it.hasNext(); ) {
            String key = it.next();
            String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1, key.length());
            Method setMethod = null;
            for (Method method : methods) {
                if (methodName.equals(method.getName()) &&
                        method.getParameterTypes().length == 1 &&
                        method.getDeclaringClass().equals(cls)) {

                    setMethod = method;
                    break;
                }
            }

            if (setMethod != null) {
                Object value = obj.get(key);
                if (value instanceof JSONObject) {
                    value = fromJsonObject((JSONObject) value, setMethod.getParameterTypes()[0]);
                } else if (value instanceof JSONArray) {
                    value = fromJsonArray((JSONArray) value, setMethod.getParameterTypes()[0]);
                }

                setMethod.setAccessible(true);
                try {
                    setMethod.invoke(bean, value);
                } catch (IllegalArgumentException e) {
                    TLog.e(e);
                    continue;
                }
            }
        }

        return bean;
    }

    /**
     * 将jsonarray对象解析为bean对象
     *
     * @param obj
     * @param cls
     * @return
     */
    private static Object fromJsonArray(JSONArray obj, Class cls) throws JSONException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //数组
        Object beanArray = Array.newInstance(cls.getComponentType(), obj.length());

        for (int n = 0; n < obj.length(); n++) {
            Object sub = obj.get(n);
            if (sub instanceof JSONObject) {
                Array.set(beanArray, n, fromJsonObject((JSONObject) sub, cls.getComponentType()));
            } else if (sub instanceof JSONArray) {
                Array.set(beanArray, n, fromJsonArray((JSONArray) sub, cls.getComponentType()));
            } else {
                Array.set(beanArray, n, sub);
            }
        }

        return beanArray;
    }

    public static <T> T fromJsonString(String json, Class<T> cls) {
        initMode();
        switch (mode) {
            case MODE_DEFAULT:
                try {
                    if (cls.isArray()) {
                        //数组
                        return (T) fromJsonArray(new JSONArray(json), cls);
                    } else {
                        //非数组
                        return (T) fromJsonObject(new JSONObject(json), cls);
                    }
                } catch (Exception e) {
                    TLog.e(e);
                }
                break;
            case MODE_GSON:
                try {
                    Class gsonCls = ApiManager.getInstance().getApplicationContext().getClassLoader()
                            .loadClass("com.google.gson.Gson");
                    if (tempObject == null) {
                        tempObject = gsonCls.newInstance();
                    }
                    Method fromJson = gsonCls.getMethod("fromJson", String.class, Class.class);
                    fromJson.setAccessible(true);
                    return (T) fromJson.invoke(tempObject, json, cls);
                } catch (Exception e) {
                    TLog.e(e);
                }
                break;
        }

        return null;
    }

    private static JSONArray toJsonArray(Object obj) throws IllegalAccessException, JSONException, InvocationTargetException {
        JSONArray array = new JSONArray();

        for (int n = 0; n < Array.getLength(obj); n++) {
            Object child = Array.get(obj, n);

            if (isBaseClass(child.getClass())) {
                //如果为基础类，那么直接赋值
                array.put(child);
            } else if (child.getClass().isArray()) {
                array.put(toJsonArray(child));
            } else if (child.getClass().getName().startsWith("java")) {
                throw new IllegalArgumentException("Illegal element at " + n);
            } else {
                array.put(toJsonObject(child));
            }
        }

        return array;
    }

    private static JSONObject toJsonObject(Object obj) throws InvocationTargetException, IllegalAccessException, JSONException {
        JSONObject object = new JSONObject();
        Method[] methods = obj.getClass().getMethods();

        for (Method method : methods) {
            if (method.getName().startsWith("get") && method.getParameterTypes().length == 0
                    && method.getDeclaringClass().equals(obj.getClass())) {
                String name = method.getName().substring(3);
                if (!EmptyUtils.isTextEmpty(name)) {
                    char first = name.charAt(0);
                    if (first >= 'A' && first <= 'Z') {
                        name = Character.toLowerCase(first) + name.substring(1);
                        Object value = method.invoke(obj);

                        if (value.getClass().getName().startsWith("java.lang") ||
                                !value.getClass().getName().contains(".")) {
                            //如果为基础类，那么直接赋值
                            object.put(name, value);
                        } else if (value.getClass().isArray()) {
                            object.put(name, toJsonArray(value));
                        } else {
                            object.put(name, toJsonObject(value));
                        }
                    }
                }
            }
        }

        return object;
    }

    public static <T> String toJsonString(T obj) {
        initMode();

        switch (mode) {
            case MODE_DEFAULT:
                try {
                    if (obj.getClass().isArray()) {
                        JSONArray array = toJsonArray(obj);
                        return array == null ? null : array.toString();
                    } else {
                        JSONObject object = toJsonObject(obj);
                        return object == null ? null : object.toString();
                    }
                } catch (Exception e) {
                    TLog.e(e);
                }
            case MODE_GSON:
                try {
                    Class gsonCls = ApiManager.getInstance().getApplicationContext().getClassLoader()
                            .loadClass("com.google.gson.Gson");
                    if (tempObject == null) {
                        tempObject = gsonCls.newInstance();
                    }

                    Method toJson = gsonCls.getMethod("toJson", Object.class);
                    toJson.setAccessible(true);
                    return (String) toJson.invoke(tempObject, obj);
                } catch (Exception e) {
                    TLog.e(e);
                }
                break;
        }

        return null;
    }
}
