package com.transsion.api.utils;
/* Top Secret */

import com.transsion.api.widget.TLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Zip操作相关
 *
 * @author 孙志刚.
 * @date 2017/2/21.
 * ==================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class ZipUtils {
    /**
     * 从输入文件中解压
     *
     * @param in
     * @param output
     * @return
     */
    public static boolean unzip(File in, File output) {
        try {
            return unzip(new FileInputStream(in), output);
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    /**
     * 从inputstream中解压到指定文件夹/文件
     *
     * @param in
     * @param outputDirectory
     * @return
     * @throws IOException
     */
    public static boolean unzip(InputStream in, File outputDirectory) {
        if (in == null)
            return false;

        ZipEntry zipEntry;
        FileOutputStream out = null;
        ZipInputStream zipIn = new ZipInputStream(in);
        byte[] cacheStream = new byte[1024];
        try {
            while ((zipEntry = zipIn.getNextEntry()) != null) {
                //如果是文件夹路径方式，本方法内暂时不提供操作
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    File file = new File(outputDirectory, name);
                    file.mkdirs();
                } else {
                    //如果是文件，则直接在对应路径下生成
                    File file = new File(outputDirectory, zipEntry.getName());
                    out = new FileOutputStream(file);

                    int b;
                    while ((b = zipIn.read(cacheStream)) != -1) {
                        out.write(cacheStream, 0, b);
                    }

                    out.close();
                }
            }
            return true;
        } catch (Exception ex) {
            TLog.e(ex);
            return false;
        } finally {
            IOUtils.closeQuietly(zipIn);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 压缩文件
     *
     * @param in
     * @param out
     * @return
     */
    public static boolean zip(File in, File out) {
        ZipOutputStream outputStream = null;
        try {
            outputStream = new ZipOutputStream(new FileOutputStream(out));
            zipInternal(in.getParent() + File.separator, in.getName(), outputStream);
            outputStream.finish();
            return true;
        } catch (Exception e) {
            TLog.e(e);
            return false;
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * zip压缩核心方法（内部方法）
     *
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     * @return
     * @throws IOException
     */
    private static boolean zipInternal(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws IOException {
        if (zipOutputSteam == null) {
            return false;
        }

        File file = new File(folderString + fileString);

        //判断是不是文件
        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(fileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);

            int len;
            byte[] buffer = new byte[4096];

            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }

            zipOutputSteam.closeEntry();
            IOUtils.closeQuietly(inputStream);
        } else {
            //文件夹的方式,获取文件夹下的子文件
            String fileList[] = file.list();

            //如果没有子文件, 则添加进去即可
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }

            //如果有子文件, 遍历子文件
            for (int i = 0; i < fileList.length; i++) {
                zipInternal(folderString, fileString + File.separator + fileList[i], zipOutputSteam);
            }
        }

        return true;
    }
}
