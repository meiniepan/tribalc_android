package com.gs.buluo.app.network;

import com.gs.buluo.app.Constant;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 2016/11/1.
 */
public class TribeRetrofit {

    private static TribeRetrofit instance;
    private Map<Class, Object> apis = new HashMap<>();
    private final Retrofit retrofit;

    private TribeRetrofit() {
        OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();
        builder.interceptors().add(new HttpInterceptor());
        builder.interceptors().add(new LogInterceptor());
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.Base.BASE_URL)
                .client(builder.build())
                .addConverterFactory(CustomGsonFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public synchronized static TribeRetrofit getInstance() {
        if (null == instance) {
            instance = new TribeRetrofit();
        }
        return instance;
    }

    public <T> T createApi(Class<T> service) {
        if (!apis.containsKey(service)) {
            T instance = retrofit.create(service);
            apis.put(service, instance);
        }

        return (T) apis.get(service);
    }

}
