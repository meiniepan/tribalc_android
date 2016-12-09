package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.ResponseBody.CompanyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by fs on 2016/12/9.
 */
public interface CompanyService {
    @GET("companies/{id}")
    Call<CompanyResponse> getCompaniesList(@Path("id") String communityId);
}
