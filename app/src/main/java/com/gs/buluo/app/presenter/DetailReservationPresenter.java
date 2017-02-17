package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.DetailReservation;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
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
        model.getServeDetail(id, TribeApplication.getInstance().getUserInfo().getId(), new Callback<BaseResponse<DetailReservation>>() {
            @Override
            public void onResponse(Call<BaseResponse<DetailReservation>> call, Response<BaseResponse<DetailReservation>> response) {
                if (mView==null)return;
                if (response.body()!=null&&response.body().code==200){
                    if (isAttach())   mView.getDetailSuccess(response.body().data);
                }else {
                    if (isAttach()) mView.showError(R.string.connect_fail);
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<DetailReservation>> call, Throwable t) {
                if (mView==null)return;
                if (isAttach()) mView.showError(R.string.connect_fail);
            }
        });
    }

    public void cancelReserve(String id,String key){
        model.cancelReserve(id, TribeApplication.getInstance().getUserInfo().getId(),new ValueRequestBody(key), new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body()!=null&&response.body().code==200){
                    if (isAttach()) mView.cancelSuccess();
                }else {
                    if (isAttach()) mView.cancelFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                if (isAttach()) mView.showError(R.string.connect_fail);
            }
        });
    }
}
