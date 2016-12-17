package com.gs.buluo.app.model;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.BillEntity.TradingType;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.RequestBodyBean.NewPaymentRequest;
import com.gs.buluo.app.bean.ResponseBody.BillResponse;
import com.gs.buluo.app.bean.ResponseBody.CardResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.PaymentResponse;
import com.gs.buluo.app.bean.ResponseBody.SimpleCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.WalletResponse;
import com.gs.buluo.app.bean.UpdatePwdBody;
import com.gs.buluo.app.network.MoneyService;
import com.gs.buluo.app.network.TribeRetrofit;

import java.util.List;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/18.
 */
public class MoneyModel {
    public void getWelletInfo(String uid, Callback<WalletResponse> callback){
        TribeRetrofit.getIntance().createApi(MoneyService.class).
                getWallet(uid).enqueue(callback);
    }


    public void getBillList(String uid,String sortSkip, Callback<BillResponse> callback){
        TribeRetrofit.getIntance().createApi(MoneyService.class).
                getBillList(uid,"20",sortSkip).enqueue(callback);
    }

    public void getBillListFirst(String uid, Callback<BillResponse> callback){
        TribeRetrofit.getIntance().createApi(MoneyService.class).
                getBillListFirst(uid,"20").enqueue(callback);
    }

    public void getCardList(String uid, Callback<CardResponse> callback){
        TribeRetrofit.getIntance().createApi(MoneyService.class).
                getCardList(uid).enqueue(callback);
    }

    public void addBankCard(String uid, String vCode, BankCard card, Callback<SimpleCodeResponse> callback){
        TribeRetrofit.getIntance().createApi(MoneyService.class).
                addBankCard(uid,vCode,card).enqueue(callback);
    }

    public void deleteCard(String id ,Callback<SimpleCodeResponse> callback) {
        TribeRetrofit.getIntance().createApi(MoneyService.class).
                deleteCard(TribeApplication.getInstance().getUserInfo().getId(),id).enqueue(callback);
    }

    public void createPayment(List<String> ids, OrderBean.PayChannel payChannel, Callback<PaymentResponse> callback) {
        NewPaymentRequest request=new NewPaymentRequest();
        request.orderIds=ids;
        request.payChannel=payChannel;
        TribeRetrofit.getIntance().createApi(MoneyService.class).
                createPayment(request).enqueue(callback);
    }

    public void getPaymentStatus(String payId, Callback<PaymentResponse> callback) {
        TribeRetrofit.getIntance().createApi(MoneyService.class).
                getPaymentStatus(TribeApplication.getInstance().getUserInfo().getId(),payId).enqueue(callback);
    }
}
