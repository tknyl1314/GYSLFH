package com.titan.data.source.remote;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.titan.newslfh.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by whs on 2017/2/17
 * Retrofit 初始化
 */


public class RetrofitHelper {
    private Context mCntext;

    //OkHttpClient client = new OkHttpClient();
    //private static NetworkMonitor networkMonitor;
    GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());
    private static RetrofitHelper instance = null;
    private Retrofit mRetrofit = null;
    private Retrofit mWeatherRetrofit=null;
    public static RetrofitHelper getInstance(Context context){
        if (instance == null){
            instance = new RetrofitHelper(context);

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
        //okHttpClientBuilder.addNetworkInterceptor(new MyNetworkInterceptor());
        okHttpClientBuilder.connectTimeout(5, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(mCntext.getResources().getString(R.string.serverhost))
                .client(okHttpClientBuilder.build())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        //获取气象数据
        mWeatherRetrofit=new Retrofit.Builder()
                .baseUrl("http://222.85.160.4/")
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
    public RetrofitService getServer(){
        return mRetrofit.create(RetrofitService.class);
    }

    public MeteorologyService getWeatherServer(){
        return mWeatherRetrofit.create(MeteorologyService.class);
    }
   /* private class MyNetworkInterceptor implements Interceptor
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
