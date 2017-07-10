package com.qg.AnyWork.exception.user;

/**
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
public class FormatterFaultException extends RuntimeException {

    public FormatterFaultException(String message){
        super(message);
    }

    public FormatterFaultException(String message, Throwable cause){
        super(message, cause);
    }
}
