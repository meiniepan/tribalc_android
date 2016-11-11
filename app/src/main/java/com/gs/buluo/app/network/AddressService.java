package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.ResponseBody.UserAddressResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hjn on 2016/11/11.
 */
public interface AddressService {
    @GET("persons/{id}/addresses")
    Call<UserAddressResponse> getDetailAddress(
            @Path("id") String uid);


}
