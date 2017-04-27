package com.titan.model;

/**
 * Created by Administrator on 2016/10/10.
 */

public class Image {

    private String FJ_NAME;
    private String REMARK;
    private String FJ_URL;

    public Image()
    {

    }

    public Image(String FJ_NAME, String REMARK, String FJ_URL) {
        this.FJ_NAME = FJ_NAME;
        this.FJ_URL = FJ_URL;
        this.REMARK = REMARK;
    }

    public Image(String FJ_URL) {
        this.FJ_URL = FJ_URL;
    }

    public Image(String FJ_URL, String REMARK) {
        this.FJ_URL = FJ_URL;
        this.REMARK = REMARK;
    }

    public String getREMARK() {
        return REMARK;
    }

    public String getFJ_NAME() {
        return FJ_NAME;
    }

    public String getFJ_URL() {
        return FJ_URL;
    }

    public void setFJ_NAME(String FJ_NAME) {
        this.FJ_NAME = FJ_NAME;
    }

    public void setFJ_URL(String FJ_URL) {
        this.FJ_URL = FJ_URL;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }
}
