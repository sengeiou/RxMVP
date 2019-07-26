package com.yumore.common.entity;

public class Option {
    private long id;
    private int resourceId;
    private String describe;
    private String imagePath;
    private boolean visible;
    private boolean localEnable;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isLocalEnable() {
        return localEnable;
    }

    public void setLocalEnable(boolean localEnable) {
        this.localEnable = localEnable;
    }
}
