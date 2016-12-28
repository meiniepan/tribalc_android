package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/12/28.
 */

public abstract class TribeCallback<T extends IBaseResponse> implements Callback<BaseCodeResponse<T>> {
    @Override
    public void onResponse(Call<BaseCodeResponse<T>> call, Response<BaseCodeResponse<T>> response) {
        if (response == null) {
            onFail(500);
            return;
        }
        BaseCodeResponse responseBody = response.body();
        if (responseBody == null) {
            onFail(500);
        } else if (responseBody.code >= 400) {
            onFail(responseBody.code);
        } else {
            onSuccess(response);
        }
    }

    @Override
    public void onFailure(Call<BaseCodeResponse<T>> call, Throwable t) {
        onFail(500);
    }


    public abstract void onSuccess(Response<BaseCodeResponse<T>> response);

    public abstract void onFail(int responseCode);

}
