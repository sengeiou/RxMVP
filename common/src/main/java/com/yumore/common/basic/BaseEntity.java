package com.yumore.common.basic;


/**
 * BaseEntity
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/8 - 15:12
 */
public class BaseEntity<T> {

    private int code;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
