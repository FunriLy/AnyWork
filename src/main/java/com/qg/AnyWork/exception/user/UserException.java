package com.qg.AnyWork.exception.user;

/**
 * Created by FunriLy on 2017/7/14.
 * From small beginnings comes great things.
 */
public class UserException extends RuntimeException {

    public UserException(String message){
        super(message);
    }

    public UserException(String message, Throwable cause){
        super(message, cause);
    }
}
