package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/24.
 */
public interface OrderService {

    @GET("orders?type=owner")
    Call<OrderResponse> getOrderFirst(@Query("me") String uid, @Query("limitSize") String limitSize);

    @GET("orders?type=owner")
    Call<OrderResponse>     getOrder(@Query("me") String uid, @Query("limitSize") String limitSize
            ,@Query("sortSkip")String sortSkip,@Query("status")OrderBean.OrderStatus status);
}
