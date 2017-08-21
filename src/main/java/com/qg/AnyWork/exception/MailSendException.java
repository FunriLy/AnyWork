package com.qg.AnyWork.exception;

/**
 * Created by FunriLy on 2017/8/20.
 * From small beginnings comes great things.
 */
public class MailSendException extends RuntimeException {

    public MailSendException(String message) {
        super(message);
    }

    public MailSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
