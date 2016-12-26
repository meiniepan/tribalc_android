package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.GoodList;
import com.gs.buluo.app.bean.GoodsStandard;
import com.gs.buluo.app.bean.ListGoodsDetail;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/11.
 */
public interface GoodsService {
    @GET("goods")
    Call<BaseCodeResponse<GoodList>> getGoodsList(
            @Query("category") String category,@Query("limitSize") int limitSize
            ,@Query("sortSkip") String sortSkip
//            ,@Query("sort") String sort
    );
    @GET("goods")
    Call<BaseCodeResponse<GoodList>> getGoodsListFirst(
           @Query("limitSize") String limitSize
//           ,@Query("sort") String sort
    );

    @GET("goods/{goodsID}")
    Call<BaseCodeResponse<ListGoodsDetail>> getGoodsDetail(@Path("goodsID") String goodsId);

    @GET("goods_standards/{id}")
    Call<BaseCodeResponse<GoodsStandard>> getGoodsStandard(@Path("id") String id);
}
