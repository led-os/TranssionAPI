package com.transsion.api.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.transsion.api.ApiManager;

/**
 * 网络相关工具类
 *
 * @author 孙志刚.
 * @date 2017/1/4.
 * ==================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class NetUtils {
    public static final int NETWORK_TYPE_DISCONNECT = 0;
    public static final int NETWORK_TYPE_WIFI = NETWORK_TYPE_DISCONNECT + 1;
    public static final int NETWORK_TYPE_3G = NETWORK_TYPE_WIFI + 1;
    public static final int NETWORK_TYPE_2G = NETWORK_TYPE_3G + 1;
    public static final int NETWORK_TYPE_WAP = NETWORK_TYPE_2G + 1;
    public static final int NETWORK_TYPE_UNKNOWN = NETWORK_TYPE_WAP + 1;

    /**
     * Get network type
     *
     * @return
     */
    public static int getNetworkType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ApiManager.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
        return networkInfo == null ? -1 : networkInfo.getType();
    }

    /**
     * Get network type name
     *
     * @return
     */
    public static int getNetworkTypeName() {
        ConnectivityManager manager = (ConnectivityManager) ApiManager.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        int type = NETWORK_TYPE_DISCONNECT;
        if (manager == null || (networkInfo = manager.getActiveNetworkInfo()) == null) {
            return type;
        }

        if (networkInfo.isConnected()) {
            String typeName = networkInfo.getTypeName();
            if ("WIFI".equalsIgnoreCase(typeName)) {
                type = NETWORK_TYPE_WIFI;
            } else if ("MOBILE".equalsIgnoreCase(typeName)) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                type = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork() ? NETWORK_TYPE_3G : NETWORK_TYPE_2G)
                        : NETWORK_TYPE_WAP;
            } else {
                type = NETWORK_TYPE_UNKNOWN;
            }
        }
        return type;
    }

    /**
     * Whether is fast mobile network
     *
     * @return
     */
    private static boolean isFastMobileNetwork() {
        TelephonyManager telephonyManager = (TelephonyManager)
                ApiManager.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return false;
        }

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    /**
     * 判断网络是否有效
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        return getNetworkTypeName() != NETWORK_TYPE_DISCONNECT;
    }
}
