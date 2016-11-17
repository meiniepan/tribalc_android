package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.ResponseBody.GoodsResponseBean;
import com.gs.buluo.app.bean.ResponseBody.UserAddressResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/11.
 */
public interface GoodsService {
    @GET("goods")
    Call<GoodsResponseBean> getGoodsList(
            @Query("category") String category,@Query("limitSize") int limitSize
            ,@Query("sortSkip") String sortSkip,@Query("sort") String sort);
    @GET("goods")
    Call<GoodsResponseBean> getGoodsListFirst(
           @Query("limitSize") int limitSize
           ,@Query("sort") String sort);
}
