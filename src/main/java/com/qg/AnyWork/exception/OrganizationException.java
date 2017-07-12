package com.qg.AnyWork.exception;

/**
 * Created by logan on 2017/7/11.
 */
public class OrganizationException extends RuntimeException{
    public OrganizationException(String message){
        super(message);
    }

    public OrganizationException(String message, Throwable cause){
        super(message, cause);
    }
}
