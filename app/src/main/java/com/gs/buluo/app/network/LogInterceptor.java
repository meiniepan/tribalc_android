package com.gs.buluo.app.network;

import android.util.Log;

import com.gs.buluo.app.bean.ResponseBody.BaseResponse;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by hjn on 2017/3/2.
 */
public class LogInterceptor implements okhttp3.Interceptor {
    private String TAG ="TribeNetwork";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long start =System.currentTimeMillis();
        Log.d(TAG, "request url:" + request.url() +" request body is "+(request.body()!=null? request.body().toString() :null));
        Response response = chain.proceed(request);
        long end =System.currentTimeMillis();
        Log.d(TAG, "response from url:" + response.request().url() +" ----------in  " +(end-start)+
                "ms \n response code is  "+response.code()+"  and response message is "+response.message());

        return response;
    }
}
