package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.RequestBodyBean.ValueBody;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;
import com.gs.buluo.common.network.BaseResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hjn on 2016/11/24.
 */
public interface ShoppingApis {

    @GET("orders?type=owner")
    Observable<BaseResponse<OrderResponse>>  getOrderFirst(@Query("me") String uid, @Query("limitSize") int limitSize,
                                            @Query("status")String status);

    @GET("orders?type=owner")
    Observable<BaseResponse<OrderResponse>> getOrderList(@Query("me") String uid, @Query("limitSize") int limitSize
            , @Query("sortSkip")String sortSkip, @Query("status")String status);

    @GET("orders/{id}")
    Observable<BaseResponse<OrderBean>> getOrder(@Path("id")String id,@Query("me") String uid);

    @PUT("orders/{orderId}/status?type=owner")
    Observable<BaseResponse<OrderBean>> updateOrderStatus(@Path("orderId")String orderId, @Query("me")String uid, @Body ValueBody status);
}
