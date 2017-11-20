package com.transsion.api.utils;

import com.transsion.api.widget.TLog;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 编码相关
 *
 * @author 孙志刚.
 * @date 17-3-2.
 * =======================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */

public class EncodeUtils {
    /**
     * 获取MD5码
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        return getMD5(str.getBytes());
    }

    /**
     * 获取文件的MD5码
     *
     * @param file
     * @return
     */
    public static String getMD5(File file) {
        return getMD5(FileUtils.readBytes(file));
    }

    /**
     * 获取MD5码
     *
     * @param bytes
     * @return
     */
    public static String getMD5(byte[] bytes) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            TLog.e(e);
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

}
