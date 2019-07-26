package com.yumore.common.entity;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * ErrorEntity
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/8 - 15:12
 */
public class SpecialEntity<T> {
    /**
     * 框架的字段值
     */
    private int code;
    private String message;
    /**
     * 将date转化为data
     * 然后解析
     */
    private T date;
    /**
     * 新加的字段值
     * error_code
     * 将其序列化为errorCode
     */
    @SerializedName("error_code")
    private int errorCode;
    private String reason;
    private List<T> list;
    private int cpage;
    private int pgnum;
    private int rsnum;
    private String result;

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
        return date;
    }

    public void setData(T data) {
        this.date = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getCpage() {
        return cpage;
    }

    public void setCpage(int cpage) {
        this.cpage = cpage;
    }

    public int getPgnum() {
        return pgnum;
    }

    public void setPgnum(int pgnum) {
        this.pgnum = pgnum;
    }

    public int getRsnum() {
        return rsnum;
    }

    public void setRsnum(int rsnum) {
        this.rsnum = rsnum;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
