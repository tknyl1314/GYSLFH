package com.titan.push;

/**
 * Created by whs on 2017/6/30
 * 推送消息模板
 */

public class PushMsg<T> {

    //类型 1：火情消息 2：任务通知 3：其他
    private int TYPE;
    //消息内容
    private T CONTENT;

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public T getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(T CONTENT) {
        this.CONTENT = CONTENT;
    }
}
