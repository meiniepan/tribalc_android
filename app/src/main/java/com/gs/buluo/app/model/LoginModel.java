package com.gs.buluo.app.model;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.bean.RequestBodyBean.VerifyBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.UserAddressListResponse;
import com.gs.buluo.app.bean.ResponseBody.UserAddressResponse;
import com.gs.buluo.app.bean.ResponseBody.UserBeanResponse;
import com.gs.buluo.app.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.app.bean.ResponseBody.UserInfoResponse;
import com.gs.buluo.app.bean.ResponseBody.UserSensitiveResponse;
import com.gs.buluo.app.network.AddressService;
import com.gs.buluo.app.network.MainService;
import com.gs.buluo.app.network.TribeRetrofit;

import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/3.
 */
public class LoginModel {             //登录数据同步
    public void doLogin(Map<String, String> params, Callback<UserBeanResponse> callback) {
        LoginBody bean = new LoginBody();
        bean.phone = params.get(Constant.PHONE);
        bean.verificationCode = params.get(Constant.PHONE);
        TribeRetrofit.getIntance().createApi(MainService.class).
                doLogin(bean).enqueue(callback);
    }

    public void doVerify(String phone, Callback<CodeResponse> callback) {
        TribeRetrofit.getIntance().createApi(MainService.class).
                doVerify(new VerifyBody(phone)).enqueue(callback);
    }

    public void getUserInfo(String uid, Callback<UserInfoResponse> callback) {
        TribeRetrofit.getIntance().createApi(MainService.class).
                getUser(uid).enqueue(callback);
    }
    public void updateUser(String id,String key,String value, CommonCallback<String> callback) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "persons/" + id+"/"+key);
        params.setHeader("Content-Type", "application/json");
        params.setHeader("Accept", "application/json");
        params.setAsJsonContent(true);
        if (key.equals(Constant.AREA)){
            String str[]=value.split("-");
            String province= str[0];
            String city= str[1];
            String district= str[2];
            params.addBodyParameter(Constant.PROVINCE,province);
            params.addBodyParameter(Constant.CITY,city);
            params.addBodyParameter(Constant.DISTRICT,district);
        }else {
            params.addBodyParameter(key,value);
        }
        x.http().request(HttpMethod.PUT,params,callback);
    }

    public void getSensitiveUserInfo(String uid, Callback<UserSensitiveResponse> callback) {
        TribeRetrofit.getIntance().createApi(MainService.class).
                getSensitiveUser(uid).enqueue(callback);
    }

    public void getAddressList(String uid,Callback<UserAddressListResponse> callback){
        TribeRetrofit.getIntance().createApi(MainService.class).
                getDetailAddressList(uid).enqueue(callback);
    }
}
