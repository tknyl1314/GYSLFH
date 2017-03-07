package com.otitan.service;

import android.content.Context;

import com.otitan.gyslfh.R;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by whs on 2017/2/17
 * Retrofit 初始化
 */


public class RetrofitHelper {
    private Context mCntext;

    //OkHttpClient client = new OkHttpClient();
    //private static NetworkMonitor networkMonitor;
    //GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());
    private static RetrofitHelper instance = null;
    private Retrofit mRetrofit = null;
    public static RetrofitHelper getInstance(Context context){
        if (instance == null){
            instance = new RetrofitHelper(context);
            //networkMonitor=new LiveNetworkMonitor(context);

        }
        return instance;
    }
    private RetrofitHelper(Context mContext){
        mCntext = mContext;
        init();
    }

    private void init() {
        resetApp();
    }

    private void resetApp() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        // 看这里 ！！！ 我们添加了一个网络监听拦截器
        /*okHttpClientBuilder.addInterceptor(chain -> {
            boolean connected = networkMonitor.isConnected();
            if (networkMonitor.isConnected()) {
                return chain.proceed(chain.request());
            } else {
                throw new NoNetworkException();
            }
        });*/
        //okHttpClientBuilder.addNetworkInterceptor(new MyNetworkInterceptor());

        mRetrofit = new Retrofit.Builder()
                .baseUrl(mCntext.getResources().getString(R.string.serverhost))
                .client(okHttpClientBuilder.build())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
    public RetrofitService getServer(){
        return mRetrofit.create(RetrofitService.class);
    }
    /*private class MyNetworkInterceptor implements Interceptor

    {

        @Override
        public Response intercept(Chain chain) throws IOException {
            boolean connected = networkMonitor.isConnected();
            if (networkMonitor.isConnected()) {
                return chain.proceed(chain.request());
            } else {
                //throw new NoNetworkException();
                ToastUtil.setToast((Activity) mCntext,"无网络连接，请检查网络");

            }
            return null;
        }
    }*/
}
