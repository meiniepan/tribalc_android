package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.GoodList;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
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
        model.getGoodsListFirst("", "", "", "",  new Callback<BaseCodeResponse<GoodList>>() {
            @Override
            public void onResponse(Call<BaseCodeResponse<GoodList>> call, Response<BaseCodeResponse<GoodList>> response) {
                if (response.body()!=null&&response.body().data!=null&&response.body().code==200){
                    nextSkip = response.body().data.nextSkip;
                    if (isAttach())mView.getGoodsInfo(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<BaseCodeResponse<GoodList>> call, Throwable t) {
                if (isAttach())mView.showError(R.string.connect_fail);
            }
        });
    }

    public void loadMore() {
            model.getGoodsList("", "", nextSkip, "saleQuantity,desc", new Callback<BaseCodeResponse<GoodList>>() {
                @Override
                public void onResponse(Call<BaseCodeResponse<GoodList>> call, Response<BaseCodeResponse<GoodList>> response) {
                    if (response.code()==200){
                        mView.getGoodsInfo(response.body().data);
                    }
                }

                @Override
                public void onFailure(Call<BaseCodeResponse<GoodList>> call, Throwable t) {
                    if (isAttach())mView.showError(R.string.connect_fail);
                }
            });
        }
}
