package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.CommunityDetail;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.CommunityResponse;
import com.gs.buluo.app.bean.StoreDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hjn on 2016/11/11.
 */
public interface CommunityService {
    @GET("communities")
    Call<CommunityResponse> getCommunitiesList();

    @GET("communities/{id}")
    Call<BaseCodeResponse<CommunityDetail>> getCommunityDetail(
            @Path("id") String uid);

    @GET("store_set_meals/store/{id}")
    Call<BaseCodeResponse<StoreDetail>> getStoreDetail(
            @Path("id") String storeId);
}
