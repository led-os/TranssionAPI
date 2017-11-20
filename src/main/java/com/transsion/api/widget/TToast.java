package com.transsion.api.widget;

import android.widget.Toast;

import com.transsion.api.ApiManager;
import com.transsion.api.utils.EmptyUtils;

/**
 * 自定义toast
 *
 * @author 孙志刚.
 * @date 17-1-29.
 * =======================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */

public class TToast {
    /**
     * 显示toast
     *
     * @param msg
     */
    public static void show(String msg) {
        if (!EmptyUtils.isTextEmpty(msg)) {
            Toast.makeText(ApiManager.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示toast(只在调试模式下可用)
     *
     * @param msg
     */
    public static void debug(String msg) {
        if (ApiManager.getInstance().getAppMode() != 0) {
            show(msg);
        }
    }
}
