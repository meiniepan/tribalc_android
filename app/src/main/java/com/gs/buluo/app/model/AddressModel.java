package com.gs.buluo.app.model;

import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.UserAddressResponse;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.network.AddressService;
import com.gs.buluo.app.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/14.
 */
public class AddressModel {
    public void getAddress(String uid,String addId,Callback<UserAddressResponse> callback){
        TribeRetrofit.getIntance().createApi(AddressService.class).
                getDetailAddress(uid,addId).enqueue(callback);
    }


    public void addAddress(String uid, UserAddressEntity entity, Callback<UserAddressResponse> callback){
        TribeRetrofit.getIntance().createApi(AddressService.class).
                addDetailAddress(uid,entity).enqueue(callback);
    }

    public void updateAddress(String uid,String addId,UserAddressEntity entity, Callback<CodeResponse> callback){
        TribeRetrofit.getIntance().createApi(AddressService.class).
                updateDetailAddress(uid,addId,entity).enqueue(callback);
    }

    public void deleteAddress(String uid, String addrId, Callback<CodeResponse> callback){
        TribeRetrofit.getIntance().createApi(AddressService.class).
                deleteAddress(uid,addrId).enqueue(callback);
    }


}
