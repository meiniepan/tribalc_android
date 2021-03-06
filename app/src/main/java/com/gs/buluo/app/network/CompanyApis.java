package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.CompanyDetail;
import com.gs.buluo.app.bean.RequestBodyBean.ValueBody;
import com.gs.buluo.app.bean.ResponseBody.CommunityResponse;
import com.gs.buluo.app.bean.ResponseBody.CompanyResponse;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.view.widget.panel.CompanyRentPlanItem;
import com.gs.buluo.common.network.BaseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by fs on 2016/12/9.
 */
public interface CompanyApis {
    @GET("companies")
    Call<CompanyResponse> getCompaniesList(
            @Query("communityId") String communityId);

    @POST("persons/{id}/company_bind_request")
    Observable<BaseResponse<UserInfoEntity>> bindCompany(
            @Path("id") String id, @Body ValueBody requestBody);

    @GET("persons/{id}/company_bind_request")
    Observable<BaseResponse<CompanyDetail>> queryCompany(
            @Path("id") String id);

    @GET("communities")
    Observable<CommunityResponse> getCommunitiesList();

    /**
     * 获取当前企业还款计划（当多个时，仅取其中一条。集合为空即没有待还款计划）
     * @param ownerId
     * @return
     */
    @GET("rent_plan_items?status=ACTIVED")
    Observable<BaseResponse<List<CompanyRentPlanItem>>> getCompanyActiveRentPlan(@Query("ownerId") String ownerId, @Query("me") String uid);
}
