package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.RequestBodyBean.AuthorityRequest;
import com.gs.buluo.app.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.app.bean.RequestBodyBean.PhoneUpdateBody;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.UploadAccessBody;
import com.gs.buluo.app.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.app.bean.ResponseBody.UserAddressListResponse;
import com.gs.buluo.app.bean.ResponseBody.UserBeanResponse;
import com.gs.buluo.app.bean.ResponseBody.UserInfoResponse;
import com.gs.buluo.app.bean.UserSensitiveEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/3.
 */
public interface MainService {

    @GET("persons/{id}")
    Call<UserInfoResponse> getUser(
            @Path("id") String uid);

    @POST("persons/login")
    Call<UserBeanResponse> doLogin(@Body LoginBody params);

    @POST("verifications/phone")
    Call<BaseCodeResponse<CodeResponse>> doVerify(@Body ValueRequestBody phone);


    @GET("persons/{id}/sensitive_info")
    Call<BaseCodeResponse<UserSensitiveEntity>>  getSensitiveUser(
            @Path("id") String uid) ;

    @GET("persons/{id}/addresses")
    Call<UserAddressListResponse> getDetailAddressList(
            @Path("id") String uid);


    @POST("oss_authorization/picture")
    Call<UploadAccessResponse> getUploadUrl(@Query("me")String id,@Body UploadAccessBody body);

    @POST("persons/{id}/authentication")
    Call<BaseCodeResponse<UserSensitiveEntity>> doAuthentication(@Path("id") String id, @Body AuthorityRequest request);

    @PUT("persons/{id}/sensitive_info")
    Call<BaseCodeResponse<CodeResponse>> updatePhone(@Path("id") String id, @Body PhoneUpdateBody body);

}
