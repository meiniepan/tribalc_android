package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.ListPropertyManagement;
import com.gs.buluo.app.bean.PropertyFixListResponseData;
import com.gs.buluo.app.bean.RequestBodyBean.CommitPropertyFixRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by fs on 2016/12/13.
 */
public interface PropertyApis {
    @POST("property_orders")
    Call<BaseResponse<ListPropertyManagement>> postFixOrder(@Query("me") String id, @Body CommitPropertyFixRequestBody commitPropertyFixRequestBody);

    @GET("property_orders?type=owner")
    Call<BaseResponse<PropertyFixListResponseData>> getPropertyFixListMore(@Query("me") String id,
                                                                           @Query("sortSkip") String sortSkip);

    @GET("property_orders?type=owner")
    Call<BaseResponse<PropertyFixListResponseData>> getPropertyFixList(@Query("me") String id);

    @PUT("property_orders/{propertyId}?type=owner")
    Call<BaseResponse<CodeResponse>> cancelPropertyFixList(@Path("propertyId") String propertyId,@Query("me")String uid);

}
