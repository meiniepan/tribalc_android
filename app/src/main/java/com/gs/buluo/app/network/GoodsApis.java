package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.GoodList;
import com.gs.buluo.app.bean.GoodsStandard;
import com.gs.buluo.app.bean.ListGoodsDetail;
import com.gs.buluo.common.network.BaseResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hjn on 2016/11/11.
 */
public interface GoodsApis {
    @GET("goods")
    Observable<BaseResponse<GoodList>> getGoodsList(
            @Query("limitSize") int limitSize
            ,@Query("sortSkip") String sortSkip
//            ,@Query("sort") String sort
    );
    @GET("goods")
    Observable<BaseResponse<GoodList>> getGoodsListFirst(
           @Query("limitSize") String limitSize
//           ,@Query("sort") String sort
    );

    @GET("goods/{goodsID}")
    Observable<BaseResponse<ListGoodsDetail>> getGoodsDetail(@Path("goodsID") String goodsId);

    @GET("goods_standards/{id}")
    Observable<BaseResponse<GoodsStandard>> getGoodsStandard(@Path("id") String id);
}
