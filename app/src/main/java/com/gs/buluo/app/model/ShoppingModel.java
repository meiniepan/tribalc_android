package com.gs.buluo.app.model;

import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;
import com.gs.buluo.app.bean.ResponseBody.ShoppingCartResponse;
import com.gs.buluo.app.network.ShoppingService;
import com.gs.buluo.app.network.TribeRetrofit;

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

//    public void addOrder(String uid, UserOrderEntity entity, Callback<UserOrderResponse> callback){
//        TribeRetrofit.getIntance().createApi(ShoppingService.class).
//                addOrder(uid,entity).enqueue(callback);
//    }
//
//    public void updateOrder(String uid,String addId,UserOrderEntity entity, Callback<CodeResponse> callback){
//        TribeRetrofit.getIntance().createApi(ShoppingService.class).
//                updateOrder(uid,addId,entity).enqueue(callback);
//    }
//
//    public void deleteOrder(String uid, String addrId, Callback<CodeResponse> callback){
//        TribeRetrofit.getIntance().createApi(ShoppingService.class).
//                deleteOrder(uid,addrId).enqueue(callback);
//    }
//
//
//    public void updateDefaultOrder(String uid,String addId, Callback<CodeResponse> callback) {
//        TribeRetrofit.getIntance().createApi(ShoppingService.class).
//                updateDefaultOrder(uid,new CommonRequestBody(addId)).enqueue(callback);
//    }

}
