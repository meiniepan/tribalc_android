package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.CartItem;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.RequestBodyBean.NewOrderBean;
import com.gs.buluo.app.bean.RequestBodyBean.NewOrderRequestBody;
import com.gs.buluo.app.bean.RequestBodyBean.ShoppingCartGoodsItem;
import com.gs.buluo.app.bean.RequestBodyBean.ValueBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;
import com.gs.buluo.app.bean.ResponseBody.ShoppingCartResponse;
import com.gs.buluo.common.network.BaseResponse;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hjn on 2016/11/24.
 */
public interface ShoppingApis {

    @GET("orders?type=owner")
    Observable<BaseResponse<OrderResponse>> getOrderFirst(@Query("me") String uid, @Query("limitSize") int limitSize,
                                                          @Query("status") String status);

    @GET("orders?type=owner")
    Observable<BaseResponse<OrderResponse>> getOrderList(@Query("me") String uid, @Query("limitSize") int limitSize
            , @Query("sortSkip") String sortSkip, @Query("status") String status);

    @GET("orders/{id}?type=owner")
    Observable<BaseResponse<OrderBean>> getOrder(@Path("id") String id, @Query("me") String uid);

    @PUT("orders/{orderId}/status?type=owner")
    Observable<BaseResponse<OrderBean>> updateOrderStatus(@Path("orderId") String orderId, @Query("me") String uid, @Body ValueBody status);

    @POST("orders?type=owner")
    Observable<BaseResponse<List<OrderBean>>> createNewOrder(@Query("me") String uid, @Body NewOrderRequestBody requestBody);

    @POST("orders?type=owner&direct=true")
    Observable<BaseResponse<List<OrderBean>>> createDirectNewOrder(@Query("me") String uid, @Body NewOrderRequestBody requestBody);

    @GET("shopping_cart")
    Observable<BaseResponse<ShoppingCartResponse>> getShoppingCarList(@Query("me") String uid, @Query("sortSkip") String sortSkip);

    @GET("persons/{id}/shopping_cart")
    Observable<BaseResponse<ShoppingCartResponse>> getShoppingCarListFirst(@Path("id") String uid, @Query("limitSize") int limitSize);

    @HTTP(method = "DELETE", path = "persons/{id}/shopping_cart/{ids}")
    Observable<BaseResponse<CodeResponse>> deleteCart(@Path("id") String uid, @Path("ids") String ids);

    @PUT("persons/{id}/shopping_cart")
    Observable<BaseResponse<CartItem>> updateCartItem(@Path("id") String uid, @Body ShoppingCartGoodsItem body);

    @POST("persons/{id}/shopping_cart")
    Observable<BaseResponse<CodeResponse>> addCartItem(@Path("id") String uid, @Body NewOrderBean body);

}
