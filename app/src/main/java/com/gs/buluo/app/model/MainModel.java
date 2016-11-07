package com.gs.buluo.app.model;

import com.gs.buluo.app.bean.UserInfo;
import com.gs.buluo.app.network.MainService;
import com.gs.buluo.app.network.TribeRetrofit;

import org.xutils.x;

import java.util.Map;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/3.
 */
public class MainModel{
    public void getUser(Map<String, String> params, Callback<UserInfo> callback){
//        TribeRetrofit.getIntance().createApi(MainService.class).repoUser("","").enqueue(callback);
    }


}
