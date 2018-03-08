package com.yumore.frame.library.basic;


public class BaseException extends RuntimeException {

    private int code;


    public BaseException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public BaseException(String message) {
        super(new Throwable(message));
    }
}