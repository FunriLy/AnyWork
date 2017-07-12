package com.qg.AnyWork.exception;

/**
 * Created by logan on 2017/7/11.
 */
public class ChapterException extends RuntimeException{
    public ChapterException(String message){
        super(message);
    }

    public ChapterException(String message, Throwable cause){
        super(message, cause);
    }
}