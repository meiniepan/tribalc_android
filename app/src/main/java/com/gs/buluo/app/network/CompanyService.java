package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.RequestBodyBean.BindCompanyRequestBody;
import com.gs.buluo.app.bean.ResponseBody.CompanyQueryResponse;
import com.gs.buluo.app.bean.ResponseBody.CompanyResponse;
import com.gs.buluo.app.bean.ResponseBody.SimpleCodeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by fs on 2016/12/9.
 */
public interface CompanyService {
    @GET("companies")
    Call<CompanyResponse> getCompaniesList(
            @Query("communityId") String communityId);

    @POST("persons/{id}/company_bind_request")
    Call<SimpleCodeResponse> bindCompany(
            @Path("id") String id, @Body BindCompanyRequestBody requestBody);

    @GET("persons/{id}/company_bind_request")
    Call<CompanyQueryResponse> queryCompany(
            @Path("id") String id);
}
