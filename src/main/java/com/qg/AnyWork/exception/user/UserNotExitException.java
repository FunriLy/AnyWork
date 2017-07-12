package com.qg.AnyWork.exception.user;

/**
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
public class UserNotExitException extends RuntimeException {

    public UserNotExitException(String message){
        super(message);
    }

    public UserNotExitException(String message, Throwable cause){
        super(message, cause);
    }
}
