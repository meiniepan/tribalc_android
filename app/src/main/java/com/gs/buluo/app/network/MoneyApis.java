package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.ResultResponse;
import com.gs.buluo.app.bean.ConfirmOrderRequest;
import com.gs.buluo.app.bean.CreditBill;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.Pay2MerchantRequest;
import com.gs.buluo.app.bean.PrepareOrderRequest;
import com.gs.buluo.app.bean.PrivilegeResponse;
import com.gs.buluo.app.bean.QueryOrderRequest;
import com.gs.buluo.app.bean.RequestBodyBean.NewPaymentRequest;
import com.gs.buluo.app.bean.RequestBodyBean.PaySessionResponse;
import com.gs.buluo.app.bean.RequestBodyBean.ValueBody;
import com.gs.buluo.app.bean.RequestBodyBean.WithdrawRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BillResponseData;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.CreditBillResponse;
import com.gs.buluo.app.bean.UpdatePwdBody;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.common.network.BaseResponse;

import java.util.ArrayList;
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
     * 已绑定银行卡列表
     */
    @GET("wallets/{id}/bank_cards")
    Observable<BaseResponse<List<BankCard>>> getCardList(
            @Path("id") String uid);

    /**
     * 支持银行卡列表
     */
    @GET("wallets/banks")
    Observable<BaseResponse<List<BankCard>>> getAllCardList(
            @Query("me") String uid);



    @DELETE("wallets/{id}/bank_cards/{bankCardID}")
    Observable<BaseResponse> deleteCard(@Path("id") String uid, @Path("bankCardID") String id);


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
    Observable<BaseResponse<CodeResponse>> confirmAddBankCard(
            @Path("id") String uid, @Path("bankCardID") String cardId, @Body ValueBody verify);


    /**
     * 提交付款申请
     *
     * @return
     */
    @POST("wallets/{id}/payments")
    Observable<BaseResponse<OrderPayment>> createPayment(@Path("id") String uid, @Query("type") String type, @Body NewPaymentRequest request);


    /**
     * 获取宝付预支付SESSION_ID ，调取SDK
     *
     * @return
     */
    @POST("recharge/bf_bankcard/generate_session_id")
    Observable<BaseResponse<PaySessionResponse>> getPrepareOrderInfo(
            @Body ValueBody requestBody);

    /**
     * 宝付储蓄卡支付-预支付
     *
     * @param prepareOrderRequest
     * @return
     */
    @POST("recharge/bf_bankcard/prepare_order")
    Observable<BaseResponse<ResultResponse>> prepareOrder(
            @Body PrepareOrderRequest prepareOrderRequest);

    /**
     * 确认支付
     *
     * @param confirmOrderRequest
     * @return
     */
    @POST("recharge/bf_bankcard/confirm_order")
    Observable<BaseResponse<ResultResponse>> confirmOrder(
            @Body ConfirmOrderRequest confirmOrderRequest);

    /**
     * 查询支付结果
     *
     * @param queryOrderRequest
     * @return
     */
    @POST("recharge/bf_bankcard/query_order")
    Observable<BaseResponse<ResultResponse>> queryOrder(
            @Body QueryOrderRequest queryOrderRequest);


    @GET("wallets/{id}/payments/{paymentId}")
    Observable<BaseResponse<OrderPayment>> getPaymentStatus(@Path("id") String uid, @Path("paymentId") String paymentId);


    @POST("recharge/wechat/orderquery")
    Observable<BaseResponse<CodeResponse>> getTopUpResult(@Body ValueBody body);

    @POST("wallets/{id}/withdraw")
    Observable<BaseResponse<CodeResponse>> withdrawCash(@Path("id") String uid, @Body WithdrawRequestBody body);

    /**
     * 提交面对面付款申请
     *
     * @param uid
     * @param pay2MerchantRequest
     * @return
     */
    @POST("wallets/{id}/payments?type=face2face")
    Observable<BaseResponse<OrderPayment>> pay2Merchant(
            @Path("id") String uid, @Body Pay2MerchantRequest pay2MerchantRequest);

    @POST("wallets/{id}/payments?type=rent")
    Observable<BaseResponse<OrderPayment>> payRent(
            @Path("id") String uid, @Body Pay2MerchantRequest pay2MerchantRequest);

    @GET("configs/bf_supported_bank")
    Observable<BaseResponse<ArrayList<BankCard>>> getSupportBankCards(@Query("type") String type);

    @GET("stores/{storeId}/privilege")
    Observable<BaseResponse<PrivilegeResponse>> getDiscountInfo(@Path("storeId") String sId,@Query("me")String uid, @Query("active") boolean active);

//    @POST("recharge/wechat/unifiedorder")
//    Observable<BaseResponse<WxPayResponse>> payInWx(@Query("me") String uid, @Body ValueBody body);

    @GET("wallets/{id}/credits")
    Observable<BaseResponse<CreditBillResponse>> getCreditBillList(@Path("id") String uid, @Query("limit") int limit, @Query("sinceTime") String sinceTime);

    /**
     * 查询当前信用账单
     */
    @GET("wallets/{id}/credits/activated")
    Observable<BaseResponse<CreditBill>> getCurrentCreditBill(@Path("id") String uid);

    /**
     * 提交付款申请
     *
     * @param uid
     * @param pay2MerchantRequest
     * @return
     */
    @POST("wallets/{id}/payments")
    Observable<BaseResponse<OrderPayment>> doPay(
            @Path("id") String uid, @Query("type") String type, @Body Pay2MerchantRequest pay2MerchantRequest);
}
