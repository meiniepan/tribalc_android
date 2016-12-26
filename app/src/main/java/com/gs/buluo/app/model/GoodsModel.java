package com.gs.buluo.app.model;

import com.gs.buluo.app.bean.GoodList;
import com.gs.buluo.app.bean.GoodsStandard;
import com.gs.buluo.app.bean.ListGoodsDetail;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.network.GoodsService;
import com.gs.buluo.app.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/16.
 */
public class GoodsModel {
    public void getGoodsListFirst(String category, String limitSize, String sortSkip, String sort, Callback<BaseCodeResponse<GoodList>> callback) {
        TribeRetrofit.getInstance().createApi(GoodsService.class).
                getGoodsListFirst(20 + "").enqueue(callback);
    }

    public void getGoodsList(String category, String limitSize, String sortSkip, String sort, Callback<BaseCodeResponse<GoodList>> callback) {
        TribeRetrofit.getInstance().createApi(GoodsService.class).
                getGoodsList(category, 20, sortSkip).enqueue(callback);
    }

    public void getGoodsDetail(String id, Callback<BaseCodeResponse<ListGoodsDetail>> callback) {
        TribeRetrofit.getInstance().createApi(GoodsService.class).
                getGoodsDetail(id).enqueue(callback);
    }

    public void getGoodsStandard(String id, Callback<BaseCodeResponse<GoodsStandard>> callback) {
        TribeRetrofit.getInstance().createApi(GoodsService.class).
                getGoodsStandard(id).enqueue(callback);
    }
}
