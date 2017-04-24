package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.RequestBodyBean.CommonRequestBody;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.common.network.BaseResponse;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by hjn on 2016/11/11.
 */
public interface AddressApis {
    @GET("persons/{id}/addresses/{addrID}")
    Observable<BaseResponse<UserAddressEntity>> getAddress(
            @Path("id") String uid, @Path("addrID") String addrID);


    @POST("persons/{id}/addresses")
    Observable<BaseResponse<UserAddressEntity>> addAddress(
            @Path("id") String uid,@Body UserAddressEntity entity);

    @PUT("persons/{id}/addresses/{addrID}")
    Observable<BaseResponse> updateAddress(
            @Path("id") String uid,@Path("addrID") String aadrId,@Body UserAddressEntity entity);

    @DELETE("persons/{id}/addresses/{addrID}")
    Observable<BaseResponse> deleteAddress(
            @Path("id") String uid,@Path("addrID") String addrId);

    @PUT("persons/{id}/addressID")
    Observable<BaseResponse> updateDefaultAddress(
            @Path("id") String uid, @Body CommonRequestBody body);
}
