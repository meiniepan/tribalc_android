package com.gs.buluo.app.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hjn on 2016/11/10.
 */
public class HttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Request request= builder.addHeader("Accept", "application/json").addHeader("Content-Type", "application/json").build();
        return chain.proceed(request);
    }
}
