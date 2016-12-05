package com.gs.buluo.app.network;

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
public interface GoodsService {
    @GET("goods")
    Call<GoodsResponseBean> getGoodsList(
            @Query("category") String category,@Query("limitSize") int limitSize
            ,@Query("sortSkip") String sortSkip
//            ,@Query("sort") String sort
    );
    @GET("goods")
    Call<GoodsResponseBean> getGoodsListFirst(
           @Query("limitSize") String limitSize
//           ,@Query("sort") String sort
    );

    @GET("goods/{goodsID}")
    Call<GoodsDetailResponseBean> getGoodsDetail(@Path("goodsID") String goodsId);

    @GET("goods_standards/{id}")
    Call<GoodsStandardResponse> getGoodsStandard(@Path("id") String id);
}
