package com.gs.buluo.app.model;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.bean.ResponseBody.GoodsResponseBean;
import com.gs.buluo.app.bean.ResponseBody.ServeDetailResponse;
import com.gs.buluo.app.bean.ResponseBody.ServeResponse;
import com.gs.buluo.app.network.ServeService;
import com.gs.buluo.app.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/29.
 */
public class ServeModel {

    public void getServeListFirst(String category, int limitSize,String sort, Callback<ServeResponse> callback) {
        TribeRetrofit.getIntance().createApi(ServeService.class).
                getServiceListFirst(limitSize, sort).enqueue(callback);
    }

    public void getServeList(String category, int limitSize, String sort, String sortSkip, Callback<ServeResponse> callback) {
        TribeRetrofit.getIntance().createApi(ServeService.class).
                getServiceList(limitSize, sortSkip,sort).enqueue(callback);
    }

    public void getServeDetail(String id, Callback<ServeDetailResponse> callback) {
        TribeRetrofit.getIntance().createApi(ServeService.class).
                getServeDetail(id).enqueue(callback);
    }

}
