package com.qg.AnyWork.exception;

/**
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
public class ValcodeWrongException extends RuntimeException {

    public ValcodeWrongException(String message){
        super(message);
    }

    public ValcodeWrongException(String message, Throwable cause) {
        super(message, cause);
    }
}
