package com.titan.data.source.remote;

/**
 * Created by whs on 2017/7/10
 */

public class TitanSubscriber<T> extends ErrorSubscriber<T>{
    @Override
    public void onCompleted() {

    }


    @Override
    protected void onError(ExceptionEngine.ApiException ex) {

    }

    @Override
    public void onNext(T t) {

    }
}


