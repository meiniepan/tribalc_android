package com.gs.buluo.app.model;

import com.gs.buluo.app.bean.DetailStoreSetMeal;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
import com.gs.buluo.app.bean.ResponseBody.ServeResponse;
import com.gs.buluo.app.network.ServeService;
import com.gs.buluo.app.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/29.
 */
public class ServeModel {

    public void getServeListFirst(String category, int limitSize,String sort,String coordinate, Callback<ServeResponse> callback) {
        TribeRetrofit.getInstance().createApi(ServeService.class).
                getServiceListFirst(category,limitSize, sort,coordinate).enqueue(callback);
    }

    public void getServeList(String category, int limitSize, String sort, String sortSkip,String coordinate, Callback<ServeResponse> callback) {
        TribeRetrofit.getInstance().createApi(ServeService.class).
                getServiceList(category,limitSize, sortSkip,sort,coordinate).enqueue(callback);
    }

    public void getServeDetail(String id, Callback<BaseResponse<DetailStoreSetMeal>> callback) {
        TribeRetrofit.getInstance().createApi(ServeService.class).
                getServeDetail(id).enqueue(callback);
    }

}
