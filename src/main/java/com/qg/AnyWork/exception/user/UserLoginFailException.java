package com.qg.AnyWork.exception.user;

/**
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
public class UserLoginFailException extends RuntimeException {

    public UserLoginFailException(String message){
        super(message);
    }

    public UserLoginFailException(String message, Throwable cause){
        super(message, cause);
    }
}
