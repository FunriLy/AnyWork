package com.qg.AnyWork.exception.question;

/**
 * Created by FunriLy on 2017/7/13.
 * From small beginnings comes great things.
 */
public class ExcelReadException extends RuntimeException {

    public ExcelReadException(String message){
        super(message);
    }

    public ExcelReadException(String message, Throwable cause){
        super(message, cause);
    }
}
