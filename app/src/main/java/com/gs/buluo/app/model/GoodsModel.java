package com.gs.buluo.app.model;

import com.gs.buluo.app.bean.GoodList;
import com.gs.buluo.app.bean.GoodsStandard;
import com.gs.buluo.app.bean.ListGoodsDetail;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.app.network.GoodsApis;
import com.gs.buluo.app.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/16.
 */
public class GoodsModel {
    public void getGoodsListFirst(String category, String limitSize, String sortSkip, String sort, Callback<BaseResponse<GoodList>> callback) {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).
                getGoodsListFirst(20 + "").enqueue(callback);
    }

    public void getGoodsList(String category, String limitSize, String sortSkip, String sort, Callback<BaseResponse<GoodList>> callback) {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).
                getGoodsList(20, sortSkip).enqueue(callback);
    }

    public void getGoodsDetail(String id, Callback<BaseResponse<ListGoodsDetail>> callback) {
        TribeRetrofit instance = TribeRetrofit.getInstance();
        instance.createApi(GoodsApis.class).
                getGoodsDetail(id).enqueue(callback);
    }

    public void getGoodsStandard(String id, Callback<BaseResponse<GoodsStandard>> callback) {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).
                getGoodsStandard(id).enqueue(callback);
    }
}
