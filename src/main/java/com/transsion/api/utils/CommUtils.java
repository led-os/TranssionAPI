package com.transsion.api.utils;
/* Top Secret */

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Binder;

import com.transsion.api.ApiManager;
import com.transsion.api.widget.TLog;

import java.io.IOException;
import java.io.InputStream;

/**
 * 常用工具类
 *
 * @author 孙志刚.
 * @date 2017/1/4.
 * ==================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class CommUtils {
    /**
     * 获取meta数据
     *
     * @param key
     * @return
     */
    public static Object getMetaData(String key) {
        try {
            ApplicationInfo appInfo = ApiManager.getInstance().getApplicationContext().getPackageManager()
                    .getApplicationInfo(ApiManager.getInstance().getApplicationContext().getPackageName(),
                            PackageManager.GET_META_DATA);
            return appInfo.metaData.get(key);
        } catch (PackageManager.NameNotFoundException e) {
            TLog.e(e);
        }

        return null;
    }

    /**
     * 获取versionName
     *
     * @return
     */
    public static String getVersionName() {
        try {
            return ApiManager.getInstance().getApplicationContext().getPackageManager().getPackageInfo(
                    ApiManager.getInstance().getApplicationContext().getPackageName(),
                    0
            ).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            TLog.e(e);
        }

        return null;
    }

    /**
     * 获取versionCode
     *
     * @return
     */
    public static int getVersionCode() {
        try {
            return ApiManager.getInstance().getApplicationContext().getPackageManager().getPackageInfo(
                    ApiManager.getInstance().getApplicationContext().getPackageName(),
                    0
            ).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            TLog.e(e);
        }

        return -1;
    }

    /**
     * 判断屏幕是否为竖屏
     *
     * @param activity
     * @return
     */
    public static boolean isScreenPortrait(Activity activity) {
        Configuration configuration = activity.getResources().getConfiguration();
        return configuration.orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 从assets读取inputstream
     *
     * @param assetName
     * @return
     * @throws IOException
     */
    public static InputStream getStreamFromAssets(String assetName) throws IOException {
        return ApiManager.getInstance().getApplicationContext().getAssets().open(assetName);
    }

    /**
     * 从classpath获取指定流
     *
     * @param cls
     * @param name
     * @return
     */
    public static InputStream getStreamFromClassPath(Class cls, String name) {
        return cls.getResourceAsStream(name);
    }

    /**
     * 根据资源id获取stream流
     *
     * @param res_id
     * @return
     */
    public static InputStream getStreamByResourceId(int res_id) {
        return ResourceUtils.getStreamByResourceId(res_id);
    }

    /**
     * 判断activity是否定义
     *
     * @param cls
     * @return
     */
    public static boolean isActivityDefined(Class<? extends Activity> cls) {
        PackageManager pm = ApiManager.getInstance().getApplicationContext().getPackageManager();
        return !EmptyUtils.isListEmpty(pm.queryIntentActivities(new Intent(
                ApiManager.getInstance().getApplicationContext(),
                cls
        ), 0));
    }

    /**
     * 判断service是否定义
     *
     * @param cls
     * @return
     */
    public static boolean isServiceDefined(Class<? extends Service> cls) {
        PackageManager pm = ApiManager.getInstance().getApplicationContext().getPackageManager();
        return !EmptyUtils.isListEmpty(pm.queryIntentServices(new Intent(
                ApiManager.getInstance().getApplicationContext(),
                cls
        ), 0));
    }

    /**
     * 判断receiver是否定义
     *
     * @param cls
     * @return
     */
    public static boolean isReceiverDefined(Class<? extends BroadcastReceiver> cls) {
        PackageManager pm = ApiManager.getInstance().getApplicationContext().getPackageManager();
        return !EmptyUtils.isListEmpty(pm.queryBroadcastReceivers(new Intent(
                ApiManager.getInstance().getApplicationContext(),
                cls
        ), 0));
    }

    /**
     * 获取uid
     *
     * @return
     */
    public static int getUID() {
        return Binder.getCallingUid();
    }

    /**
     * 获取pid
     *
     * @return
     */
    public static int getPID() {
        return Binder.getCallingPid();
    }
}