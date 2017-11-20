package com.transsion.api.utils;

import android.content.SharedPreferences;

import com.transsion.api.ApiManager;
import com.transsion.api.widget.TLog;

import java.util.Set;

/**
 * SharePreference相关
 *
 * @author 孙志刚.
 * @date 17-1-29.
 * =======================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */

public class SPUtils {
    /**
     * 获取sp实例
     *
     * @return
     */
    private static SharedPreferences getInstance() {
        return ApiManager.getInstance().getSharedPreferences();
    }

    /**
     * 清除key
     *
     * @param key
     */
    public static void clear(String key) {
        getInstance().edit().remove(key).apply();
    }

    /**
     * 写入sp
     *
     * @param key
     * @param value
     */
    public static void put(String key, Object value) {
        if (value == null) {
            TLog.i("put null sp, skip");
            return;
        }

        if (value instanceof String) {
            getInstance().edit().putString(key, (String) value).apply();
        } else if (value instanceof Long) {
            getInstance().edit().putLong(key, (Long) value).apply();
        } else if (value instanceof Integer) {
            getInstance().edit().putInt(key, (Integer) value).apply();
        } else if (value instanceof Float) {
            getInstance().edit().putFloat(key, (Float) value).apply();
        } else if (value instanceof Boolean) {
            getInstance().edit().putBoolean(key, (Boolean) value).apply();
        } else if (value instanceof Set) {
            getInstance().edit().putStringSet(key, (Set<String>) value).apply();
        } else {
            TLog.e("not support type of value");
        }
    }

    /**
     * 获取字符串(默认空字符串)
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getInstance().getString(key, "");
    }

    /**
     * 获取字符串
     *
     * @param key
     * @return
     */
    public static String getString(String key, String def) {
        return getInstance().getString(key, def);
    }

    /**
     * 获取长整形(默认0)
     *
     * @param key
     * @return
     */
    public static Long getLong(String key) {
        return getInstance().getLong(key, 0l);
    }

    /**
     * 获取长整形
     *
     * @param key
     * @return
     */
    public static Long getLong(String key, Long def) {
        return getInstance().getLong(key, def);
    }

    /**
     * 获取整形(默认0)
     *
     * @param key
     * @return
     */
    public static Integer getInt(String key) {
        return getInstance().getInt(key, 0);
    }

    /**
     * 获取整形
     *
     * @param key
     * @return
     */
    public static Integer getInt(String key, Integer def) {
        return getInstance().getInt(key, def);
    }

    /**
     * 获取浮点型(默认0)
     *
     * @param key
     * @return
     */
    public static Float getFloat(String key) {
        return getInstance().getFloat(key, 0f);
    }

    /**
     * 获取浮点型
     *
     * @param key
     * @return
     */
    public static Float getFloat(String key, Float def) {
        return getInstance().getFloat(key, def);
    }

    /**
     * 获取布尔型(默认false)
     *
     * @param key
     * @return
     */
    public static Boolean getBoolean(String key) {
        return getInstance().getBoolean(key, false);
    }

    /**
     * 获取布尔型
     *
     * @param key
     * @return
     */
    public static Boolean getBoolean(String key, Boolean def) {
        return getInstance().getBoolean(key, def);
    }

    /**
     * 获取字符串数组(默认空)
     *
     * @param key
     * @return
     */
    public static Set<String> getStringSet(String key) {
        return getInstance().getStringSet(key, null);
    }

    /**
     * 获取字符串数组
     *
     * @param key
     * @return
     */
    public static Set<String> getStringSet(String key, Set<String> def) {
        return getInstance().getStringSet(key, def);
    }
}
