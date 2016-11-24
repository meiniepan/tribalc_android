package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ResponseBody.CardResponse;
import com.gs.buluo.app.model.MoneyModel;
import com.gs.buluo.app.view.impl.ICardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/23.
 */
public class BankCardPresenter extends BasePresenter<ICardView> {
    MoneyModel moneyModel;

    public BankCardPresenter(){
        moneyModel=new MoneyModel();
    }

    public void getCardList(String uid){
        moneyModel.getCardList(uid, new Callback<CardResponse>() {
            @Override
            public void onResponse(Call<CardResponse> call, Response<CardResponse> response) {
                if (response.body()!=null&&response.body().code==200){
                    mView.getCardInfoSuccess(response.body().data);
                }else {
                    mView.showError(R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<CardResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
