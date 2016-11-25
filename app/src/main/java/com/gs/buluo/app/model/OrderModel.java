package com.gs.buluo.app.model;

import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;
import com.gs.buluo.app.network.OrderService;
import com.gs.buluo.app.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/14.
 */
public class OrderModel {

    public void getOrder(String uid,OrderBean.OrderStatus status, String limitSize, String sortSkip, Callback<OrderResponse> callback){
        TribeRetrofit.getIntance().createApi(OrderService.class).
                getOrder(uid,limitSize,sortSkip,status).enqueue(callback);
    }

    public void getOrderFirst(String uid, OrderBean.OrderStatus status, String limitSize, Callback<OrderResponse> callback){
        TribeRetrofit.getIntance().createApi(OrderService.class).
                getOrderFirst(uid,limitSize).enqueue(callback);
    }

//    public void addOrder(String uid, UserOrderEntity entity, Callback<UserOrderResponse> callback){
//        TribeRetrofit.getIntance().createApi(OrderService.class).
//                addOrder(uid,entity).enqueue(callback);
//    }
//
//    public void updateOrder(String uid,String addId,UserOrderEntity entity, Callback<CodeResponse> callback){
//        TribeRetrofit.getIntance().createApi(OrderService.class).
//                updateOrder(uid,addId,entity).enqueue(callback);
//    }
//
//    public void deleteOrder(String uid, String addrId, Callback<CodeResponse> callback){
//        TribeRetrofit.getIntance().createApi(OrderService.class).
//                deleteOrder(uid,addrId).enqueue(callback);
//    }
//
//
//    public void updateDefaultOrder(String uid,String addId, Callback<CodeResponse> callback) {
//        TribeRetrofit.getIntance().createApi(OrderService.class).
//                updateDefaultOrder(uid,new CommonRequestBody(addId)).enqueue(callback);
//    }
}
