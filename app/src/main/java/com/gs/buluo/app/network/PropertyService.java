package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.PropertyManagement;
import com.gs.buluo.app.bean.RequestBodyBean.CommitPropertyFixRequestBody;
import com.gs.buluo.app.bean.ResponseBody.PropertyFixListResponse;
import com.gs.buluo.app.bean.ResponseBody.PropertyFixResponse;

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
    Call<PropertyFixResponse> postFixOrder(@Path("id") String id, @Body CommitPropertyFixRequestBody commitPropertyFixRequestBody);

    @GET("/persons/{id}/property_management")
    Call<PropertyFixListResponse> getPropertyFixList(@Path("id") String id,@Query("status") String status, @Query("limitSize") int limitSize, @Query("sortSkip") String sortSkip);

}
