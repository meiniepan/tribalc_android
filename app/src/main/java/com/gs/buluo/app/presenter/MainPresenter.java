package com.gs.buluo.app.presenter;

import android.content.Context;

import com.gs.buluo.app.bean.UserInfo;
import com.gs.buluo.app.model.MainModel;
import com.gs.buluo.app.view.impl.IMainView;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/3.
 */
public class MainPresenter extends BasePresenter<IMainView> {

    private final MainModel mainModel;

    public MainPresenter(){
        mainModel = new MainModel();
    }

    public void getUser(Context context,Map<String, String> params){
        mainModel.getUser(params, new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo userInfo=response.body();
                userInfo.setUid(100001);
                mView.setUser(userInfo);
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                mView.showError();
            }
        });
    }
}
