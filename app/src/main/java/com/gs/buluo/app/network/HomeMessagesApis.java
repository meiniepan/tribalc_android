package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.HomeMessageEnum;
import com.gs.buluo.app.bean.HomeMessageResponse;
import com.gs.buluo.common.network.BaseResponse;

import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Solang on 2017/7/20.
 */

public interface HomeMessagesApis {
    @GET("members/{ownerId}/homeMessages")
    Observable<BaseResponse<HomeMessageResponse>> getMessage(@Path("ownerId") String uid, @Query("limit") int limit);

    @GET("members/{ownerId}/homeMessages")
    Observable<BaseResponse<HomeMessageResponse>> getMessageMore(@Path("ownerId") String uid, @Query("limit") int limit, @Query("sinceTime") long sinceTime, @Query("isNew") boolean isNew);

    @PUT("members/{ownerId}/homeMessages/{messageId}/state")
    Observable<BaseResponse> ignoreMessage(@Path("ownerId")String uid, @Path("messageId")String mid);

    @PUT("members/{ownerId}/homeMessageTypeShield/{messageType}")
    Observable<BaseResponse> refuseMessage(@Path("ownerId")String uid,@Path("messageType")HomeMessageEnum type);

}
