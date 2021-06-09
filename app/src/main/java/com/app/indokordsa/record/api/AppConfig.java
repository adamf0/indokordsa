package com.app.indokordsa.record.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AppConfig {
    public static Retrofit getRetrofit(int timeout) {
        if(timeout>0) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .build();

            return new Retrofit.Builder()
                    .baseUrl("http://indokordsa.media-phonix.co.id/")
//                    .baseUrl("http://192.168.43.210/indokordsa/")
                    .client(okHttpClient) //.client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        else{
            return new Retrofit.Builder()
                    .baseUrl("http://indokordsa.media-phonix.co.id/")
//                    .baseUrl("http://192.168.43.210/indokordsa/")
                    .addConverterFactory(ScalarsConverterFactory.create()) //.client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                    .build();
        }
    }
    public static Retrofit getRetrofitV2(int timeout) {
        if(timeout>0) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(timeout, TimeUnit.MINUTES)
                    .readTimeout(timeout, TimeUnit.MINUTES)
                    .writeTimeout(timeout, TimeUnit.MINUTES)
                    .build();

            return new Retrofit.Builder()
                    .baseUrl("http://indokordsa.media-phonix.co.id/")
//                    .baseUrl("http://192.168.43.210/indokordsa/")
                    .client(okHttpClient) //.client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        else{
            return new Retrofit.Builder()
                    .baseUrl("http://indokordsa.media-phonix.co.id/")
//                    .baseUrl("http://192.168.43.210/indokordsa/")
                    .addConverterFactory(ScalarsConverterFactory.create()) //.client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                    .build();
        }
    }
}