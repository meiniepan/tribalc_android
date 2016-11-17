package com.gs.buluo.app.model;
import com.gs.buluo.app.bean.ResponseBody.GoodsResponseBean;
import com.gs.buluo.app.network.GoodsService;
import com.gs.buluo.app.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/16.
 */
public class GoodsModel {
    public void getGoodsListFirst(String category, String limitSize, String sortSkip, String sort, Callback<GoodsResponseBean> callback){
        TribeRetrofit.getIntance().createApi(GoodsService.class).
                getGoodsListFirst(20,"saleQuantity,desc").enqueue(callback);
    }

    public void getGoodsList(String category, String limitSize, String sortSkip, String sort, Callback<GoodsResponseBean> callback){
        TribeRetrofit.getIntance().createApi(GoodsService.class).
                getGoodsList(category,20,sortSkip,sort).enqueue(callback);
    }
}
