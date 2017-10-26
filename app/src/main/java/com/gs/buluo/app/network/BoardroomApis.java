package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.ConferenceRoom;
import com.gs.buluo.app.bean.ResponseBody.ConferenceRoomResponse;
import com.gs.buluo.common.network.BaseResponse;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by hjn on 2017/10/23.
 */

public interface BoardroomApis {
    @GET("conference_rooms")
    Observable<BaseResponse<List<ConferenceRoom>>> getBoardroomFilterList(
            @QueryMap Map<String, String> map);
//            @Query("floor") int floor, @Query("attendance") int attendance,
//            @Query("searchDate") String searchDate, @Query("equipments") String equipments, @Query("duration") int duration);

    @GET("conference_rooms/reservation?limitSize=10")
    Observable<BaseResponse<ConferenceRoomResponse>> getRoomRecord();

    @GET("conference_rooms/reservation?limitSize=10")
    Observable<BaseResponse<ConferenceRoomResponse>> getRoomRecordMore(@Query("sortSkip") String sortSkip);
}
