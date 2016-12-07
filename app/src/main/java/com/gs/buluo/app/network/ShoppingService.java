package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.CartItemUpdateResponse;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.RequestBodyBean.CartDeleteRequestBody;
import com.gs.buluo.app.bean.RequestBodyBean.ShoppingCartGoodsItem;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;
import com.gs.buluo.app.bean.ResponseBody.ShoppingCartResponse;
import com.gs.buluo.app.bean.ResponseBody.SimpleCodeResponse;
import com.gs.buluo.app.bean.ShoppingCart;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/24.
 */
public interface ShoppingService {

    @GET("orders?type=owner")
    Call<OrderResponse> getOrderFirst(@Query("me") String uid, @Query("limitSize") String limitSize);


    @GET("orders?type=owner")
    Call<OrderResponse>     getOrder(@Query("me") String uid, @Query("limitSize") String limitSize
            ,@Query("sortSkip")String sortSkip,@Query("status")OrderBean.OrderStatus status);


    @GET("shopping_cart")
    Call<ShoppingCartResponse> getShoppingCarList(@Query("me")String uid,@Query("sortSkip") String sortSkip);

    @GET("persons/{id}/shopping_cart")
    Call<ShoppingCartResponse> getShoppingCarListFirst(@Path("id") String uid);

    @HTTP(method = "DELETE", path = "persons/{id}/shopping_cart", hasBody = true)
    Call<SimpleCodeResponse> deleteCart(@Path("id")String uid, @Body CartDeleteRequestBody body);

    @PUT("persons/{id}/shopping_cart")
    Call<CartItemUpdateResponse> updateCartItem(@Path("id")String uid,@Body ShoppingCartGoodsItem body);
}
