package com.transsion.api.utils;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.transsion.api.ApiManager;
import com.transsion.api.widget.TLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片处理相关
 *
 * @author 孙志刚.
 * @date 17-1-28.
 * =======================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */

public class ImageUtils {

    /**
     * 从文件中获取bitmap描述信息（宽高类型）
     *
     * @param file
     * @return
     */
    public static BitmapFactory.Options getBitmapInfoFromFile(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(fis, null, options);
            return options;
        } catch (FileNotFoundException e) {
            TLog.e(e);
        } finally {
            IOUtils.closeQuietly(fis);
        }

        return null;
    }

    /**
     * 从资源文件中获取信息描述
     *
     * @param res_id
     * @return
     */
    public static BitmapFactory.Options getBitmapInfoFromResource(int res_id) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream is = ResourceUtils.getStreamByResourceId(res_id);
        BitmapFactory.decodeStream(is, null, options);
        IOUtils.closeQuietly(is);
        return options;
    }

    /**
     * 根据option解析缩放值
     *
     * @param options
     * @param width
     * @param height
     * @return
     */
    public static int calcSampleSize(BitmapFactory.Options options, int width, int height) {
        if (options == null) {
            return 1;
        }

        return (int) Math.floor(Math.min(options.outWidth / (double) width, options.outHeight / (double) height));
    }

    /**
     * 从文件中获取bitmap(按原始比例)
     *
     * @param file
     * @return
     */
    public static Bitmap getBitmapFromFile(File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    /**
     * 从文件中获取bitmap(适应宽高)
     *
     * @param file
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapFromFile(File file, int width, int height) {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = calcSampleSize(getBitmapInfoFromFile(file), width, height);
            return BitmapFactory.decodeStream(fis, null, options);
        } catch (IOException e) {
            TLog.e(e);
        } finally {
            IOUtils.closeQuietly(fis);
        }

        return null;
    }

    /**
     * 从资源中获取bitmap
     *
     * @param res_id
     * @return
     */
    public static Bitmap getBitmapFromResource(int res_id) {
        InputStream is = ResourceUtils.getStreamByResourceId(res_id);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        IOUtils.closeQuietly(is);
        return bitmap;
    }

    /**
     * 从资源中获取bitmap(指定宽高)
     *
     * @param res_id
     * @return
     */
    public static Bitmap getBitmapFromResource(int res_id, int width, int height) {
        InputStream ris = null;

        try {
            ris = ResourceUtils.getStreamByResourceId(res_id);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = calcSampleSize(getBitmapInfoFromResource(res_id), width, height);
            return BitmapFactory.decodeStream(ris, null, options);
        } finally {
            IOUtils.closeQuietly(ris);
        }
    }

    /**
     * bitmap to bytes
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }

    /**
     * bitmap to bytes(可指定压缩比率)
     *
     * @param bitmap
     * @param compressLevel
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bitmap, int compressLevel) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressLevel, bos);
        return bos.toByteArray();
    }

    /**
     * 图片圆角处理
     *
     * @param bitmap
     * @param radius
     * @return
     */
    public static Bitmap roundCorner(Bitmap bitmap, int radius, boolean isRecycleSrc) {
        try {
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();

            Bitmap paintingBoard = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(paintingBoard);
            canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);

            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);

            final RectF rectF = new RectF(0, 0, width, height);
            canvas.drawRoundRect(rectF, radius, radius, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, width, height);
            final Rect dst = src;
            canvas.drawBitmap(bitmap, src, dst, paint);
            if (isRecycleSrc) {
                bitmap.recycle();
            }

            return paintingBoard;
        } catch (Exception exp) {
            return bitmap;
        }
    }

    /**
     * 图片圆形处理
     *
     * @param bitmap
     * @param isRecycleSrc
     * @return
     */
    public static Bitmap roundAsCircle(Bitmap bitmap, boolean isRecycleSrc) {
        try {
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();

            Bitmap paintingBoard = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(paintingBoard);
            canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);

            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);

            canvas.drawCircle(width / 2, height / 2, Math.min(width / 2, height / 2), paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, width, height);
            final Rect dst = src;
            canvas.drawBitmap(bitmap, src, dst, paint);
            if (isRecycleSrc) {
                bitmap.recycle();
            }

            return paintingBoard;
        } catch (Exception exp) {
            return bitmap;
        }
    }

    /**
     * 高斯模糊(API >= 17)
     *
     * @param bitmap
     * @param radius
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurBitmap(Bitmap bitmap, int radius, boolean isRecycleSrc) {
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(ApiManager.getInstance().getApplicationContext());

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(radius);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        if (isRecycleSrc) {
            //recycle the original bitmap
            bitmap.recycle();
        }

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();
        return outBitmap;
    }
}
