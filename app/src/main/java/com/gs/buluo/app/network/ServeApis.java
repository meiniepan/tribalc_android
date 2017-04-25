package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.DetailStoreSetMeal;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.app.bean.ResponseBody.ServeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hjn on 2016/11/11.
 */
public interface ServeApis {
    @GET("store_set_meals")
    Observable<BaseResponse<ServeResponse>> getServiceList(
            @Query("category") String category,
            @Query("limitSize") int limitSize,
            @Query("sortSkip") String sortSkip,@Query("sort") String sort);

    @GET("store_set_meals")
    Observable<BaseResponse<ServeResponse>> getServiceList(
            @Query("category") String category,
            @Query("limitSize") int limitSize,
            @Query("sortSkip") String sortSkip,@Query("sort") String sort,
            @Query("coordinate") String coordinate);

    @GET("store_set_meals")
    Observable<BaseResponse<ServeResponse>> getServiceListFirst(
            @Query("category") String category,
            @Query("limitSize") int limitSize, @Query("sort") String sort,
            @Query("coordinate") String coordinate);

    @GET("store_set_meals")
    Observable<BaseResponse<ServeResponse>> getServiceListFirst(
            @Query("category") String category,
            @Query("limitSize") int limitSize, @Query("sort") String sort);

    @GET("store_set_meals/{id}")
    Observable<BaseResponse<DetailStoreSetMeal>> getServeDetail(@Path("id")String serveId);

}

