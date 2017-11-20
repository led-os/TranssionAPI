package com.transsion.api.utils;
/* Top Secret */

import android.content.ContentResolver;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

import com.transsion.api.ApiManager;

import java.io.InputStream;

/**
 * 资源工具类
 *
 * @author 孙志刚.
 * @date 2017/1/4.
 * ==================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class ResourceUtils {
    /**
     * 获取系统resources
     *
     * @return
     */
    public static Resources getResources() {
        return ApiManager.getInstance().getApplicationContext().getResources();
    }

    /**
     * 获取颜色
     *
     * @param res_id
     * @return
     */
    public static int getColor(int res_id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(res_id, null);
        } else {
            return getResources().getColor(res_id);
        }
    }

    /**
     * 获取颜色状态表
     *
     * @param res_id
     * @return
     */
    public static ColorStateList getColorStateList(int res_id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColorStateList(res_id, null);
        } else {
            return getResources().getColorStateList(res_id);
        }
    }

    /**
     * 获取Drawable
     *
     * @param res_id
     * @return
     */
    public static Drawable getDrawable(int res_id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getDrawable(res_id, null);
        } else {
            return getResources().getDrawable(res_id);
        }
    }

    /**
     * 获取Drawable并设置默认bound
     *
     * @param res_id
     * @return
     */
    public static Drawable getDrawableWithDefaultBounds(int res_id) {
        Drawable drawable = getDrawable(res_id);

        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }

        return drawable;
    }

    /**
     * 获取字串
     *
     * @param res_id
     * @return
     */
    public static String getString(int res_id) {
        return getResources().getString(res_id);
    }

    /**
     * 获取带参数字串
     *
     * @param res_id
     * @param formatArgs
     * @return
     */
    public static String getString(int res_id, Object... formatArgs) {
        return getResources().getString(res_id, formatArgs);
    }

    /**
     * 获取Dimension
     *
     * @param res_id
     * @return
     */
    public static float getDimension(int res_id) {
        return getResources().getDimension(res_id);
    }

    /**
     * 根据资源id获取stream流
     *
     * @param res_id
     * @return
     */
    public static InputStream getStreamByResourceId(int res_id) {
        return getResources().openRawResource(res_id);
    }

    /**
     * 根据resource id获取 uri string
     *
     * @param res_id
     * @return
     */
    public static String getUriStringByResourceId(int res_id) {
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + getResources().getResourcePackageName(res_id) + "/"
                + getResources().getResourceTypeName(res_id) + "/"
                + getResources().getResourceEntryName(res_id);
    }
}