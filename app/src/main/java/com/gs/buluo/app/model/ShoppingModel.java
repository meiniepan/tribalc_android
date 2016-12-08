package com.gs.buluo.app.model;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.CartItemUpdateResponse;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.RequestBodyBean.CartDeleteRequestBody;
import com.gs.buluo.app.bean.RequestBodyBean.ShoppingCartGoodsItem;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;
import com.gs.buluo.app.bean.ResponseBody.ShoppingCartResponse;
import com.gs.buluo.app.bean.ResponseBody.SimpleCodeResponse;
import com.gs.buluo.app.network.ShoppingService;
import com.gs.buluo.app.network.TribeRetrofit;

import java.util.List;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/14.
 */
public class ShoppingModel {

    public void getOrder(String uid,OrderBean.OrderStatus status, String limitSize, String sortSkip, Callback<OrderResponse> callback){
        TribeRetrofit.getIntance().createApi(ShoppingService.class).
                getOrder(uid,limitSize,sortSkip,status).enqueue(callback);
    }

    public void getOrderFirst(String uid, OrderBean.OrderStatus status, String limitSize, Callback<OrderResponse> callback){
        TribeRetrofit.getIntance().createApi(ShoppingService.class).
                getOrderFirst(uid,limitSize).enqueue(callback);
    }

    public void getShoppingListMore(String uid,String sortSkip, Callback<ShoppingCartResponse> callback){
        TribeRetrofit.getIntance().createApi(ShoppingService.class).
                getShoppingCarList(uid,sortSkip).enqueue(callback);
    }

    public void getShoppingListFirst(String uid,Callback<ShoppingCartResponse> callback){
        TribeRetrofit.getIntance().createApi(ShoppingService.class).
                getShoppingCarListFirst(uid).enqueue(callback);
    }

    public void deleteShoppingItem(List<CartDeleteRequestBody> body, Callback<SimpleCodeResponse> callback){
        TribeRetrofit.getIntance().createApi(ShoppingService.class).
                deleteCart(TribeApplication.getInstance().getUserInfo().getId(),body).enqueue(callback);
    }

    public void updateShoppingItem(ShoppingCartGoodsItem body, Callback<CartItemUpdateResponse> callback){
        TribeRetrofit.getIntance().createApi(ShoppingService.class).
                updateCartItem(TribeApplication.getInstance().getUserInfo().getId(),body).enqueue(callback);
    }

    public void addShoppingCart(ShoppingCartGoodsItem body,Callback<SimpleCodeResponse> callback){
        TribeRetrofit.getIntance().createApi(ShoppingService.class).
                addCartItem(TribeApplication.getInstance().getUserInfo().getId(),body).enqueue(callback);
    }

}
