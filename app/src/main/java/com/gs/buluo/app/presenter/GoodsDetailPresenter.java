package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.ResponseCode;
import com.gs.buluo.app.bean.GoodsStandard;
import com.gs.buluo.app.bean.ListGoodsDetail;
import com.gs.buluo.app.bean.RequestBodyBean.NewOrderBean;
import com.gs.buluo.common.network.BaseResponse;
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

    public void getGoodsDetail(String id){
        model.getGoodsDetail(id, new Callback<BaseResponse<ListGoodsDetail>>() {
            @Override
            public void onResponse(Call<BaseResponse<ListGoodsDetail>> call, Response<BaseResponse<ListGoodsDetail>> response) {
                if (response.body()!=null&&response.body().code==200){
                    if (isAttach()) mView.getDetailSuccess(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ListGoodsDetail>> call, Throwable t) {
                if (isAttach()) mView.showError(R.string.connect_fail);
            }
        });
    }

    public void getGoodsStandard(String id){
        model.getGoodsStandard(id, new Callback<BaseResponse<GoodsStandard>>() {
            @Override
            public void onResponse(Call<BaseResponse<GoodsStandard>> call, Response<BaseResponse<GoodsStandard>> response) {
                if (response.body()!=null&&response.body().code==200){
                    if (isAttach())mView.getStandardSuccess(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<GoodsStandard>> call, Throwable t) {
                if (isAttach())mView.showError(R.string.connect_fail);
            }
        });
    }

    public void addCartItem(String id, int num) {
        NewOrderBean item = new NewOrderBean();
        item.goodsId= id;
        item.amount = num;
        new ShoppingModel().addShoppingCart(item, new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body()!=null&&response.body().code== ResponseCode.UPDATE_SUCCESS){
                    if (isAttach())mView.addSuccess();
                }else {
                    if (isAttach())mView.showError(R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                if (isAttach()) mView.showError(R.string.connect_fail);
            }
        });

    }
}
