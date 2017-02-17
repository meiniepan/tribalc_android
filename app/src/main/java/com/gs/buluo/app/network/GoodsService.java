package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.GoodList;
import com.gs.buluo.app.bean.GoodsStandard;
import com.gs.buluo.app.bean.ListGoodsDetail;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/11.
 */
public interface GoodsService {
    @GET("goods")
    Call<BaseResponse<GoodList>> getGoodsList(
            @Query("limitSize") int limitSize
            ,@Query("sortSkip") String sortSkip
//            ,@Query("sort") String sort
    );
    @GET("goods")
    Call<BaseResponse<GoodList>> getGoodsListFirst(
           @Query("limitSize") String limitSize
//           ,@Query("sort") String sort
    );

    @GET("goods/{goodsID}")
    Call<BaseResponse<ListGoodsDetail>> getGoodsDetail(@Path("goodsID") String goodsId);

    @GET("goods_standards/{id}")
    Call<BaseResponse<GoodsStandard>> getGoodsStandard(@Path("id") String id);
}
