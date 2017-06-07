package com.titan.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by whs on 2017/5/23
 */

public class FileUtils {

    /**
     * 根据文件路径获取文件名(待后缀名)
     * @param path
     * @return
     */
    public  static String pathGetFileName(String path){
        return path.substring(path.lastIndexOf(File.separator)+1);
    }

    /**
     * 根据文件路径获取文件名
     * @param path
     * @return
     */
    public  static String pathGetNoSuffixFileName(String path){
        return path.substring(path.lastIndexOf(File.separator)+1,path.lastIndexOf(".")-1);
    }


    /**
     * 根据文件路径获取文件名后缀名
     * @param path
     * @return
     */
    public static  String pathGetSuffix(String path){
        return path.substring(path.lastIndexOf(".")+1);

    }

    /**
     *
     * 写文件
     * @param file
     * @param bytes
     * @return
     * @throws IOException
     */
    public static boolean writeByteArrayToFile(File file, byte[] bytes) throws IOException {

        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
            outputStream.flush();
            return true;
        } finally {

            if (outputStream != null) {
                outputStream.close();
            }
        }

    }
}
