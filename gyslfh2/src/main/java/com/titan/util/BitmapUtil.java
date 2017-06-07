package com.titan.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.titan.gyslfh.TitanApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


/**
 * Created by whs on 2017/5/23
 */

public class BitmapUtil {
    private static final String TAG = "TITAN";


    /**
     * 图片压缩
     * @param imgName
     */
    public static String compressBitmap(String path,String imgName) {
        try {
            File file = new File(path);
            /*Log.i("compress", "jpg start");
            //分别是图片路径，宽度高度，质量，和图片类型，重点在这里。
            byte[] bytes = BitmapUtil.compressBitmapToBytes(file.getPath(), 600, 0, 60, Bitmap.CompressFormat.JPEG);
            File jpg = new File(path, imgName + "compress.jpg");
            FileUtils.writeByteArrayToFile(jpg, bytes);
            Log.i("compress", "jpg finish");*/
            Log.i("compress", "----------------------------------------------------");
            Log.i("compress", "jpg size"+file.length());
            byte[] bytes1 = BitmapUtil.compressBitmapToBytes(file.getPath(), 600, 800, 80, Bitmap.CompressFormat.WEBP);
            String cachepath=TitanApplication.getContext().getExternalCacheDir().getPath();
            //File webp = new File(path, imgName + "compress.webp");
            File webp = new File(cachepath, imgName + "compress.webp");

            boolean isWrite=FileUtils.writeByteArrayToFile(webp, bytes1);
            if(isWrite){
                Log.i("compress", "webp size"+webp.length());



                return cachepath+File.separator+imgName+"compress.webp";
            }else {
                return null;
            }


        } catch (IOException e) {
            Log.i("compress_error", "compress_error"+e);
            return null;

            //e.printStackTrace();
        }
    }

    public static byte[] compressBitmapToBytes(String filePath, int reqWidth, int reqHeight, int quality, Bitmap.CompressFormat format) {
        Bitmap bitmap = getSmallBitmap(filePath, reqWidth, reqHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, quality, baos);
        byte[] bytes = baos.toByteArray();
        bitmap.recycle();
        Log.i(TAG, "Bitmap compressed success, size: " + bytes.length);
        return bytes;
    }

    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
//      options.inPreferQualityOverSpeed = true;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int h = options.outHeight;
        int w = options.outWidth;
        int inSampleSize = 0;
        if (h > reqHeight || w > reqWidth) {
            float ratioW = (float) w / reqWidth;
            float ratioH = (float) h / reqHeight;
            inSampleSize = (int) Math.min(ratioH, ratioW);
        }
        inSampleSize = Math.max(1, inSampleSize);
        return inSampleSize;
    }

}
