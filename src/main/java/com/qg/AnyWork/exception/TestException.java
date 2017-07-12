package com.qg.AnyWork.exception;

/**
 * Created by logan on 2017/7/11.
 */
public class TestException extends RuntimeException{
    public TestException(String message){
        super(message);
    }

    public TestException(String message, Throwable cause){
        super(message, cause);
    }
}
