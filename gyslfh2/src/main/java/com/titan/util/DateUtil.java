package com.titan.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Whs on 2016/12/13 0013
 */
public class DateUtil {
    public static  String  dateFormat(Date date){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    /*public static  String  dateFormat(Date date){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }*/

    /**
     * 获取年月日
     * @return
     */
    public static String  getYMD(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return dateFormat.format(new Date());

    }
    /**
     * 获取年月日
     * @return
     */
    public static String  getYMD(Date date){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return dateFormat.format(new Date());

    }

    /**
     * 获取小时
     * @return
     */
    public static String  getHour(Date date){
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH", Locale.getDefault());
        return dateFormat.format(date);

    }
    /**
     * 获取小时
     * @return
     */
    public static String  getHour(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH", Locale.getDefault());
        return dateFormat.format(new Date());

    }
}
