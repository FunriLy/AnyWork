package com.qg.AnyWork.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转换器
 * Created by FunriLy on 2017/8/19.
 * From small beginnings comes great things.
 */
public class DateUtil {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date longToDate(long longTime) {
        return new Date(longTime);
    }
}
