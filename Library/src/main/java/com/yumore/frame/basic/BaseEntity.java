package com.yumore.frame.basic;


/**
 * BaseEntity
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/8 - 15:12
 */
public class BaseEntity<T> {

    private int StatusCode;
    private String Info;
    private T Data;
    private int Total;

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }
}
