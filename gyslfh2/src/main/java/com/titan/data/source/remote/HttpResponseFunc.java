package com.titan.data.source.remote;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by whs on 2017/7/10
 */

class HttpResponseFunc<T>  implements Func1<Throwable, Observable<T>> {
    @Override
    public Observable<T> call(Throwable throwable) {
        //ExceptionEngine为处理异常的驱动器
        return Observable.error(ExceptionEngine.handleException(throwable));
    }
}
