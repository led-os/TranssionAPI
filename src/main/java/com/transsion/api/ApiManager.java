package com.transsion.api;
/* Top Secret */

import android.content.Context;
import android.content.SharedPreferences;

import com.transacme.api.BuildConfig;

import java.io.File;

/**
 * Api全局管理器
 *
 * @author 孙志刚.
 * @date 2017/1/4.
 * ==================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class ApiManager {
    private static ApiManager sInstance = null;
    private ApiConfig mConfig = null;

    /**
     * 初始化API管理器，该函数一般放置在Application的onCreate方法中
     *
     * @param config
     */
    public static void init(ApiConfig config) {
        getInstance().mConfig = config;

        if (config.cacheFolder == null) {
            config.cacheFolder = ApiConstants.DEFAULT_FOLDER;
        }
    }

    public static ApiManager getInstance() {
        if (sInstance == null) {
            synchronized (ApiManager.class) {
                if (sInstance == null) {
                    sInstance = new ApiManager();
                }
            }
        }

        return sInstance;
    }

    public boolean isInited() {
        return mConfig != null;
    }

    public String getSDKVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public Context getApplicationContext() {
        if (mConfig != null) {
            return mConfig.getApplicationContext();
        }

        return null;
    }

    public int getAppMode() {
        if (mConfig != null && mConfig.getAppMode() >= 0) {
            return mConfig.getAppMode();
        }

        return ApiConstants.DEFAULT_APP_MODE;
    }

    public int getLogMode() {
        if (mConfig != null && mConfig.getLogMode() >= 0) {
            return mConfig.getLogMode();
        }

        return ApiConstants.DEFAULT_LOG_MODE;
    }

    public File getLogFile() {
        if (mConfig != null && mConfig.getLogFile() != null) {
            return mConfig.getLogFile();
        }

        return ApiConstants.DEFAULT_LOG_FILE;
    }

    public long getLogFileMaxSize() {
        if (mConfig != null && mConfig.getLogFileMaxSize() >= 0) {
            return mConfig.getLogFileMaxSize();
        }

        return ApiConstants.DEFAULT_MAX_LOG_FILE_SIZE;
    }

    public File getCacheFolder() {
        if (mConfig != null && mConfig.getCacheFolder() != null) {
            return mConfig.getCacheFolder();
        }

        return ApiConstants.DEFAULT_FOLDER;
    }

    public SharedPreferences getSharedPreferences() {
        if (mConfig != null && mConfig.getSharedPreferences() != null) {
            return mConfig.getSharedPreferences();
        }

        return ApiConstants.DEFAULT_SP;
    }

    public static class ApiConfig {
        protected Context applicationContext;
        protected int appMode;
        protected int logMode;
        protected File logFile;
        protected long logFileMaxSize;
        protected File cacheFolder;
        protected SharedPreferences sharedPreferences;

        public Context getApplicationContext() {
            return applicationContext;
        }

        public int getAppMode() {
            return appMode;
        }

        public int getLogMode() {
            return logMode;
        }

        public File getLogFile() {
            return logFile;
        }

        public long getLogFileMaxSize() {
            return logFileMaxSize;
        }

        public File getCacheFolder() {
            return cacheFolder;
        }

        public SharedPreferences getSharedPreferences() {
            return sharedPreferences;
        }
    }

    public static class ApiConfigBuilder {
        private Context mContext = null;
        private int appMode = -1;
        private int logMode = -1;
        private File logFile = null;
        private long logFileMaxSize = -1;
        private File cacheFolder = null;
        private SharedPreferences sharedPreferences = null;

        public ApiConfigBuilder(Context context) {
            this.mContext = context;
        }

        public ApiConfigBuilder setAppMode(int appMode) {
            this.appMode = appMode;
            return this;
        }

        public ApiConfigBuilder setLogMode(int logMode) {
            this.logMode = logMode;
            return this;
        }

        public ApiConfigBuilder setLogFile(File logFile) {
            this.logFile = logFile;
            return this;
        }

        public ApiConfigBuilder setLogFileMaxSize(long logFileMaxSize) {
            this.logFileMaxSize = logFileMaxSize;
            return this;
        }

        public ApiConfigBuilder setCacheFolder(File cacheFolder) {
            this.cacheFolder = cacheFolder;
            return this;
        }

        public ApiConfigBuilder setSharedPreferences(SharedPreferences sp) {
            this.sharedPreferences = sp;
            return this;
        }

        public ApiConfig build() {
            if (mContext == null) {
                throw new IllegalArgumentException("null context");
            }

            ApiConfig config = new ApiConfig();
            config.applicationContext = mContext.getApplicationContext();
            config.appMode = appMode;
            config.logMode = logMode;
            config.cacheFolder = cacheFolder;
            config.logFile = logFile;
            config.logFileMaxSize = logFileMaxSize;
            config.sharedPreferences = sharedPreferences;
            return config;
        }
    }
}
