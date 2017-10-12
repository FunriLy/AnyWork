package com.qg.AnyWork.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * Created by FunriLy on 2017/9/25.
 * From small beginnings comes great things.
 */
public class CommonDateUtil {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取当前时间的时间戳
     * @return 时间戳
     */
    public static Long getStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前时间的Date对象
     * @return 当前时间的Date对象
     */
    public static Date getNowDate(){
        return new Date(System.currentTimeMillis());
    }

    /**
     * 获取当前时间的SimpleDateFormat对象
     * @return 当前时间的SimpleDateFormat对象
     */
    public static String getNowFormat(){
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 将毫秒数转化为 Date
     * @param longtime
     * @return
     */
    public static Date changeLongtimeToDate(long longtime){
        return new Date(longtime);
    }
}
