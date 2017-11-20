package com.transsion.api.utils;
/* Top Secret */

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.transsion.api.ApiManager;
import com.transsion.api.widget.TLog;

import java.lang.reflect.Method;

/**
 * 系统元素帮组类（状态栏，内容栏，虚拟键盘，导航栏等）
 *
 * @author 孙志刚.
 * @date 2017/1/20.
 * ==================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class ScreenUtils {
    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return ApiManager.getInstance().getApplicationContext().getResources()
                .getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度(不包括虚拟键)
     *
     * @return
     */
    public static int getScreenHeight() {
        return ApiManager.getInstance().getApplicationContext().getResources()
                .getDisplayMetrics().heightPixels;
    }

    /**
     * 获取实际屏幕尺寸
     *
     * @return
     */
    public static int getRealScreenHeight() {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) ApiManager.getInstance().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            TLog.e(e);
        }

        return dpi;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = ApiManager.getInstance().getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ApiManager.getInstance().getApplicationContext().getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public static int dp2px(float dp) {
        return (int) (ApiManager.getInstance().getApplicationContext().getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    /**
     * px转dp
     *
     * @param px
     * @return
     */
    public static int px2dp(float px) {
        return (int) (px / ApiManager.getInstance().getApplicationContext().getResources().getDisplayMetrics().density + 0.5f);
    }
}
