package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ListReservation;
import com.gs.buluo.app.bean.RequestBodyBean.ReserveRequestBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.ReserveDetailResponse;
import com.gs.buluo.app.bean.ResponseBody.SimpleCodeResponse;
import com.gs.buluo.app.model.ReserveModel;
import com.gs.buluo.app.view.impl.IDetailReserveView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/30.
 */
public class DetailReservationPresenter extends BasePresenter<IDetailReserveView> {
    ReserveModel model;
    public DetailReservationPresenter(){
        model=new ReserveModel();
    }

    public void getReserveDetail(String id){
        model.getServeDetail(id, TribeApplication.getInstance().getUserInfo().getId(), new Callback<ReserveDetailResponse>() {
            @Override
            public void onResponse(Call<ReserveDetailResponse> call, Response<ReserveDetailResponse> response) {
                if (response.body()!=null&&response.body().code==200){
                    mView.getDetailSuccess(response.body().data);
                }else {
                    mView.showError(R.string.connect_fail);
                }

            }

            @Override
            public void onFailure(Call<ReserveDetailResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void cancelReserve(String id,String key){
        model.cancelReserve(id, TribeApplication.getInstance().getUserInfo().getId(),new ReserveRequestBody(key), new Callback<SimpleCodeResponse>() {
            @Override
            public void onResponse(Call<SimpleCodeResponse> call, Response<SimpleCodeResponse> response) {
                if (response.body()!=null&&response.body().code==200){
                    mView.cancelSuccess();
                }else {
                    mView.cancelFailure();
                }
            }

            @Override
            public void onFailure(Call<SimpleCodeResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
