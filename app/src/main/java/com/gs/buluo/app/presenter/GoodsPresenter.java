package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ResponseBody.GoodsResponseBean;
import com.gs.buluo.app.model.GoodsModel;
import com.gs.buluo.app.view.impl.IGoodsView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/16.
 */
public class GoodsPresenter extends BasePresenter<IGoodsView>{
    GoodsModel model;
    private String nextSkip;

    public GoodsPresenter(){
        model=new GoodsModel();
    }

    public void getGoodsList(){
        model.getGoodsListFirst("", "", "", "",  new Callback<GoodsResponseBean>() {
            @Override
            public void onResponse(Call<GoodsResponseBean> call, Response<GoodsResponseBean> response) {
                if (response.body().data!=null&&response.body().code==200){
                    nextSkip = response.body().data.nextSkip;
                    mView.getGoodsInfo(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<GoodsResponseBean> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void loadMore() {
            model.getGoodsList("FOOD", "", nextSkip, "saleQuantity,desc", new Callback<GoodsResponseBean>() {
                @Override
                public void onResponse(Call<GoodsResponseBean> call, Response<GoodsResponseBean> response) {
                    if (response.code()==200){
                        mView.getGoodsInfo(response.body().data);
                    }
                }

                @Override
                public void onFailure(Call<GoodsResponseBean> call, Throwable t) {

                }
            });
        }
}
