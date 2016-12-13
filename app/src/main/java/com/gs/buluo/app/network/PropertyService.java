package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.PropertyManagement;
import com.gs.buluo.app.bean.RequestBodyBean.CommitPropertyFixRequestBody;
import com.gs.buluo.app.bean.ResponseBody.PropertyFixResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by fs on 2016/12/13.
 */
public interface PropertyService {
    @POST("/persons/{id}/property_management")
    Call<PropertyFixResponse> postFixOrder(@Path("id") String id, @Body CommitPropertyFixRequestBody commitPropertyFixRequestBody);
}
