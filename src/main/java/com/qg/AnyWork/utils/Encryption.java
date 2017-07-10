package com.qg.AnyWork.utils;

import org.springframework.util.DigestUtils;

/**
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
public class Encryption {

    //md5盐值字符串
    private  static final String slat = "livid";

    public static String  getMD5 (String string) {
        String base = string+"/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return  md5;
    }
}
