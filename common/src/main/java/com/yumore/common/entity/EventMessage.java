package com.yumore.common.entity;

/**
 * @author Nathaniel
 * @date 2018/5/29-11:49
 */
public class EventMessage {
    private String id;
    private Object object;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
