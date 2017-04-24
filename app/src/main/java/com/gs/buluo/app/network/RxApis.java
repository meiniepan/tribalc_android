package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.app.bean.ResponseBody.UserBeanEntity;
import com.gs.buluo.common.network.BaseResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Solang on 2017/3/29.
 */

public interface RxApis {

    @POST("persons/login")
    Observable<BaseResponse<UserBeanEntity>> doLogin(@Body LoginBody params);

}
