package com.transsion.api.utils;
/* Top Secret */

import com.transsion.api.ApiManager;
import com.transsion.api.widget.TLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/**
 * 文件工具类
 *
 * @author 孙志刚.
 * @date 2017/1/4.
 * ==================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class FileUtils {
    /**
     * 获取data目录中的file目录
     *
     * @return
     */
    public static File getDataFileDir() {
        return ApiManager.getInstance().getApplicationContext().getFilesDir();
    }

    /**
     * 获取data目录中的cache目录
     *
     * @return
     */
    public static File getDataCacheDir() {
        return ApiManager.getInstance().getApplicationContext().getCacheDir();
    }

    /**
     * 获取外部存储目录中的file目录
     *
     * @return
     */
    public static File getExternalFileDir() {
        return ApiManager.getInstance().getApplicationContext().getExternalFilesDir(null);
    }

    /**
     * 获取data目录中的cache目录
     *
     * @return
     */
    public static File getExternalCacheDir() {
        return ApiManager.getInstance().getApplicationContext().getCacheDir();
    }

    /**
     * 文件拷贝
     *
     * @param src
     * @param dst
     * @return 返回文件拷贝大小
     */
    public static boolean copy(File src, File dst) {
        if (src == null || dst == null) {
            return false;
        }

        if (src.isDirectory()) {
            dst.mkdirs();

            File[] subFiles = src.listFiles();
            if (subFiles != null) {
                for (File f : subFiles) {
                    if (f != null) {
                        if (!copy(f, new File(dst, f.getName()))) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }

        FileInputStream fi = null;
        FileOutputStream fo = null;
        try {
            fi = new FileInputStream(src);
            fo = new FileOutputStream(dst);
            FileChannel in = fi.getChannel();
            FileChannel out = fo.getChannel();
            in.transferTo(0, in.size(), out);
            return true;
        } catch (IOException e) {
            TLog.e(e);
            return false;
        } finally {
            try {
                if (fo != null) fo.close();
                if (fi != null) fi.close();
            } catch (IOException e) {
                TLog.e(e);
            }

        }
    }

    /**
     * 文件或文件夹移动
     *
     * @param src
     * @param dst
     * @return 返回是否移动成功
     */
    public static boolean move(File src, File dst) {
        if (copy(src, dst)) {
            return delete(src);
        }

        return false;
    }

    /**
     * 获取文件（指定父文件）
     *
     * @param directory
     * @param names
     * @return
     */
    public static File getFile(File directory, String... names) {
        if (directory == null || !directory.isDirectory()) {
            throw new IllegalArgumentException("file is null or file is not a directory");
        }

        File file = directory;
        for (String name : names) {
            file = new File(file, name);
        }

        return file;
    }

    /**
     * 获取文件(使用全局缓存文件夹)
     *
     * @param names
     * @return
     */
    public static File getFile(String... names) {
        File file = null;
        for (String name : names) {
            if (file == null) {
                file = new File(ApiManager.getInstance().getCacheFolder(), name);
            } else {
                file = new File(file, name);
            }
        }

        return file;
    }

    /**
     * 获取文件二进制码，适合小文件（大文件请使用流）
     *
     * @param file
     * @return
     */
    public static byte[] readBytes(File file) {
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(file);
            return IOUtils.toBytes(fi);
        } catch (IOException e) {
            TLog.e(e);
            return null;
        } finally {
            IOUtils.closeQuietly(fi);
        }
    }

    /**
     * 写入文件二进制码
     *
     * @param file
     * @param bytes
     * @return
     */
    public static boolean writeBytes(File file, byte[] bytes) {
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream(file);
            fo.write(bytes);
            fo.close();
            return true;
        } catch (IOException e) {
            TLog.e(e);
            return false;
        } finally {
            IOUtils.closeQuietly(fo);
        }
    }

    /**
     * 获取文件内容字符串(适合小文件，大文件读取请使用流)
     *
     * @param file
     * @return
     */
    public static String readString(File file) {
        byte[] bytes = readBytes(file);
        if (bytes != null) {
            return new String(bytes);
        }

        return null;
    }

    /**
     * 写入文件字符串
     *
     * @param file
     * @param content
     * @return
     */
    public static boolean writeString(File file, String content) {
        if (content != null) {
            return writeBytes(file, content.getBytes());
        } else {
            TLog.i("write string is null");
            return true;
        }
    }

    /**
     * 从文件中读取流
     *
     * @param file
     * @return
     */
    public static InputStream readStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            TLog.e(e);
            return null;
        }
    }

    /**
     * 从文件中写入流
     *
     * @param file
     * @param in
     * @return
     */
    public static boolean writeStream(File file, InputStream in) {
        FileOutputStream fos = null;
        try {
            byte[] stream = new byte[1024];
            fos = new FileOutputStream(file);

            int read;
            while ((read = in.read(stream)) >= 0) {
                fos.write(stream, 0, read);
            }
            return true;
        } catch (Exception e) {
            TLog.e(e);
            return false;
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 删除目录及文件
     *
     * @param file
     * @return
     */
    public static boolean delete(File file) {
        if (file == null) {
            return true;
        }

        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null) {
                for (File f : subFiles) {
                    if (!delete(f)) {
                        return false;
                    }
                }
            }
        }

        return file.delete();
    }
}
