package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ListGoodsDetail;
import com.gs.buluo.app.bean.ResponseBody.ShoppingCartResponse;
import com.gs.buluo.app.model.ShoppingModel;
import com.gs.buluo.app.view.impl.IShoppingView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/12/2.
 */
public class ShoppingCarPresenter extends BasePresenter<IShoppingView> {
    ShoppingModel model;
    private String nextSkip;

    public ShoppingCarPresenter(){
        model=new ShoppingModel();
    }


    public void getShoppingListFirst(){
        model.getShoppingListFirst(TribeApplication.getInstance().getUserInfo().getId(), new Callback<ShoppingCartResponse>() {
            @Override
            public void onResponse(Call<ShoppingCartResponse> call, Response<ShoppingCartResponse> response) {
                if (response.body()!=null&&response.body().code==200){
                    ShoppingCartResponse.ShoppingCartResponseBody data = response.body().data;
                    mView.getShoppingCarInfoSuccess(data);
                    nextSkip=data.nextSkip;
                }else {
                    mView.showError(R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<ShoppingCartResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void getShoppingCarMore(){
        model.getShoppingListMore(TribeApplication.getInstance().getUserInfo().getId(), nextSkip, new Callback<ShoppingCartResponse>() {
            @Override
            public void onResponse(Call<ShoppingCartResponse> call, Response<ShoppingCartResponse> response) {
                if (response.body()!=null&&response.body().code==200){
                    ShoppingCartResponse.ShoppingCartResponseBody data = response.body().data;
                    mView.getShoppingCarInfoSuccess(data);
                    nextSkip=data.nextSkip;
                }
            }

            @Override
            public void onFailure(Call<ShoppingCartResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void updateGoods(ListGoodsDetail item) {



    }
}
