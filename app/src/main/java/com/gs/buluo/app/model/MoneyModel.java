package com.gs.buluo.app.model;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.RequestBodyBean.NewPaymentRequest;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BillResponse;
import com.gs.buluo.app.bean.ResponseBody.CardResponse;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.bean.WxPayResponse;
import com.gs.buluo.app.network.MoneyService;
import com.gs.buluo.app.network.TribeRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/18.
 */
public class MoneyModel {
    public void getWelletInfo(String uid, Callback<BaseCodeResponse<WalletAccount>> callback){
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                getWallet(uid).enqueue(callback);
    }


    public void getBillList(String uid,String sortSkip, Callback<BillResponse> callback){
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                getBillList(uid,"20",sortSkip).enqueue(callback);
    }

    public void getBillListFirst(String uid, Callback<BillResponse> callback){
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                getBillListFirst(uid,"20").enqueue(callback);
    }

    public void getCardList(String uid, Callback<CardResponse> callback){
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                getCardList(uid).enqueue(callback);
    }

    public void addBankCard(String uid, String vCode, BankCard card, Callback<BaseCodeResponse<CodeResponse>> callback){
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                addBankCard(uid,vCode,card).enqueue(callback);
    }

    public void deleteCard(String id ,Callback<BaseCodeResponse> callback) {
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                deleteCard(TribeApplication.getInstance().getUserInfo().getId(),id).enqueue(callback);
    }

    public void createPayment(List<String> ids, String payChannel,String type, Callback<BaseCodeResponse<OrderPayment>> callback) {
        NewPaymentRequest request=new NewPaymentRequest();
        request.orderIds=ids;
        request.payChannel=payChannel;
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                createPayment(TribeApplication.getInstance().getUserInfo().getId(),type,request).enqueue(callback);
    }

    public void getPaymentStatus(String payId, Callback<BaseCodeResponse<OrderPayment>> callback) {
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                getPaymentStatus(TribeApplication.getInstance().getUserInfo().getId(),payId).enqueue(callback);
    }

    public void topUpInWx(String price, Callback<BaseCodeResponse<WxPayResponse>> callback){
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                payInWx(TribeApplication.getInstance().getUserInfo().getId(),new ValueRequestBody(price)).enqueue(callback);
    }

    public void getWXRechargeResult(String prepayId, Callback<BaseCodeResponse<CodeResponse>> callback){
        TribeRetrofit.getInstance().createApi(MoneyService.class).
                getTopUpResult(TribeApplication.getInstance().getUserInfo().getId(),new ValueRequestBody(prepayId)).enqueue(callback);
    }
}
