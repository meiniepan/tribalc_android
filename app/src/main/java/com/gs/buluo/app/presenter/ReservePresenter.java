package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.ReserveResponse;
import com.gs.buluo.app.model.ReserveModel;
import com.gs.buluo.app.view.impl.IReserveView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/29.
 */
public class ReservePresenter extends BasePresenter<IReserveView> {
    private ReserveModel model;
    private String nextSkip;

    public ReservePresenter(){
        model=new ReserveModel();
    }

    public void getReserveListFirst(String category){
        model.getReserveListFirst(category, TribeApplication.getInstance().getUserInfo().getId(),20,new Callback<ReserveResponse>() {
            @Override
            public void onResponse(Call<ReserveResponse> call, Response<ReserveResponse> response) {
                if (response.body()!=null&&response.body().code==200&&response.body().data!=null){
                    ReserveResponse.ReserveResponseBody data = response.body().data;
                    nextSkip= data.nextSkip;
                    mView.getReserveSuccess(response.body().data);
                }else {
                    mView.showError(R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<ReserveResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
    public void getReserveMore(String category){
        model.getReserveList(category,TribeApplication.getInstance().getUserInfo().getId(), 20,nextSkip, new Callback<ReserveResponse>() {
            @Override
            public void onResponse(Call<ReserveResponse> call, Response<ReserveResponse> response) {
                if (response.body()!=null&&response.body().code==200&&response.body().data!=null){
                    mView.getReserveSuccess(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<ReserveResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
