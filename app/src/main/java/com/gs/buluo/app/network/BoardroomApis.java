package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.ConferenceRoom;
import com.gs.buluo.app.bean.ResponseBody.CompanyResponse;
import com.gs.buluo.common.network.BaseResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hjn on 2017/10/23.
 */

public interface BoardroomApis {
    @GET("conference_rooms")
    Observable<BaseResponse<ConferenceRoom>> getBoardroomList(
            @Query("me") String uid,@Query("floor")int floor,  @Query("attendance") int attendance,
    @Query("searchDate")String searchDate,@Query("equipMents")String equipMents,@Query("duration")int duration);
}
