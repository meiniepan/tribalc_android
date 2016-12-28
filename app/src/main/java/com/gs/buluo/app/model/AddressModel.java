package com.gs.buluo.app.model;

import com.gs.buluo.app.bean.RequestBodyBean.CommonRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.network.AddressService;
import com.gs.buluo.app.network.TribeCallback;
import com.gs.buluo.app.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/14.
 */
public class AddressModel {
    public void getAddress(String uid, String addId, Callback<BaseCodeResponse<UserAddressEntity>> callback) {
        TribeRetrofit.getInstance().createApi(AddressService.class).
                getAddress(uid, addId).enqueue(callback);
    }


    public void addAddress(String uid, UserAddressEntity entity, Callback<BaseCodeResponse<UserAddressEntity>> callback) {
        TribeRetrofit.getInstance().createApi(AddressService.class).
                addAddress(uid, entity).enqueue(callback);
    }

    public void updateAddress(String uid, String addId, UserAddressEntity entity, Callback<BaseCodeResponse> callback) {
        TribeRetrofit.getInstance().createApi(AddressService.class).
                updateAddress(uid, addId, entity).enqueue(callback);
    }

    public void deleteAddress(String uid, String addrId, Callback<BaseCodeResponse> callback) {
        TribeRetrofit.getInstance().createApi(AddressService.class).
                deleteAddress(uid, addrId).enqueue(callback);
    }


    public void updateDefaultAddress(String uid, String addId, TribeCallback callback) {
        TribeRetrofit.getInstance().createApi(AddressService.class).
                updateDefaultAddress(uid, new CommonRequestBody(addId)).enqueue(callback);
    }

}
