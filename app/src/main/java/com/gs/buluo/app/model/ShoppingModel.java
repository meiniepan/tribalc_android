package com.gs.buluo.app.model;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.CartItemUpdateResponse;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.RequestBodyBean.NewOrderBean;
import com.gs.buluo.app.bean.RequestBodyBean.NewOrderRequestBody;
import com.gs.buluo.app.bean.RequestBodyBean.ShoppingCartGoodsItem;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.NewOrderResponse;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;
import com.gs.buluo.app.bean.ResponseBody.ShoppingCartResponse;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.app.network.ShoppingApis;
import com.gs.buluo.app.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/14.
 */
public class ShoppingModel {

    public void getOrder(String uid,String status, String limitSize, String sortSkip, Callback<OrderResponse> callback){
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                getOrderList(uid,limitSize,sortSkip,status).enqueue(callback);
    }

    public void updateOrder(String uid, ValueRequestBody status, String orderId, Callback<BaseResponse<OrderBean>> callback){
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                updateOrderStatus(orderId,uid,status).enqueue(callback);
    }

    public void getOrderFirst(String uid, String status, String limitSize, Callback<OrderResponse> callback){
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                getOrderFirst(uid,limitSize,status).enqueue(callback);
    }

    public void getShoppingListMore(String uid,String sortSkip, Callback<ShoppingCartResponse> callback){
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                getShoppingCarList(uid,sortSkip).enqueue(callback);
    }

    public void getShoppingListFirst(String uid,Callback<ShoppingCartResponse> callback){
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                getShoppingCarListFirst(uid,50).enqueue(callback);
    }

    public void deleteShoppingItem(String body, Callback<BaseResponse> callback){
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                deleteCart(TribeApplication.getInstance().getUserInfo().getId(),body).enqueue(callback);
    }

    public void updateShoppingItem(ShoppingCartGoodsItem body, Callback<CartItemUpdateResponse> callback){
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                updateCartItem(TribeApplication.getInstance().getUserInfo().getId(),body).enqueue(callback);
    }

    public void addShoppingCart(NewOrderBean body, Callback<BaseResponse> callback){
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                addCartItem(TribeApplication.getInstance().getUserInfo().getId(),body).enqueue(callback);
    }

    public void createNewOrder(NewOrderRequestBody body, Callback<NewOrderResponse> callback){
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                createNewOrder(TribeApplication.getInstance().getUserInfo().getId(),body).enqueue(callback);
    }
}
