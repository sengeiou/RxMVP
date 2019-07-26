package com.yumore.common.entity;

import java.util.List;

/**
 * @author Nathaniel
 */
public class ResultData<T> extends StatusEntity {
    private T data;
    private List<T> list;
    private String json;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

}
