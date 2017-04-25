package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.DetailReservation;
import com.gs.buluo.app.bean.RequestBodyBean.NewReserveRequest;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.ReserveResponseBody;
import com.gs.buluo.common.network.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hjn on 2016/11/11.
 */
public interface ReserveApis {
    @GET("reservations?type=owner")
    Observable<BaseResponse<ReserveResponseBody>> getReserveList(
//            @Query("status") String status,
            @Query("me") String myId ,
            @Query("limitSize") int limitSize,
            @Query("sortSkip") String sortSkip);

    @GET("reservations?type=owner")
    Observable<BaseResponse<ReserveResponseBody>> getReserveListFirst(
//            @Query("status") String status,
            @Query("me") String myId ,
            @Query("limitSize") int limitSize);


    @GET("reservations/{id}?type=owner")
    Observable<BaseResponse<DetailReservation>> getReserveDetail(@Path("id") String reserveId, @Query("me") String myId);

    @PUT("reservations/{id}/status?type=owner")
    Observable<BaseResponse<CodeResponse>> cancelReserve(@Path("id")String id, @Query("me") String myId, @Body ValueRequestBody body);

    @POST("reservations")
    Observable<BaseResponse<DetailReservation>> createReserve(@Query("me")String myId, @Body NewReserveRequest body);
}

