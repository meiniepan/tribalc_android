package com.gs.buluo.app.model;

import android.util.Log;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.ConstantKey;
import com.gs.buluo.app.bean.RequestBodyBean.VerifyBody;
import com.gs.buluo.app.bean.ResponseBody.UpdateResponse;
import com.gs.buluo.app.bean.UserBeanResponse;
import com.gs.buluo.app.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.app.bean.UserInfoResponse;
import com.gs.buluo.app.bean.UserSetting;
import com.gs.buluo.app.network.MainService;
import com.gs.buluo.app.network.TribeRetrofit;

import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/3.
 */
public class LoginModel {             //与登录相关逻辑和处理
    public void doLogin(Map<String, String> params, Callback<UserBeanResponse> callback) {
        LoginBody bean = new LoginBody();
        bean.phone = params.get(ConstantKey.PHONE);
        bean.verificationCode = params.get(ConstantKey.PHONE);
        TribeRetrofit.getIntance().createApi(MainService.class).
                doLogin(bean).enqueue(callback);
    }

    public void doVerify(String phone, Callback<UserBeanResponse> callback) {
        TribeRetrofit.getIntance().createApi(MainService.class).
                doVerify(new VerifyBody(phone)).enqueue(callback);
    }

    public void getUserInfo(String uid, Callback<UserInfoResponse> callback) {
        TribeRetrofit.getIntance().createApi(MainService.class).
                getUser(uid).enqueue(callback);
    }

    public void updateUser(String id,String key,String value, CommonCallback<String> callback) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "persons/" + id);
        params.setHeader("Content-Type", "application/json");
        params.setHeader("Accept", "application/json");
        params.setAsJsonContent(true);
        params.addBodyParameter(key,value);
//        x.http().request(HttpMethod.PUT,params,callback);

        TribeRetrofit.getIntance().createApi(MainService.class).
                updateUser(id,value).enqueue(new Callback<UpdateResponse>() {
            @Override
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                Log.d("aaaaa", "onResponse: re");
            }

            @Override
            public void onFailure(Call<UpdateResponse> call, Throwable t) {
                Log.d("aaaaa", "onResponse: re");
            }
        });
    }
}
