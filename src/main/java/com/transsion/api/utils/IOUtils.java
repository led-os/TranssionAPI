package com.transsion.api.utils;

import com.transsion.api.widget.TLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * io工具类
 *
 * @author 孙志刚.
 * @date 17-1-30.
 * =======================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */

public class IOUtils {
    /**
     * 默认buffer（4K）
     */
    public static final int DEFAULT_BUFFER_SIZE = 4 << 10;

    /**
     * 安静模式关闭io
     *
     * @param io
     */
    public static void closeQuietly(Closeable io) {
        try {
            if (io != null) {
                io.close();
            }
        } catch (IOException e) {
            TLog.e(e);
        }
    }

    /**
     * 将input复制到output
     *
     * @param in
     * @param out
     * @return
     * @throws IOException
     */
    public static long copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        while (-1 != (n = in.read(buffer))) {
            out.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * stream转换为byte数组
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] toBytes(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        copy(is, bos);
        return bos.toByteArray();
    }

    /**
     * 字节转换为inputStream
     *
     * @param bytes
     * @return
     */
    public static InputStream toStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }
}
