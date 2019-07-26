package com.yumore.common.entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * BaseEntity
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/8 - 15:12
 */
public class ListEntity<T> {
    @Expose
    @SerializedName("error_code")
    private int errorCode;
    private String reason;
    private List<T> data;
    private int cpage;
    private int pgnum;
    private int rsnum;
    private String result;
    private int ClassCount;
    private int MaxClassCount;
    private String shareDes;
    private String onsize;
    private String sitesize;

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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
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

    public int getClassCount() {
        return ClassCount;
    }

    public void setClassCount(int classCount) {
        ClassCount = classCount;
    }

    public int getMaxClassCount() {
        return MaxClassCount;
    }

    public void setMaxClassCount(int maxClassCount) {
        MaxClassCount = maxClassCount;
    }

    public String getShareDes() {
        return shareDes;
    }

    public void setShareDes(String shareDes) {
        this.shareDes = shareDes;
    }

    public String getOnsize() {
        return onsize;
    }

    public void setOnsize(String onsize) {
        this.onsize = onsize;
    }

    public String getSitesize() {
        return sitesize;
    }

    public void setSitesize(String sitesize) {
        this.sitesize = sitesize;
    }
}
