package com.titan.model;

/**
 * Created by Administrator on 2016/10/10
 */

public class Image {

    private String name;
    private String path;
    private String data;

    public Image()
    {

    }


    public Image(String name, String path, String data) {
        this.name = name;
        this.path = path;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
