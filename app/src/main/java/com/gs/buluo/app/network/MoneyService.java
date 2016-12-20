package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.BillEntity.TradingType;
import com.gs.buluo.app.bean.RequestBodyBean.NewPaymentRequest;
import com.gs.buluo.app.bean.ResponseBody.BillResponse;
import com.gs.buluo.app.bean.ResponseBody.CardResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.PaymentResponse;
import com.gs.buluo.app.bean.ResponseBody.SimpleCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.WalletResponse;
import com.gs.buluo.app.bean.UpdatePwdBody;

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


    @GET("persons/{id}/bank_cards")
    Call<CardResponse> getCardList(
            @Path("id") String uid);

    @POST("persons/{id}/bank_cards")
    Call<SimpleCodeResponse> addBankCard(
            @Path("id") String uid,@Query("vcode")String vCode,@Body BankCard card);

    @DELETE("persons/{id}/bank_cards/{bankCardID}")
    Call<SimpleCodeResponse> deleteCard(@Path("id")String uid,@Path("bankCardID") String id);

    @GET("persons/{id}/payments/{paymentId}")
    Call<PaymentResponse> getPaymentStatus(@Path("id")String uid,@Path("paymentId")String paymentId);

    @POST("persons/{id}/payments/")
    Call<PaymentResponse> createPayment(@Path("id")String uid,@Body NewPaymentRequest request);
}
