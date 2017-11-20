package com.transsion.api.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据验证相关
 *
 * @author 孙志刚.
 * @date 17-1-28.
 * =======================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */

public class ValidateUtils {
    /**
     * 判断是否为email地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (EmptyUtils.isTextEmpty(email)) {
            return false;
        }

        return Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*").matcher(email).matches();
    }

    /**
     * 判断是否为手机号码（国内号码）
     *
     * @param number
     * @return
     */
    public static boolean isMobile(String number) {
        if (EmptyUtils.isTextEmpty(number)) {
            return false;
        }

        return Pattern.compile("^[1][0-9][0-9]{9}$").matcher(number).matches();
    }

    /**
     * 判断是否为手机号码或者固定号码（国内号码）
     *
     * @param number
     * @return
     */
    public static boolean isTelephone(String number) {
        if (EmptyUtils.isTextEmpty(number)) {
            return false;
        }

        boolean result;
        result = Pattern.compile("^[1][0-9][0-9]{9}$").matcher(number).matches();

        if (!result) {
            result = Pattern.compile("([0-9]{3,4})?[0-9]{7,8}").matcher(number).matches();
        }

        return result;
    }

    /**
     * 判断是否为纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否为纯ascii
     *
     * @param ascii
     * @return
     */
    public static boolean isAsciiString(String ascii) {
        if (EmptyUtils.isTextEmpty(ascii)) {
            return true;
        }

        char[] chars = ascii.toCharArray();
        for (char c : chars) {
            if (c > 127) {
                return false;
            }
        }

        return true;
    }
}
