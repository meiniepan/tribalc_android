package com.gs.buluo.app.network;

import android.util.Log;

import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/12/28.
 */

public abstract class TribeCallback<T extends IBaseResponse> implements Callback<BaseResponse<T>> {
    private static final String TAG = "TribeCallback";

    @Override
    public void onResponse(Call<BaseResponse<T>> call, Response<BaseResponse<T>> response) {
        if (response == null) {
            onFail(500, null);
            return;
        }
        BaseResponse responseBody = response.body();
        if (responseBody == null) {
            onFail(500, response.body());
        } else if (responseBody.code >= 400) {
            Log.e(TAG, "onResponse: "+responseBody.code +"   body： url is "+response.raw().request().url());
            onFail(responseBody.code,response.body());
        } else {
            onSuccess(response);
        }
    }

    @Override
    public void onFailure(Call<BaseResponse<T>> call, Throwable t) {
        onFail(500, null);
    }


    public abstract void onSuccess(Response<BaseResponse<T>> response);

    public abstract void onFail(int responseCode, BaseResponse<T> body);

}
