package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.CommunityDetail;
import com.gs.buluo.app.bean.ResponseBody.CommunityDetailResponse;
import com.gs.buluo.app.bean.ResponseBody.CommunityResponse;
import com.gs.buluo.app.bean.ResponseBody.GoodsDetailResponseBean;
import com.gs.buluo.app.bean.ResponseBody.GoodsResponseBean;
import com.gs.buluo.app.bean.ResponseBody.GoodsStandardResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/11.
 */
public interface CommunityService {
    @GET("communities")
    Call<CommunityResponse> getCommunitiesList();

    @GET("communities/{id}")
    Call<CommunityDetailResponse> getCommunityDetail(
            @Path("id") String uid);

}
