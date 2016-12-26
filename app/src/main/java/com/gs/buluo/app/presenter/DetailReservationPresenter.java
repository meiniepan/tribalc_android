package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.DetailReservation;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
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
        model.getServeDetail(id, TribeApplication.getInstance().getUserInfo().getId(), new Callback<BaseCodeResponse<DetailReservation>>() {
            @Override
            public void onResponse(Call<BaseCodeResponse<DetailReservation>> call, Response<BaseCodeResponse<DetailReservation>> response) {
                if (mView==null)return;
                if (response.body()!=null&&response.body().code==200){
                    mView.getDetailSuccess(response.body().data);
                }else {
                    mView.showError(R.string.connect_fail);
                }

            }

            @Override
            public void onFailure(Call<BaseCodeResponse<DetailReservation>> call, Throwable t) {
                if (mView==null)return;
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void cancelReserve(String id,String key){
        model.cancelReserve(id, TribeApplication.getInstance().getUserInfo().getId(),new ValueRequestBody(key), new Callback<BaseCodeResponse>() {
            @Override
            public void onResponse(Call<BaseCodeResponse> call, Response<BaseCodeResponse> response) {
                if (response.body()!=null&&response.body().code==200){
                    mView.cancelSuccess();
                }else {
                    mView.cancelFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseCodeResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
