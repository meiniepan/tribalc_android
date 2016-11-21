package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.BillEntity.TradingType;
import com.gs.buluo.app.bean.ResponseBody.BillResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.WalletResponse;
import com.gs.buluo.app.bean.UpdatePwdBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/11.
 */
public interface MoneyService {
    @GET("persons/{id}/wallet")
    Call<WalletResponse> getWallet(
            @Path("id") String uid);


    @GET("persons/{id}/bills")
    Call<BillResponse> getBillList(
            @Path("id") String uid ,@Query("limitSize") String limitSize,@Query("sortSkip")String sortSkip);

    @GET("persons/{id}/bills")
    Call<BillResponse> getBillListFirst(
            @Path("id") String uid ,@Query("limitSize") String limitSize);


    @PUT("persons/{id}/wallet/password")
    Call<CodeResponse> updatePwd(
            @Path("id") String uid, @Body UpdatePwdBody pwd);

//    @DELETE("persons/{id}/addresses/{addrID}")
//    Call<CodeResponse> deleteAddress(
//            @Path("id") String uid, @Path("addrID") String addrId);
//
//    @PUT("persons/{id}/sensitive_info/addressID")
//    Call<CodeResponse> updateDefaultAddress(
//            @Path("id") String uid, @Body CommonRequestBody body);
}
