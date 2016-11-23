package com.gs.buluo.app.presenter;

import android.util.Log;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ResponseBody.GoodsDetailResponseBean;
import com.gs.buluo.app.bean.ResponseBody.GoodsStandardResponse;
import com.gs.buluo.app.model.GoodsModel;
import com.gs.buluo.app.view.impl.IGoodDetialView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/22.
 */
public class GoodsDetailPresenter extends BasePresenter<IGoodDetialView> {
    GoodsModel model;
    public GoodsDetailPresenter(){
        model=new GoodsModel();
    }

    public void getGoodsDetaii(String id){
        model.getGoodsDetail(id, new Callback<GoodsDetailResponseBean>() {
            @Override
            public void onResponse(Call<GoodsDetailResponseBean> call, Response<GoodsDetailResponseBean> response) {
                if (response.body()!=null&&response.body().code==200){
                    mView.getDetailSuccess(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<GoodsDetailResponseBean> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void getGoodsStandard(String id){
        model.getGoodsStandard(id, new Callback<GoodsStandardResponse>() {
            @Override
            public void onResponse(Call<GoodsStandardResponse> call, Response<GoodsStandardResponse> response) {
                if (response.body()!=null&&response.body().code==200){
                    mView.getStandardSuccess(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<GoodsStandardResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

}
