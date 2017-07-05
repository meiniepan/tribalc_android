package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.LockPwdRequestBody;
import com.gs.buluo.app.bean.RentProtocol;
import com.gs.buluo.common.network.BaseResponse;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hjn on 2017/6/26.
 */

public interface DepartmentApi {
    @GET("rent_protocols")
    Observable<BaseResponse<List<RentProtocol>>> getDepartmentList(@Query("me")String uid, @Query("ownerId")String oid);

    @GET("smart_locks/apply_temporary_password")
    Observable<BaseResponse<String>> getTempPwd(@Query("me")String uid,@Query("sourceId")String sourceId,@Query("sn")String sn);

    @POST("smart_locks/password")
    Observable<BaseResponse> updateLockPwd(@Query("me")String uid, @Query("sourceId")String sourceId, @Body LockPwdRequestBody body);
}
