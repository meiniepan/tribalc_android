package com.gs.buluo.app.model;

import com.gs.buluo.app.bean.DetailReservation;
import com.gs.buluo.app.bean.RequestBodyBean.NewReserveRequest;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.ReserveResponse;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.app.network.ReserveApis;
import com.gs.buluo.app.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/29.
 */
public class ReserveModel {
    public void getReserveList(String category,String id, int limitSize,String nextSkip, Callback<ReserveResponse> callback) {
        TribeRetrofit.getInstance().createApi(ReserveApis.class).
                getReserviceList(limitSize,id,nextSkip).enqueue(callback);

    }

    public void getReserveListFirst(String category, String id,int limitSize, Callback<ReserveResponse> callback) {
        TribeRetrofit.getInstance().createApi(ReserveApis.class).
                getReserviceListFirst(id,limitSize).enqueue(callback);

    }

    public void getServeDetail(String id, String myId, Callback<BaseResponse<DetailReservation>> callback) {
        TribeRetrofit.getInstance().createApi(ReserveApis.class).
                getReserveDetail(id,myId).enqueue(callback);
    }

    public void cancelReserve(String id, String myId, ValueRequestBody body, Callback<BaseResponse> callback) {
        TribeRetrofit.getInstance().createApi(ReserveApis.class).
                cancelReserve(id, myId,body).enqueue(callback);
    }

    public void createReserve(String id,  NewReserveRequest body, Callback<BaseResponse<DetailReservation>> callback) {
        TribeRetrofit.getInstance().createApi(ReserveApis.class).
                createReserve(id,body).enqueue(callback);
    }
}
