package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.UserInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/3.
 */
public interface MainService {

    @GET("")
    Call<UserInfo> repoUser(
            @Query("api_key") String api_key,
            @Query("page") String page);

//    @GET("")
//    Call<UserInfo> repoUser(
//            @Query("api_key") String api_key,
//            @Query("page") String page);
}
