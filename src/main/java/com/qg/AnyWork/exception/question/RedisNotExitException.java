package com.qg.AnyWork.exception.question;


/**
 * Created by FunriLy on 2017/7/13.
 * From small beginnings comes great things.
 */
public class RedisNotExitException extends RuntimeException {

    public RedisNotExitException(String message){
        super(message);
    }

    public RedisNotExitException(String message, Throwable cause){
        super(message, cause);
    }
}
