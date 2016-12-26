package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.DetailReservation;
import com.gs.buluo.app.bean.RequestBodyBean.NewReserveRequest;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.ReserveResponse;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/11.
 */
public interface ReserveService {
    @GET("reservations?type=owner")
    Call<ReserveResponse> getReserviceList(
//            @Query("status") String status,
            @Query("limitSize") int limitSize,
            @Query("me") String myId ,
            @Query("sortSkip") String sortSkip);

    @GET("reservations?type=owner")
    Call<ReserveResponse> getReserviceListFirst(
//            @Query("status") String status,
            @Query("me") String myId ,
            @Query("limitSize") int limitSize);


    @GET("reservations/{id}?type=owner")
    Call<BaseCodeResponse<DetailReservation>> getReserveDetail(@Path("id") String reserveId, @Query("me") String myId);

    @PUT("reservations/{id}/status?type=owner")
    Call<BaseCodeResponse> cancelReserve(@Path("id")String id, @Query("me") String myId, @Body ValueRequestBody body);

    @POST("reservations")
    Call<BaseCodeResponse<DetailReservation>> createReserve(@Query("me")String myId, @Body NewReserveRequest body);
}

