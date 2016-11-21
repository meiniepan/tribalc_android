package com.gs.buluo.app.model;

import com.gs.buluo.app.bean.BillEntity.TradingType;
import com.gs.buluo.app.bean.ResponseBody.BillResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.WalletResponse;
import com.gs.buluo.app.bean.UpdatePwdBody;
import com.gs.buluo.app.network.MoneyService;
import com.gs.buluo.app.network.TribeRetrofit;

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

    public void updatePwd(String uid, UpdatePwdBody body, Callback<CodeResponse> callback){
        TribeRetrofit.getIntance().createApi(MoneyService.class).
                updatePwd(uid,body).enqueue(callback);
    }
}
