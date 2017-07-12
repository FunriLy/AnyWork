package com.qg.AnyWork.exception.user;

/**
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
public class UserNotLoginException extends RuntimeException {

    public UserNotLoginException(String message){
        super(message);
    }

    public UserNotLoginException(String message, Throwable cause){
        super(message, cause);
    }
}
