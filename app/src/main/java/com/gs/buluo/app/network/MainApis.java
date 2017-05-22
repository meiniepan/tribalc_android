package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.ConfigInfo;
import com.gs.buluo.app.bean.RequestBodyBean.AuthorityRequest;
import com.gs.buluo.app.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.app.bean.RequestBodyBean.PhoneUpdateBody;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.SignRecordResponse;
import com.gs.buluo.app.bean.ResponseBody.UploadAccessBody;
import com.gs.buluo.app.bean.ResponseBody.UploadResponseBody;
import com.gs.buluo.app.bean.ResponseBody.UserBeanEntity;
import com.gs.buluo.app.bean.SignResponse;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.common.network.BaseResponse;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hjn on 2016/11/3.
 */
public interface MainApis {
    @GET("configs/init?os=android")
    Observable<BaseResponse<ConfigInfo>> getConfig(
            @Query("selfId") String uid  ,@Query("version") String version);

    @GET("persons/{id}")
    Observable<BaseResponse<UserInfoEntity>> getUser(
            @Path("id") String uid);

    @POST("persons/login")
    Observable<BaseResponse<UserBeanEntity>> doLogin(@Body LoginBody params);

    @POST("verifications/phone")
    Observable<BaseResponse<CodeResponse>> doVerify(@Body ValueRequestBody phone);

    @GET("persons/{id}/addresses")
    Observable<BaseResponse<List<UserAddressEntity>>> getDetailAddressList(
            @Path("id") String uid);

    @POST("oss_authorization/picture")
    Observable<BaseResponse<UploadResponseBody>> getUploadUrl(@Query("me")String id, @Body UploadAccessBody body);

    @POST("persons/{id}/authentication")
    Observable<BaseResponse<UserInfoEntity>> doAuthentication(@Path("id") String id, @Body AuthorityRequest request);

    @PUT("persons/{id}/phone")
    Observable<BaseResponse<CodeResponse>> updatePhone(@Path("id") String id, @Body PhoneUpdateBody body);

    @PUT("activities/signin")
    Observable<BaseResponse<SignResponse>> signIn(@Query("me") String id);

    @GET("activities/signin")
    Observable<BaseResponse<SignRecordResponse>> getSignRecord(@Query("me") String id);
}
