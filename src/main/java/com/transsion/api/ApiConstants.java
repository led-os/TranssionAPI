package com.transsion.api;
/* Top Secret */

import android.content.SharedPreferences;

import java.io.File;

/**
 * Api常量
 *
 * @author 孙志刚.
 * @date 2017/1/22.
 * ==================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class ApiConstants {
    // =========================== APP MODE ===========================
    /**
     * 发布模式
     */
    public static final int APP_MODE_RELEASE = 0;
    /**
     * 调试模式
     */
    public static final int APP_MODE_DEBUG = 1;
    /**
     * 测试模式
     */
    public static final int APP_MODE_TEST = 2;

    // =========================== LOG MODE ===========================
    /**
     * 不输出log
     */
    public static final int LOG_MODE_NULL = 0;
    /**
     * 输出log到console
     */
    public static final int LOG_MODE_TO_CONSOLE = 1;
    /**
     * 输出log到文件
     */
    public static final int LOG_MODE_TO_FILE = LOG_MODE_TO_CONSOLE << 1;
    /**
     * 同时输出log到console和文件
     */
    public static final int LOG_MODE_TO_ALL = (LOG_MODE_TO_CONSOLE | LOG_MODE_TO_FILE);

    // ======================== 默认变量定义 ==============================
    /**
     * 默认app mode
     */
    public static final int DEFAULT_APP_MODE = APP_MODE_RELEASE;
    /**
     * 默认文件缓存文件夹(包含log/image等)
     */
    public static final File DEFAULT_FOLDER =
            ApiManager.getInstance().getApplicationContext().getExternalFilesDir(null) == null
                    ? ApiManager.getInstance().getApplicationContext().getFileStreamPath("")
                    : ApiManager.getInstance().getApplicationContext().getExternalFilesDir(null);
    /**
     * 默认log输出方式
     */
    public static final int DEFAULT_LOG_MODE = LOG_MODE_TO_CONSOLE;
    /**
     * 默认log文件
     */
    public static final File DEFAULT_LOG_FILE = new File(DEFAULT_FOLDER, "log.txt");
    /**
     * 默认log文件最大值
     */
    public static final long DEFAULT_MAX_LOG_FILE_SIZE = 1 << 20; //1M
    /**
     * 默认sp
     */
    public static final SharedPreferences DEFAULT_SP = ApiManager.getInstance().getApplicationContext()
            .getSharedPreferences(ApiManager.getInstance().getApplicationContext().getPackageName() + "_tapi", 0);
}
