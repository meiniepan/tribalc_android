package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.ConferenceEquipment;
import com.gs.buluo.app.bean.ConferenceReserveDetail;
import com.gs.buluo.app.bean.ConferenceReservationDateEntity;
import com.gs.buluo.app.bean.ConferenceRoom;
import com.gs.buluo.app.bean.ContactsPersonEntity;
import com.gs.buluo.app.bean.RequestBodyBean.ConferenceReserveEntity;
import com.gs.buluo.app.bean.RequestBodyBean.ValueBody;
import com.gs.buluo.app.bean.ResponseBody.CompanyBoardroomResponse;
import com.gs.buluo.app.bean.ResponseBody.ConferenceRoomResponse;
import com.gs.buluo.common.network.BaseResponse;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by hjn on 2017/10/23.
 */

public interface BoardroomApis {
    @GET("conference_rooms/search")
    Observable<BaseResponse<List<ConferenceRoom>>> getBoardroomFilterList(
            @QueryMap Map<String, String> map);

    @GET("conference_rooms/reservation?limitSize=10")
    Observable<BaseResponse<ConferenceRoomResponse>> getRoomRecord();

    @GET("conference_rooms/reservation?limitSize=10")
    Observable<BaseResponse<ConferenceRoomResponse>> getRoomRecordMore(@Query("sortSkip") String sortSkip);

    @PUT("conference_rooms/reservation/{id}/status")
    Observable<BaseResponse> cancelReserveRoom(@Path("id") String rid);

    @PUT("conference_rooms/reservation/{id}/time")
    Observable<BaseResponse> delayReserveRoom(@Path("id") String rid, @Body ValueBody request);

    @GET("equipments")
    Observable<BaseResponse<List<ConferenceEquipment>>> getEquipments(@Query("me") String uid);

    /**
     * 创建会议室预订信息
     *
     * @param rId
     * @param entity
     * @return
     */
    @POST("conference_rooms/{id}/reservation")
    Observable<BaseResponse> createReserveInfo(@Path("id") String rId, @Body ConferenceReserveEntity entity);

    /**
     * 会议室具体日期的预定信息
     *
     * @param rId
     * @param searchDate
     * @return
     */
    @GET("conference_rooms/{id}/reservation_date")
    Observable<BaseResponse<ConferenceReservationDateEntity>> searchReservationOfDate
    (@Path("id") String rId, @Query("searchDate") long searchDate);

    @GET("conference_rooms/reservation/{id}")
    Observable<BaseResponse<ConferenceReserveDetail>> getRoomReserveDetail(@Path("id") String id);

    @GET("conference_rooms/reservation/{id}/time")
    Observable<BaseResponse<Long>> getAvailableDelayTime(@Path("id") String rid);

    @GET("conference_rooms/reservation?limitSize=20")
    Observable<BaseResponse<CompanyBoardroomResponse>> getCompanyBoardroomRecord(@Query("companyId") String companyID);

    @GET("conference_rooms/reservation?limitSize=20")
    Observable<BaseResponse<CompanyBoardroomResponse>> getCompanyBoardroomRecordMore(@Query("companyId") String companyID, @Query("sortSkip") String sortSkip);

    @PUT("conference_rooms/reservation/{id}")
    Observable<BaseResponse> updateReservation(@Path("id") String rid, @Body ConferenceReserveEntity entity);

    /**
     * 获取常用联系人
     * @return
     */
    @GET("conference_rooms/addressBook")
    Observable<BaseResponse<List<ContactsPersonEntity>>> getCommonContact();
}
