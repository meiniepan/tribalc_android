package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.RequestBodyBean.NewPaymentRequest;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BillResponse;
import com.gs.buluo.app.bean.ResponseBody.CardResponse;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.UpdatePwdBody;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.bean.WxPayResponse;
import com.gs.buluo.app.utils.WXPayUtils;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    Call<BaseCodeResponse<WalletAccount>> getWallet(
            @Path("id") String uid);


    @GET("persons/{id}/bills")
    Call<BillResponse> getBillList(
            @Path("id") String uid ,@Query("limitSize") String limitSize,@Query("sortSkip")String sortSkip);

    @GET("persons/{id}/bills")
    Call<BillResponse> getBillListFirst(
            @Path("id") String uid ,@Query("limitSize") String limitSize);


    @PUT("persons/{id}/wallet/password")
    Call<BaseCodeResponse<CodeResponse>> updatePwd(
            @Path("id") String uid, @Body UpdatePwdBody pwd);

    @PUT("persons/{id}/wallet/password")
    Call<BaseCodeResponse<CodeResponse>> updatePwd(
            @Path("id") String uid, @Body UpdatePwdBody pwd,@Query("vcode")String vCode);

    @GET("persons/{id}/bank_cards")
    Call<CardResponse> getCardList(
            @Path("id") String uid);

    @POST("persons/{id}/bank_cards")
    Call<BaseCodeResponse<CodeResponse>> addBankCard(
            @Path("id") String uid,@Query("vcode")String vCode,@Body BankCard card);

    @DELETE("persons/{id}/bank_cards/{bankCardID}")
    Call<BaseCodeResponse> deleteCard(@Path("id")String uid, @Path("bankCardID") String id);

    @GET("persons/{id}/payments/{paymentId}")
    Call<BaseCodeResponse<OrderPayment>> getPaymentStatus(@Path("id")String uid, @Path("paymentId")String paymentId);

    @POST("persons/{id}/payments")
    Call<BaseCodeResponse<OrderPayment>> createPayment(@Path("id")String uid,@Query("type")String type,@Body NewPaymentRequest request);

    @POST("recharge/wechat/unifiedorder")
    Call<BaseCodeResponse<WxPayResponse>> payInWx(@Query("me")String uid, @Body ValueRequestBody body);

    @POST("recharge/wechat/orderquery")
    Call<BaseCodeResponse<CodeResponse>> getTopUpResult(@Query("me")String uid,@Body ValueRequestBody body);
}
