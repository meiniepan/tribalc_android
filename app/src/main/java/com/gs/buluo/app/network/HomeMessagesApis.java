package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.HomeMessageEnum;
import com.gs.buluo.app.bean.HomeMessageResponse;
import com.gs.buluo.app.bean.MessageToggleBean;
import com.gs.buluo.app.bean.RequestBodyBean.ValueBody;
import com.gs.buluo.app.bean.RequestBodyBean.XgMessageReadBody;
import com.gs.buluo.app.bean.XgMessageResponse;
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
 * Created by Solang on 2017/7/20.
 */

public interface HomeMessagesApis {
    @GET("members/{ownerId}/homeMessages")
    Observable<BaseResponse<HomeMessageResponse>> getMessage(@Path("ownerId") String uid, @Query("limit") int limit);

    @GET("members/{ownerId}/homeMessages")
    Observable<BaseResponse<HomeMessageResponse>> getMessageMore(@Path("ownerId") String uid, @Query("limit") int limit, @Query("sinceTime") long sinceTime, @Query("isNew") boolean isNew);

    @PUT("members/{ownerId}/homeMessages/{messageId}/state")
    Observable<BaseResponse> ignoreMessage(@Path("ownerId") String uid, @Path("messageId") String mid);

    @PUT("members/{ownerId}/homeMessageTypeShield/{messageType}")
    Observable<BaseResponse> refuseMessage(@Path("ownerId") String uid, @Path("messageType") HomeMessageEnum type);

    @GET("members/{ownerId}/homeMessages/types/state?appType=PERSON")
    Observable<BaseResponse<List<MessageToggleBean>>> getMessageToggleList(@Path("ownerId") String uid, @Query("isAgent") boolean isAgent);

    @POST("members/{ownerId}/homeMessages/types/{messageType}/state")
    Observable<BaseResponse> toggleMessageStatus(@Path("ownerId") String uid, @Path("messageType") HomeMessageEnum messageType, @Body ValueBody body);

    /**
     * 查看未阅读的PUSH消息
     *
     * @param uid
     * @return
     */
    @GET("members/{ownerId}/xgMessages/count")
    Observable<BaseResponse<XgMessageResponse>> getXgMessage(@Path("ownerId") String uid);

    /**
     * 阅读某一类型消息
     *
     * @param uid
     * @param body
     * @return
     */
    @POST("members/{ownerId}/xgMessages/read")

    Observable<BaseResponse<XgMessageResponse>> readXgMessage(@Path("ownerId") String uid, @Body XgMessageReadBody body);

}
