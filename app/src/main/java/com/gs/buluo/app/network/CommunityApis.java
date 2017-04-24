package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.CommunityDetail;
import com.gs.buluo.app.bean.ResponseBody.CommunityResponse;
import com.gs.buluo.app.bean.StoreDetail;
import com.gs.buluo.common.network.BaseResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by hjn on 2016/11/11.
 */
public interface CommunityApis {
    @GET("communities")
    Observable<CommunityResponse> getCommunitiesList();

    @GET("communities/{id}")
    Observable<BaseResponse<CommunityDetail>> getCommunityDetail(
            @Path("id") String uid);

    @GET("store_set_meals/store/{id}")
    Observable<BaseResponse<StoreDetail>> getStoreDetail(
            @Path("id") String storeId);
}
