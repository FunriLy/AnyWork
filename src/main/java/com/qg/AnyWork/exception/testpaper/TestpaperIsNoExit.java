package com.qg.AnyWork.exception.testpaper;

/**
 * Created by FunriLy on 2017/8/18.
 * From small beginnings comes great things.
 */
public class TestpaperIsNoExit extends RuntimeException {

    public TestpaperIsNoExit(String message) {
        super(message);
    }

    public TestpaperIsNoExit(String message, Throwable cause) {
        super(message, cause);
    }
}
