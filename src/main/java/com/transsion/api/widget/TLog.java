/* Top Secret */
package com.transsion.api.widget;

import android.util.Log;

import com.transsion.api.ApiConstants;
import com.transsion.api.ApiManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * log输出
 *
 * @author 孙志刚.
 * @date 2016/9/19.
 * ==================================
 * Copyright (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class TLog {
    static final int MSG_SEP_LENGTH = 10;

    /**
     * log time format
     */
    private static SimpleDateFormat sLogFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

    /**
     * @param tag
     * @param msg  log info message
     * @param args msg format args
     */
    public static void d(String tag, String msg, Object... args) {
        if (ApiManager.getInstance().getLogMode() > 0 &&
                ApiManager.getInstance().getAppMode() != ApiConstants.APP_MODE_RELEASE) {

            if (args.length > 0) {
                msg = String.format(msg, args);
            }

            if (msg.length() < MSG_SEP_LENGTH) {
                Log.d("=!= " + tag + " =!=", msg);
            } else {
                String[] messages = convertMsgToArray(msg);
                for (String sub : messages) {
                    if (sub != null) {
                        Log.d("=!= " + tag + " =!=", sub);
                    }
                }
            }

            if ((ApiManager.getInstance().getLogMode() & ApiConstants.LOG_MODE_TO_FILE) > 0) {
                log2File(tag, msg);
            }
        }
    }

    /**
     * @param msg  log info message
     * @param args msg format args
     */
    public static void d(String msg, Object... args) {
        if (ApiManager.getInstance().getLogMode() > 0 &&
                ApiManager.getInstance().getAppMode() != ApiConstants.APP_MODE_RELEASE) {

            if (args.length > 0) {
                msg = String.format(msg, args);
            }

            if (msg.length() < MSG_SEP_LENGTH) {
                Log.d("=!= " + getCallerClassName() + " =!=", msg);
            } else {
                String[] messages = convertMsgToArray(msg);
                for (String sub : messages) {
                    if (sub != null) {
                        Log.d("=!= " + getCallerClassName() + " =!=", sub);
                    }
                }
            }

            if ((ApiManager.getInstance().getLogMode() & ApiConstants.LOG_MODE_TO_FILE) > 0) {
                log2File(getCallerClassName(), "info: " + msg);
            }
        }
    }

    /**
     * @param tag
     * @param msg  log info message
     * @param args msg format args
     */
    public static void i(String tag, String msg, Object... args) {
        if (ApiManager.getInstance().getLogMode() > 0) {
            if (args.length > 0) {
                msg = String.format(msg, args);
            }

            if (msg.length() < MSG_SEP_LENGTH) {
                Log.i("=!= " + tag + " =!=", msg);
            } else {
                String[] messages = convertMsgToArray(msg);
                for (String sub : messages) {
                    if (sub != null) {
                        Log.i("=!= " + tag + " =!=", sub);
                    }
                }
            }

            if ((ApiManager.getInstance().getLogMode() & ApiConstants.LOG_MODE_TO_FILE) > 0) {
                log2File(tag, msg);
            }

        }
    }

    /**
     * @param msg  log info message
     * @param args msg format args
     */
    public static void i(String msg, Object... args) {
        if (ApiManager.getInstance().getLogMode() > 0) {
            if (args.length > 0) {
                msg = String.format(msg, args);
            }

            if (msg.length() < MSG_SEP_LENGTH) {
                Log.i("=!= " + getCallerClassName() + " =!=", msg);
            } else {
                String[] messages = convertMsgToArray(msg);
                for (String sub : messages) {
                    if (sub != null) {
                        Log.i("=!= " + getCallerClassName() + " =!=", sub);
                    }
                }
            }

            if ((ApiManager.getInstance().getLogMode() & ApiConstants.LOG_MODE_TO_FILE) > 0) {
                log2File(getCallerClassName(), "info: " + msg);
            }
        }
    }

    /**
     * @param tag
     * @param msg  log error message
     * @param args msg format args
     */
    public static void e(String tag, String msg, Object... args) {
        if (ApiManager.getInstance().getLogMode() > 0) {
            if (args.length > 0) {
                msg = String.format(msg, args);
            }

            if (msg.length() < MSG_SEP_LENGTH) {
                Log.e("=!= " + tag + " =!=", msg);
            } else {
                String[] messages = convertMsgToArray(msg);
                for (String sub : messages) {
                    if (sub != null) {
                        Log.e("=!= " + tag + " =!=", sub);
                    }
                }
            }

            if ((ApiManager.getInstance().getLogMode() & ApiConstants.LOG_MODE_TO_FILE) > 0) {
                log2File(tag, msg);
            }

        }
    }

    /**
     * @param msg  log error message
     * @param args msg format args
     */
    public static void e(String msg, Object... args) {
        if (ApiManager.getInstance().getLogMode() > 0) {
            if (args.length > 0) {
                msg = String.format(msg, args);
            }

            if (msg.length() < MSG_SEP_LENGTH) {
                Log.e("=!= " + getCallerClassName() + " =!=", msg);
            } else {
                String[] messages = convertMsgToArray(msg);
                for (String sub : messages) {
                    if (sub != null) {
                        Log.e("=!= " + getCallerClassName() + " =!=", sub);
                    }
                }
            }

            if ((ApiManager.getInstance().getLogMode() & ApiConstants.LOG_MODE_TO_FILE) > 0) {
                log2File(getCallerClassName(), "normal error: " + msg);
            }
        }
    }

    /**
     * @param e log error exception
     */
    public static void e(Throwable e) {
        if (ApiManager.getInstance().getLogMode() > 0) {
            String msg = exceptionToString(e);

            if (msg.length() < MSG_SEP_LENGTH) {
                Log.e("=!= " + getCallerClassName() + " =!=", msg);
            } else {
                String[] messages = convertMsgToArray(msg);
                for (String sub : messages) {
                    if (sub != null) {
                        Log.e("=!= " + getCallerClassName() + " =!=", sub);
                    }
                }
            }

            if ((ApiManager.getInstance().getLogMode() & ApiConstants.LOG_MODE_TO_FILE) > 0) {
                log2File(getCallerClassName(), "throwable error: " + msg);
            }
        }
    }

    /**
     * 将长字符串分隔成若干个短字符串
     *
     * @param msg
     * @return
     */
    private static String[] convertMsgToArray(String msg) {
        if (msg != null) {
            String[] messages = new String[msg.length() / MSG_SEP_LENGTH + ((msg.length() % MSG_SEP_LENGTH == 0) ? 0 : 1)];
            for (int n = 0; n < messages.length; n++) {
                messages[n] = msg.substring(n * MSG_SEP_LENGTH, Math.min((n + 1) * MSG_SEP_LENGTH, msg.length()));
            }

            return messages;
        }

        return null;
    }

    /**
     * 获取log行数
     *
     * @return
     */
    private static String getCallerClassName() {
        StackTraceElement ele = new Throwable().getStackTrace()[2];
        return ele.getFileName() + ":" + ele.getLineNumber();
    }

    /**
     * @param tag
     * @param msg log to file
     */
    private static void log2File(String tag, String msg) {
        if (ApiManager.getInstance().getLogFile() != null &&
                ApiManager.getInstance().getLogFile().length() > ApiManager.getInstance().getLogFileMaxSize()) {

            //清空文件log
            ApiManager.getInstance().getLogFile().delete();
        }

        appendLog(sLogFormat.format(new Date()) + " =!= " + tag + " =!=: " + msg + "\r\n");
    }

    /**
     * @param t
     * @return
     * @throws IOException exception to string
     */
    private static String exceptionToString(Throwable t) {
        if (t == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            t.printStackTrace(new PrintStream(baos));
        } finally {
            try {
                baos.close();
            } catch (IOException e) {

            }
        }

        return baos.toString();
    }

    /**
     * 追加log到文件
     *
     * @param msg
     * @return
     */
    private static boolean appendLog(String msg) {
        try {
            RandomAccessFile randomFile = new RandomAccessFile(ApiManager.getInstance().getLogFile(), "rw");
            randomFile.seek(randomFile.length());
            randomFile.write(msg.getBytes());
            randomFile.close();

            return true;
        } catch (Exception e) {
            System.out.println("append file error");
        }

        return false;
    }
}
