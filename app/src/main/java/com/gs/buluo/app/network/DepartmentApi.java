package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.LockPwdRequestBody;
import com.gs.buluo.app.bean.RentProtocol;
import com.gs.buluo.app.bean.RequestBodyBean.RentPlanItem;
import com.gs.buluo.app.bean.RequestBodyBean.RentProtocolWithholdInfo;
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
 * Created by hjn on 2017/6/26.
 */

public interface DepartmentApi {
    @GET("rent_protocols")
    Observable<BaseResponse<List<RentProtocol>>> getDepartmentList(@Query("me") String uid, @Query("ownerId") String oid);

    /**
     * 查询缴租协议计划项
     * @param protocloId
     * @param sid
     * @return
     */
    @GET("rent_protocols/{id}/plan_item")
    Observable<BaseResponse<RentPlanItem>> getRentPlanItem(@Path("id") String protocloId, @Query("me") String sid);

    /**
     * 查询缴租协议计划列表
     * @param protocloId
     * @return
     */
    @GET("rent_protocols/{id}/plan_item?&fetchAll=true")
    Observable<BaseResponse<List<RentPlanItem>>> getRentPlanItems(@Path("id") String protocloId,@Query("me") String uid);

    /**
     * 修改代扣信息
     * @param id
     * @param rentProtocolWithholdInfo
     * @param uid
     * @return
     */
    @PUT("rent_protocols/{id}/withhold")
    Observable<BaseResponse> updateWithholdInfo(@Path("id") String id, @Body RentProtocolWithholdInfo rentProtocolWithholdInfo, @Query("me") String uid);

    /**
     * 查询代扣信息
     * @param id
     * @param uid
     * @return
     */
    @GET("rent_protocols/{id}/withhold")
    Observable<BaseResponse<RentProtocolWithholdInfo>> getWithholdInfo(@Path("id") String id, @Query("me") String uid);

    @GET("smart_locks/apply_temporary_password")
    Observable<BaseResponse<String>> getTempPwd(@Query("me")String uid,@Query("sourceId")String sourceId,@Query("sn")String sn);

    @POST("smart_locks/password")
    Observable<BaseResponse> updateLockPwd(@Query("me")String uid, @Query("sourceId")String sourceId, @Body LockPwdRequestBody body);
}
