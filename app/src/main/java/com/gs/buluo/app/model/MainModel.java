package com.gs.buluo.app.model;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.RequestBodyBean.AuthorityRequest;
import com.gs.buluo.app.bean.RequestBodyBean.PhoneUpdateBody;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.UploadAccessBody;
import com.gs.buluo.app.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.app.bean.ResponseBody.UserAddressListResponse;
import com.gs.buluo.app.bean.ResponseBody.UserBeanResponse;
import com.gs.buluo.app.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.app.bean.ResponseBody.UserInfoResponse;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.network.MainService;
import com.gs.buluo.app.network.TribeRetrofit;

import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.MD5;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/3.
 */
public class MainModel {             //登录数据同步,上传，验证码
    public void doLogin(Map<String, String> params, Callback<UserBeanResponse> callback) {
        LoginBody bean = new LoginBody();
        bean.phone = params.get(Constant.PHONE);
        bean.verificationCode = params.get(Constant.VERIFICATION);
        TribeRetrofit.getInstance().createApi(MainService.class).
                doLogin(bean).enqueue(callback);
    }

    public void doVerify(String phone, Callback<BaseCodeResponse<CodeResponse>> callback) {
        TribeRetrofit.getInstance().createApi(MainService.class).
                doVerify(new ValueRequestBody(phone)).enqueue(callback);
    }

    public void getUserInfo(String uid, Callback<UserInfoResponse> callback) {
        TribeRetrofit.getInstance().createApi(MainService.class).
                getUser(uid).enqueue(callback);
    }

    public void updateUser(String id, String key, String value, CommonCallback<String> callback) {
        RequestParams params = new RequestParams(Constant.Base.BASE_URL + "persons/" + id + "/" + key);
        params.setHeader("Content-Type", "application/json");
        params.setHeader("Accept", "application/json");
        params.setAsJsonContent(true);
        if (key.equals(Constant.AREA)) {
            String str[] = value.split("-");
            String province = str[0];
            String city = str[1];
            String district = str[2];
            params.addBodyParameter(Constant.PROVINCE, province);
            params.addBodyParameter(Constant.CITY, city);
            params.addBodyParameter(Constant.DISTRICT, district);
        } else {
            params.addBodyParameter(key, value);
        }
        x.http().request(HttpMethod.PUT, params, callback);
    }

    public void getSensitiveUserInfo(String uid, Callback<BaseCodeResponse<UserSensitiveEntity>> callback) {
        TribeRetrofit.getInstance().createApi(MainService.class).
                getSensitiveUser(uid).enqueue(callback);
    }

    public void getAddressList(String uid, Callback<UserAddressListResponse> callback) {
        TribeRetrofit.getInstance().createApi(MainService.class).
                getDetailAddressList(uid).enqueue(callback);
    }


    public void uploadFile(File file, String name, String type, Callback<UploadAccessResponse> callback) {
        UploadAccessBody body = new UploadAccessBody();
        body.key = name;
        body.contentType = type;
        try {
            body.contentMD5 = MD5.md5(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
//            body.contentMD5 = "98d8826e6308556a4a0ed87e265e2573";
        TribeRetrofit.getInstance().createApi(MainService.class).
                getUploadUrl(TribeApplication.getInstance().getUserInfo().getId(), body).enqueue(callback);
    }

    public void doAuthentication(String name, String sex, long birthday, String idNo, Callback<BaseCodeResponse<UserSensitiveEntity>> callback) {
        AuthorityRequest request = new AuthorityRequest();
        request.birthday = birthday + "";
        request.idNo = idNo;
        request.name = name;
        request.personSex = sex;
        TribeRetrofit.getInstance().createApi(MainService.class).
                doAuthentication(TribeApplication.getInstance().getUserInfo().getId(), request).enqueue(callback);
    }

    public void updatePhone(String phone, String code, Callback<BaseCodeResponse<CodeResponse>> callback) {
        PhoneUpdateBody body = new PhoneUpdateBody();
        body.phone = phone;
        body.verificationCode = code;
        TribeRetrofit.getInstance().createApi(MainService.class).
                updatePhone(TribeApplication.getInstance().getUserInfo().getId(), body).enqueue(callback);
    }
}
