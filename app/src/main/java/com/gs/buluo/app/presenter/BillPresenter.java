package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.BillResponse;
import com.gs.buluo.app.model.MoneyModel;
import com.gs.buluo.app.view.impl.IBillView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/18.
 */
public class BillPresenter extends BasePresenter<IBillView>{

    private final MoneyModel moneyModel;
    private String nextSkip;

    public BillPresenter(){
        moneyModel = new MoneyModel();
    }

    public void getBillListFirst(){
        moneyModel.getBillListFirst(TribeApplication.getInstance().getUserInfo().getId(), new Callback<BillResponse>() {
            @Override
            public void onResponse(Call<BillResponse> call, Response<BillResponse> response) {
                if (response.body()!=null&&response.body().code==200){
                    mView.getBillSuccess(response.body().data);
                    nextSkip =  response.body().data.nextSkip;
                }
            }

            @Override
            public void onFailure(Call<BillResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void loadMoreBill() {
        moneyModel.getBillList(TribeApplication.getInstance().getUserInfo().getId(), nextSkip, new Callback<BillResponse>() {
            @Override
            public void onResponse(Call<BillResponse> call, Response<BillResponse> response) {
                if (response.body()!=null&&response.body().code==200){
                    nextSkip= response.body().data.nextSkip;
                    mView.getBillSuccess(response.body().data);
                }

            }

            @Override
            public void onFailure(Call<BillResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
