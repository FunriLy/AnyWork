package com.qg.AnyWork.exception.user;

/**
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
public class EmptyUserException extends RuntimeException {

    public EmptyUserException(String message){
        super(message);
    }

    public EmptyUserException(String message, Throwable cause){
        super(message, cause);
    }
}
