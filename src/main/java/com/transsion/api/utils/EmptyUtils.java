package com.transsion.api.utils;

import android.text.TextUtils;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

/**
 * 数据相关
 *
 * @author 孙志刚.
 * @date 17-1-28.
 * =======================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */

public class EmptyUtils {
    /**
     * 判断字符串是否为空
     *
     * @param text
     * @return
     */
    public static boolean isTextEmpty(String text) {
        return TextUtils.isEmpty(text);
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     * @return
     */
    public static boolean isArrayEmpty(Object array) {
        if (array != null && !array.getClass().isArray()) {
            throw new IllegalArgumentException("param is not an array");
        }

        if (array == null ||
                Array.getLength(array) == 0) {
            return true;
        }

        return false;
    }

    /**
     * 判断list是否为空
     *
     * @param list
     * @return
     */
    public static boolean isListEmpty(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }

        return false;
    }

    /**
     * 判断map是否为空
     *
     * @param map
     * @return
     */
    public static boolean isMapEmpty(Map map) {
        if (map == null || map.size() == 0) {
            return true;
        }

        return false;
    }
}
