package com.titan.data.source.remote;

import rx.Observer;

/**
 * Created by whs on 2017/7/10
 */

public abstract class ErrorSubscriber<T> implements Observer<T> {
    @Override
    public void onError(Throwable e) {
        if(e instanceof ExceptionEngine.ApiException){
            onError((ExceptionEngine.ApiException)e);
        }else{
            onError(new ExceptionEngine.ApiException(e,123));
        }
    }

    /**
     * 错误回调
     */
    protected abstract void onError(ExceptionEngine.ApiException ex);
}
