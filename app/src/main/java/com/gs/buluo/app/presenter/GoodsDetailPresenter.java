package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.ResponseCode;
import com.gs.buluo.app.bean.GoodsStandard;
import com.gs.buluo.app.bean.ListGoodsDetail;
import com.gs.buluo.app.bean.RequestBodyBean.NewOrderBean;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.model.GoodsModel;
import com.gs.buluo.app.model.ShoppingModel;
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
        model.getGoodsDetail(id, new Callback<BaseCodeResponse<ListGoodsDetail>>() {
            @Override
            public void onResponse(Call<BaseCodeResponse<ListGoodsDetail>> call, Response<BaseCodeResponse<ListGoodsDetail>> response) {
                if (response.body()!=null&&response.body().code==200){
                    mView.getDetailSuccess(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<BaseCodeResponse<ListGoodsDetail>> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void getGoodsStandard(String id){
        model.getGoodsStandard(id, new Callback<BaseCodeResponse<GoodsStandard>>() {
            @Override
            public void onResponse(Call<BaseCodeResponse<GoodsStandard>> call, Response<BaseCodeResponse<GoodsStandard>> response) {
                if (response.body()!=null&&response.body().code==200){
                    mView.getStandardSuccess(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<BaseCodeResponse<GoodsStandard>> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void addCartItem(String id, int num) {
        NewOrderBean item = new NewOrderBean();
        item.goodsId= id;
        item.amount = num;
        new ShoppingModel().addShoppingCart(item, new Callback<BaseCodeResponse>() {
            @Override
            public void onResponse(Call<BaseCodeResponse> call, Response<BaseCodeResponse> response) {
                if (response.body()!=null&&response.body().code== ResponseCode.UPDATE_SUCCESS){
                    mView.addSuccess();
                }else {
                  mView.showError(R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<BaseCodeResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });

    }
}
