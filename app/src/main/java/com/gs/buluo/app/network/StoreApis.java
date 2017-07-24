package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.StoreDesc;
import com.gs.buluo.app.bean.StoreInfo;
import com.gs.buluo.app.bean.StoreListResponse;
import com.gs.buluo.common.network.BaseResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Solang on 2017/6/8.
 */

public interface StoreApis {
    @GET("stores/{id}")
    Observable<BaseResponse<StoreDesc>> getStoreDesc(@Path("id") String targetId, @Query("me") String uid);

    /**
     * 分页查询店铺列表
     * @param
     * @return
     */
    @GET("stores")
    Observable<BaseResponse<StoreListResponse>> getStoreList();

    @GET("stores/{storeId}?type=person")
    Observable<BaseResponse<StoreInfo>> getStoreInfo(
            @Path("storeId") String storeId, @Query("me") String uid
    );
}
