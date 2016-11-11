package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.app.bean.RequestBodyBean.VerifyBody;
import com.gs.buluo.app.bean.ResponseBody.UpdateResponse;
import com.gs.buluo.app.bean.UserBeanResponse;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.bean.UserInfoResponse;
import com.gs.buluo.app.bean.UserSetting;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by hjn on 2016/11/3.
 */
public interface MainService {

    @GET("persons/{id}")
    Call<UserInfoResponse> getUser(
            @Path("id") String uid);

    @Multipart
    @PUT("persons/{id}")
    Call<UpdateResponse> updateUser(
            @Path("")String id,@Part("nickname")String name);

    @POST("persons/login")
    Call<UserBeanResponse> doLogin(@Body LoginBody params);

    @POST("persons/phone")
    Call<UserBeanResponse> doVerify(@Body VerifyBody phone);
}
