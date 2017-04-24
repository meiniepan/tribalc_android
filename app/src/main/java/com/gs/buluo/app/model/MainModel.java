package com.gs.buluo.app.model;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.TribeApplication;

import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by hjn on 2016/11/3.
 */
public class MainModel {             //登录数据同步,上传，验证码


    public void updateUser(String id, String key, String value, CommonCallback<String> callback) {
        RequestParams params = new RequestParams(Constant.Base.BASE_URL + "persons/" + id + "/" + key);
        params.setHeader("Content-Type", "application/json");
        params.setHeader("Accept", "application/json");
        if (TribeApplication.getInstance().getUserInfo() != null && TribeApplication.getInstance().getUserInfo().getToken() != null) {
            params.setHeader("Authorization", TribeApplication.getInstance().getUserInfo().getToken());
        }
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
}
