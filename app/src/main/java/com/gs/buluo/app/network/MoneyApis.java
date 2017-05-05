package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.BankOrderResponse;
import com.gs.buluo.app.bean.ConfirmOrderRequest;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.PrepareOrderRequest;
import com.gs.buluo.app.bean.QueryOrderRequest;
import com.gs.buluo.app.bean.RequestBodyBean.NewPaymentRequest;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.RequestBodyBean.WithdrawRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BillResponseData;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.UpdatePwdBody;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.bean.WxPayResponse;
import com.gs.buluo.common.network.BaseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hjn on 2016/11/11.
 */
public interface MoneyApis {
    @GET("wallets/{id}")
    Observable<BaseResponse<WalletAccount>> getWallet(
            @Path("id") String uid);

    @GET("wallets/{id}/bills")
    Observable<BaseResponse<BillResponseData>> getBillList(
            @Path("id") String uid, @Query("limitSize") String limitSize, @Query("sortSkip") String sortSkip);

    @GET("wallets/{id}/bills")
    Observable<BaseResponse<BillResponseData>> getBillListFirst(
            @Path("id") String uid, @Query("limitSize") String limitSize);

    @PUT("wallets/{id}/password")
    Call<BaseResponse<CodeResponse>> updatePwd(
            @Path("id") String uid, @Body UpdatePwdBody pwd);

    @PUT("wallets/{id}/password")
    Call<BaseResponse<CodeResponse>> updatePwd(
            @Path("id") String uid, @Body UpdatePwdBody pwd, @Query("vcode") String vCode);

    /**
     * 准备添加银行卡信息
     *
     * @param uid
     * @param card
     * @return
     */
    @POST("wallets/{id}/bank_cards")
    Observable<BaseResponse<BankCard>> prepareAddBankCard(
            @Path("id") String uid, @Body BankCard card);

    /**
     * 上传验证码，确认添加银行卡信息
     *
     * @param uid
     * @param cardId
     * @param verify
     * @return
     */
    @PUT("wallets/{id}/bank_cards/{bankCardID}")
    Observable<BaseResponse<CodeResponse>> uploadVerify(
            @Path("id") String uid, @Path("bankCardID") String cardId, @Body ValueRequestBody verify);

    /**
     * 宝付储蓄卡支付-预支付
     * @param uid
     * @param prepareOrderRequest
     * @return
     */
    @POST("recharge/bf_bankcard/prepare_order")
    Observable<BaseResponse<BankOrderResponse>> prepareOrder(
            @Query("me") String uid,@Body PrepareOrderRequest prepareOrderRequest);

    /**
     * 确认支付
     * @param uid
     * @param confirmOrderRequest
     * @return
     */
    @POST("recharge/bf_bankcard/confirm_order")
    Observable<BaseResponse<BankOrderResponse>> confirmOrder(
            @Query("me") String uid, @Body ConfirmOrderRequest confirmOrderRequest);

    /**
     * 查询支付结果
     * @param uid
     * @param queryOrderRequest
     * @return
     */
    @POST("recharge/bf_bankcard/query_order")
    Observable<BaseResponse<BankOrderResponse>> queryOrder(
            @Query("me") String uid, @Body QueryOrderRequest queryOrderRequest);

    @GET("wallets/{id}/bank_cards")
    Observable<BaseResponse<List<BankCard>>> getCardList(
            @Path("id") String uid);


    @DELETE("wallets/{id}/bank_cards/{bankCardID}")
    Observable<BaseResponse> deleteCard(@Path("id") String uid, @Path("bankCardID") String id);

    @GET("wallets/{id}/payments/{paymentId}")
    Observable<BaseResponse<OrderPayment>> getPaymentStatus(@Path("id") String uid, @Path("paymentId") String paymentId);

    @POST("wallets/{id}/payments")
    Observable<BaseResponse<OrderPayment>> createPayment(@Path("id") String uid, @Query("type") String type, @Body NewPaymentRequest request);

    @POST("recharge/wechat/unifiedorder")
    Observable<BaseResponse<WxPayResponse>> payInWx(@Query("me") String uid, @Body ValueRequestBody body);

    @POST("recharge/wechat/orderquery")
    Observable<BaseResponse<CodeResponse>> getTopUpResult(@Query("me") String uid, @Body ValueRequestBody body);

    @POST("wallets/{id}/withdraw")
    Observable<BaseResponse<CodeResponse>> withdrawCash(@Path("id") String uid, @Body WithdrawRequestBody body);
}
