package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.ListPropertyManagement;
import com.gs.buluo.app.bean.PropertyFixListResponseData;
import com.gs.buluo.app.bean.RequestBodyBean.CommitPropertyFixRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by fs on 2016/12/13.
 */
public interface PropertyService {
    @POST("persons/{id}/property_management")
    Call<BaseCodeResponse<ListPropertyManagement>> postFixOrder(@Path("id") String id, @Body CommitPropertyFixRequestBody commitPropertyFixRequestBody);

    @GET("persons/{id}/property_management")
    Call<BaseCodeResponse<PropertyFixListResponseData>> getPropertyFixListMore(@Path("id") String id, @Query("sortSkip") String sortSkip);

    @GET("persons/{id}/property_management")
    Call<BaseCodeResponse<PropertyFixListResponseData>> getPropertyFixList(@Path("id") String id);
}
