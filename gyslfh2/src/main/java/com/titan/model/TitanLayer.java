package com.titan.model;

/**
 * Created by whs on 2017/7/3
 * 图层类
 */

public class TitanLayer {
    //图层名称
    private String name;
    //图层地址
    private String url;
    //图层索引
    private int index;
    //可见性
    private boolean visiable;
    //图层状态
    private int status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isVisiable() {
        return visiable;
    }

    public void setVisiable(boolean visiable) {
        this.visiable = visiable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
