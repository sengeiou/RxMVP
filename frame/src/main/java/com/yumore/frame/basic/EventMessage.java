package com.yumore.frame.basic;

/**
 * @author Nathaniel
 * @date 2018/5/29-11:49
 */
public class EventMessage<T> {
    private String id;
    private T data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
