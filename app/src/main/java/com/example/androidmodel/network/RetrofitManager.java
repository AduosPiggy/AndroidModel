package com.example.androidmodel.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author kfflso
 * @data 2025-05-06 16:41
 * @plus:
 */
public class RetrofitManager {
    private final String BASE_URL = "https://zzz.com//api/v1/";

    public static boolean isDebug = false;
    private static RetrofitServices apiServices;
    private static RetrofitManager retrofitManager;

    public static RetrofitServices apiServices(){
        if(apiServices == null) {
            retrofitManager = new RetrofitManager();
        }
        return apiServices;
    }

    public RetrofitManager() {
        OkHttpClient.Builder builder =new OkHttpClient.Builder();
        if(isDebug){
            //使用自定义的Log拦截器
        }
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10,TimeUnit.SECONDS);
        builder.writeTimeout(10,TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        apiServices = retrofit.create(RetrofitServices.class);
    }
}
