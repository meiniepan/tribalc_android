package com.gs.buluo.app.network;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.utils.CommonUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hjn on 2016/11/10.
 */
public class HttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request req = chain.request();
        Request.Builder builder = req.newBuilder();
        HttpUrl url = req.url();
        if (TribeApplication.getInstance().getUserInfo()!=null&&TribeApplication.getInstance().getUserInfo().getToken()!=null){
            builder.addHeader("Authorization",TribeApplication.getInstance().getUserInfo().getToken());
            if (url.encodedPath().contains("wallets")) {
                HttpUrl.Builder newBuilder = url.newBuilder();
                newBuilder.addQueryParameter("me", TribeApplication.getInstance().getUserInfo().getId());
                url = newBuilder.build();
            }
        }
        Request request= builder.addHeader("Accept", "application/json").url(url).addHeader("Content-Type", "application/json").
                addHeader("User-Agent", CommonUtils.getDeviceInfo(TribeApplication.getInstance().getApplicationContext())).build();
        return chain.proceed(request);
    }
}
