package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.RequestBodyBean.VerifyBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.UserAddressResponse;
import com.gs.buluo.app.bean.UserAddressEntity;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/11.
 */
public interface AddressService {
    @GET("persons/{id}/addresses/{addrID}")
    Call<UserAddressResponse> getDetailAddress(
            @Path("id") String uid, @Path("addrID") String addrID);


    @POST("persons/{id}/addresses")
    Call<UserAddressResponse> addDetailAddress(
            @Path("id") String uid,@Body UserAddressEntity entity);

    @PUT("persons/{id}/addresses/{addrID}")
    Call<CodeResponse> updateDetailAddress(
            @Path("id") String uid,@Path("aadrID") String aadrId,@Body UserAddressEntity entity);

    @DELETE("persons/{id}/addresses/{addrID}")
    Call<CodeResponse> deleteAddress(
            @Path("id") String uid,@Path("addrID") String addrId);
}
