package com.gs.buluo.app.model;

import com.gs.buluo.app.bean.CommunityDetail;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.app.bean.ResponseBody.CommunityResponse;
import com.gs.buluo.app.bean.StoreDetail;
import com.gs.buluo.app.network.CommunityApis;
import com.gs.buluo.app.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/28.
 */
public class CommunityModel {
    public void getCommunitiesList(Callback<CommunityResponse> callback) {
        TribeRetrofit.getInstance().createApi(CommunityApis.class).
                getCommunitiesList().enqueue(callback);
    }

    public void getCommunityDetail(String uid,Callback<BaseResponse<CommunityDetail>> callback){
        TribeRetrofit.getInstance().createApi(CommunityApis.class).
                getCommunityDetail(uid).enqueue(callback);
    }

    public void getStoreDetail(String strorId,Callback<BaseResponse<StoreDetail>> callback){
        TribeRetrofit.getInstance().createApi(CommunityApis.class).
                getStoreDetail(strorId).enqueue(callback);
    }
}
