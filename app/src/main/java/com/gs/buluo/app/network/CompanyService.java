package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.CompanyDetail;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.CompanyResponse;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;

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
    Call<BaseCodeResponse<CodeResponse>> bindCompany(
            @Path("id") String id, @Body ValueRequestBody requestBody);

    @GET("persons/{id}/company_bind_request")
    Call<BaseCodeResponse<CompanyDetail>> queryCompany(
            @Path("id") String id);
}
