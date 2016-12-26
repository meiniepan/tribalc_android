package com.gs.buluo.app.model;

import com.gs.buluo.app.bean.DetailReservation;
import com.gs.buluo.app.bean.RequestBodyBean.NewReserveRequest;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.ReserveResponse;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.network.ReserveService;
import com.gs.buluo.app.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/29.
 */
public class ReserveModel {
    public void getReserveList(String category,String id, int limitSize,String nextSkip, Callback<ReserveResponse> callback) {
        TribeRetrofit.getInstance().createApi(ReserveService.class).
                getReserviceList(limitSize,id,nextSkip).enqueue(callback);

    }

    public void getReserveListFirst(String category, String id,int limitSize, Callback<ReserveResponse> callback) {
        TribeRetrofit.getInstance().createApi(ReserveService.class).
                getReserviceListFirst(id,limitSize).enqueue(callback);

    }

    public void getServeDetail(String id, String myId, Callback<BaseCodeResponse<DetailReservation>> callback) {
        TribeRetrofit.getInstance().createApi(ReserveService.class).
                getReserveDetail(id,myId).enqueue(callback);
    }

    public void cancelReserve(String id, String myId, ValueRequestBody body, Callback<BaseCodeResponse> callback) {
        TribeRetrofit.getInstance().createApi(ReserveService.class).
                cancelReserve(id, myId,body).enqueue(callback);
    }

    public void createReserve(String id,  NewReserveRequest body, Callback<BaseCodeResponse<DetailReservation>> callback) {
        TribeRetrofit.getInstance().createApi(ReserveService.class).
                createReserve(id,body).enqueue(callback);
    }
}
