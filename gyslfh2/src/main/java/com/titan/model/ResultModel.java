package com.titan.model;

import java.io.Serializable;

/**
 * Created by whs on 2017/4/26
 * 服务端返回结果
 */

public class ResultModel<T> implements Serializable {

    /**
     * result : 1:成功 0:失败
     * data : {"dqid":"1472","dqName":"中国","role":"超级管理员","accountStatus":"1","clientID":"","userID":"1","dqLevel":"1"}
     */
    //返回结果标识
    private String result;
    //返回数据
    private T data;
    //返回错误信息
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
    }
}
