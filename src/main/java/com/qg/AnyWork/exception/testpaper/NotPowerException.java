package com.qg.AnyWork.exception.testpaper;

/**
 * Created by FunriLy on 2017/8/18.
 * From small beginnings comes great things.
 */
public class NotPowerException extends RuntimeException {

    public NotPowerException(String message){
        super(message);
    }

    public NotPowerException(String message, Throwable cause){
        super(message, cause);
    }
}
